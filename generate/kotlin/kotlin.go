// Package kotlin emits a plain Kotlin model library from a metamodel.Document.
//
// One file per model type, plus three hand-written runtime files (FintObject,
// FintResource, Link). The mapping is deliberately minimal:
//
//   - abstrakt        -> interface (safe: inheritance in the model only ever
//     targets abstrakt parents), declaring the type's own attributes
//   - everything else -> data class with the pre-flattened attribute list from
//     the JSON as nullable var constructor parameters; inherited attributes
//     get the override modifier since the parent interface declares them
//   - types with no attributes -> plain class (data classes need parameters)
//
// A type is a resource — implements FintResource and carries a links map —
// iff it is a hovedklasse or its flattened relation list is non-empty. This
// matches the old Java emitter's isResource rule.
package kotlin

import (
	"fmt"
	"sort"
	"strings"

	"github.com/FINTLabs/fint-model/common/config"
	"github.com/FINTLabs/fint-model/common/metamodel"
)

var primitiveTypes = map[string]string{
	"string":   "String",
	"boolean":  "Boolean",
	"int":      "Int",
	"long":     "Long",
	"float":    "Float",
	"double":   "Double",
	"date":     "LocalDate",
	"datetime": "LocalDateTime",
}

var primitiveImports = map[string]string{
	"date":     "java.time.LocalDate",
	"datetime": "java.time.LocalDateTime",
}

type typeEntry struct {
	component string
	t         *metamodel.Type
}

func Files(doc *metamodel.Document) (map[string]string, error) {
	index := make(map[string]typeEntry)
	for ci := range doc.Components {
		comp := &doc.Components[ci]
		for ti := range comp.Types {
			t := &comp.Types[ti]
			index[comp.Name+":"+t.Name] = typeEntry{component: comp.Name, t: t}
		}
	}

	files := runtimeFiles()
	for ci := range doc.Components {
		comp := &doc.Components[ci]
		for ti := range comp.Types {
			t := &comp.Types[ti]
			content, err := render(comp.Name, t, index)
			if err != nil {
				return nil, err
			}
			files[filePath(comp.Name, t.Name)] = content
		}
	}
	return files, nil
}

func packageFor(component string) string {
	return config.KOTLIN_PACKAGE_BASE + "." + strings.ReplaceAll(component, "-", ".")
}

func filePath(component, typeName string) string {
	return strings.ReplaceAll(packageFor(component), ".", "/") + "/" + typeName + ".kt"
}

func isResource(t *metamodel.Type) bool {
	return t.Stereotype == metamodel.StereotypeMain || len(t.Relations) > 0
}

func render(component string, t *metamodel.Type, index map[string]typeEntry) (string, error) {
	pkg := packageFor(component)
	resource := isResource(t)
	abstract := t.Stereotype == metamodel.StereotypeAbstract

	imports := map[string]struct{}{}
	addImport := func(imp string) {
		if imp != "" {
			imports[imp] = struct{}{}
		}
	}

	var supers []string
	parentResource := false
	if t.Parent != nil {
		parent, ok := index[*t.Parent]
		if !ok {
			return "", fmt.Errorf("%s:%s: unresolved parent %q", component, t.Name, *t.Parent)
		}
		if parent.t.Stereotype != metamodel.StereotypeAbstract {
			return "", fmt.Errorf("%s:%s: parent %q is %s, expected abstrakt", component, t.Name, *t.Parent, parent.t.Stereotype)
		}
		supers = append(supers, parent.t.Name)
		if parent.component != component {
			addImport(packageFor(parent.component) + "." + parent.t.Name)
		}
		parentResource = isResource(parent.t)
	}
	if resource && !parentResource {
		supers = append(supers, "FintResource")
		addImport(config.KOTLIN_PACKAGE_BASE + ".FintResource")
	}
	if !resource && t.Parent == nil {
		supers = append(supers, "FintObject")
		addImport(config.KOTLIN_PACKAGE_BASE + ".FintObject")
	}

	attrs := t.Attributes
	if abstract {
		attrs = ownAttributes(t)
	}

	type property struct {
		name     string
		typ      string
		override bool
	}
	properties := make([]property, 0, len(attrs))
	for i := range attrs {
		a := &attrs[i]
		typ, imp, err := kotlinType(a, component, index)
		if err != nil {
			return "", fmt.Errorf("%s:%s: %w", component, t.Name, err)
		}
		addImport(imp)
		properties = append(properties, property{name: a.Name, typ: typ, override: a.Inherited})
	}

	linksBody := resource && !abstract
	if linksBody {
		addImport(config.KOTLIN_PACKAGE_BASE + ".Link")
	}

	var b strings.Builder
	b.WriteString("package " + pkg + "\n\n")
	for _, imp := range sortedKeys(imports) {
		b.WriteString("import " + imp + "\n")
	}
	if len(imports) > 0 {
		b.WriteString("\n")
	}

	superClause := ""
	if len(supers) > 0 {
		superClause = " : " + strings.Join(supers, ", ")
	}

	if abstract {
		b.WriteString("interface " + t.Name + superClause)
		if len(properties) > 0 {
			b.WriteString(" {\n")
			for _, p := range properties {
				b.WriteString("    var " + p.name + ": " + p.typ + "?\n")
			}
			b.WriteString("}")
		}
		b.WriteString("\n")
		return b.String(), nil
	}

	if len(properties) > 0 {
		b.WriteString("data class " + t.Name + "(\n")
		for _, p := range properties {
			b.WriteString("    ")
			if p.override {
				b.WriteString("override ")
			}
			b.WriteString("var " + p.name + ": " + p.typ + "? = null,\n")
		}
		b.WriteString(")")
	} else {
		b.WriteString("class " + t.Name)
	}
	b.WriteString(superClause)
	if linksBody {
		b.WriteString(" {\n    override val links: MutableMap<String, MutableList<Link>> = mutableMapOf()\n}")
	}
	b.WriteString("\n")
	return b.String(), nil
}

func ownAttributes(t *metamodel.Type) []metamodel.Attribute {
	own := make([]metamodel.Attribute, 0, len(t.Attributes))
	for _, a := range t.Attributes {
		if !a.Inherited {
			own = append(own, a)
		}
	}
	return own
}

func kotlinType(a *metamodel.Attribute, component string, index map[string]typeEntry) (string, string, error) {
	var name, imp string
	if strings.Contains(a.Type, ":") {
		target, ok := index[a.Type]
		if !ok {
			return "", "", fmt.Errorf("attribute %q: unresolved type %q", a.Name, a.Type)
		}
		name = target.t.Name
		if target.component != component {
			imp = packageFor(target.component) + "." + target.t.Name
		}
	} else {
		mapped, ok := primitiveTypes[a.Type]
		if !ok {
			return "", "", fmt.Errorf("attribute %q: unknown primitive %q", a.Name, a.Type)
		}
		name = mapped
		imp = primitiveImports[a.Type]
	}
	if a.List {
		name = "List<" + name + ">"
	}
	return name, imp, nil
}

func sortedKeys(set map[string]struct{}) []string {
	keys := make([]string, 0, len(set))
	for k := range set {
		keys = append(keys, k)
	}
	sort.Strings(keys)
	return keys
}

func runtimeFiles() map[string]string {
	pkg := config.KOTLIN_PACKAGE_BASE
	dir := strings.ReplaceAll(pkg, ".", "/")
	return map[string]string{
		dir + "/FintObject.kt": "package " + pkg + "\n\ninterface FintObject\n",
		dir + "/FintResource.kt": "package " + pkg + "\n\ninterface FintResource : FintObject {\n" +
			"    val links: MutableMap<String, MutableList<Link>>\n}\n",
		dir + "/Link.kt": "package " + pkg + "\n\ndata class Link(var href: String? = null)\n",
	}
}
