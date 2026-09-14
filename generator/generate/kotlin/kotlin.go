// Package kotlin emits the FINT Kotlin model library from a metamodel.Document.
//
// One file per model type, a generated FintModel registry, and a small set of
// hand-written runtime files. The mapping:
//
//   - abstrakt        -> interface (safe: inheritance in the model only ever
//     targets abstrakt parents), declaring the type's own attributes
//   - everything else -> data class with the pre-flattened attribute list from
//     the JSON as nullable var constructor parameters; inherited attributes
//     get the override modifier since the parent interface declares them;
//     attribute-less types fall back to plain classes
//
// A type is a resource — implements FintResource, carries a links map, and
// gets FintResourceMetadata — iff it is a hovedklasse or its flattened
// relation list is non-empty. Every other concrete type gets FintTypeMetadata.
// Metadata lives on a named companion (compile-time constant data: ref, path,
// name, isCommon, idFields, attributes, relations with baked target paths and
// inverse multiplicities), and the FintModel object indexes it all by path and
// KClass so consumers never need reflection.
package kotlin

import (
	"fmt"
	"sort"
	"strconv"
	"strings"

	"github.com/FINTLabs/fint-core-information-model/generator/common/config"
	"github.com/FINTLabs/fint-core-information-model/generator/common/metamodel"
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

var multiplicityKinds = map[string]struct{}{
	"EXACTLY_ONE":  {},
	"ZERO_OR_ONE":  {},
	"ONE_OR_MORE":  {},
	"ZERO_OR_MORE": {},
}

type typeEntry struct {
	component string
	t         *metamodel.Type
}

type importSet map[string]struct{}

func (s importSet) add(imp string) {
	if imp != "" {
		s[imp] = struct{}{}
	}
}

func (s importSet) sorted() []string {
	keys := make([]string, 0, len(s))
	for k := range s {
		keys = append(keys, k)
	}
	sort.Strings(keys)
	return keys
}

func buildIndex(doc *metamodel.Document) (map[string]typeEntry, error) {
	index := make(map[string]typeEntry)
	for ci := range doc.Components {
		comp := &doc.Components[ci]
		for ti := range comp.Types {
			t := &comp.Types[ti]
			ref := comp.Name + ":" + t.Name
			if _, dup := index[ref]; dup {
				return nil, fmt.Errorf("duplicate type %q in the model", ref)
			}
			index[ref] = typeEntry{component: comp.Name, t: t}
		}
	}
	return index, nil
}

func Files(doc *metamodel.Document) (map[string]string, error) {
	index, err := buildIndex(doc)
	if err != nil {
		return nil, err
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

	files[baseDir()+"/FintModel.kt"] = renderRegistry(doc)
	return files, nil
}

func packageFor(component string) string {
	return config.KOTLIN_PACKAGE_BASE + "." + strings.ReplaceAll(component, "-", ".")
}

func baseDir() string {
	return strings.ReplaceAll(config.KOTLIN_PACKAGE_BASE, ".", "/")
}

func filePath(component, typeName string) string {
	return strings.ReplaceAll(packageFor(component), ".", "/") + "/" + typeName + ".kt"
}

func isResource(t *metamodel.Type) bool {
	return t.Stereotype == metamodel.StereotypeMain || len(t.Relations) > 0
}

func render(component string, t *metamodel.Type, index map[string]typeEntry) (string, error) {
	if t.Stereotype == metamodel.StereotypeAbstract {
		return renderInterface(component, t, index)
	}
	return renderClass(component, t, index)
}

type property struct {
	name     string
	typ      string
	override bool
}

func resolveSupers(component string, t *metamodel.Type, index map[string]typeEntry, imports importSet) ([]string, error) {
	resource := isResource(t)

	var supers []string
	parentResource := false
	if t.Parent != nil {
		parent, ok := index[*t.Parent]
		if !ok {
			return nil, fmt.Errorf("%s:%s: unresolved parent %q", component, t.Name, *t.Parent)
		}
		if parent.t.Stereotype != metamodel.StereotypeAbstract {
			return nil, fmt.Errorf("%s:%s: parent %q is %s, expected abstrakt", component, t.Name, *t.Parent, parent.t.Stereotype)
		}
		supers = append(supers, parent.t.Name)
		if parent.component != component {
			imports.add(packageFor(parent.component) + "." + parent.t.Name)
		}
		parentResource = isResource(parent.t)
	}
	if resource && !parentResource {
		supers = append(supers, "FintResource")
		imports.add(config.KOTLIN_PACKAGE_BASE + ".FintResource")
	}
	if !resource && t.Parent == nil {
		supers = append(supers, "FintObject")
		imports.add(config.KOTLIN_PACKAGE_BASE + ".FintObject")
	}
	return supers, nil
}

func resolveProperties(component string, t *metamodel.Type, attrs []metamodel.Attribute, index map[string]typeEntry, imports importSet) ([]property, error) {
	properties := make([]property, 0, len(attrs))
	for i := range attrs {
		a := &attrs[i]
		element, imp, err := kotlinElementType(a, component, index)
		if err != nil {
			return nil, fmt.Errorf("%s:%s: %w", component, t.Name, err)
		}
		imports.add(imp)
		typ := element
		if a.List {
			typ = "List<" + element + ">"
		}
		properties = append(properties, property{name: a.Name, typ: typ, override: a.Inherited})
	}
	return properties, nil
}

func renderInterface(component string, t *metamodel.Type, index map[string]typeEntry) (string, error) {
	imports := importSet{}
	supers, err := resolveSupers(component, t, index, imports)
	if err != nil {
		return "", err
	}
	properties, err := resolveProperties(component, t, ownAttributes(t), index, imports)
	if err != nil {
		return "", err
	}

	var b strings.Builder
	writeHeader(&b, packageFor(component), imports)
	b.WriteString("interface " + t.Name + superClause(supers))
	if len(properties) > 0 {
		b.WriteString(" {\n")
		for _, p := range properties {
			b.WriteString("    val " + p.name + ": " + p.typ + "?\n")
		}
		b.WriteString("}")
	}
	b.WriteString("\n")
	return b.String(), nil
}

func renderClass(component string, t *metamodel.Type, index map[string]typeEntry) (string, error) {
	pkg := packageFor(component)
	resource := isResource(t)

	imports := importSet{}
	supers, err := resolveSupers(component, t, index, imports)
	if err != nil {
		return "", err
	}
	properties, err := resolveProperties(component, t, t.Attributes, index, imports)
	if err != nil {
		return "", err
	}

	metaInterface := "FintTypeMetadata"
	if resource {
		metaInterface = "FintResourceMetadata"
	}
	imports.add(config.KOTLIN_PACKAGE_BASE + "." + metaInterface)
	imports.add(config.KOTLIN_PACKAGE_BASE + ".FintAttribute")
	if resource {
		imports.add(config.KOTLIN_PACKAGE_BASE + ".Link")
		imports.add(config.KOTLIN_PACKAGE_BASE + ".IdentifikatorVisitor")
		imports.add(config.KOTLIN_PACKAGE_BASE + ".FintRelation")
		if len(t.Relations) > 0 {
			imports.add(config.KOTLIN_PACKAGE_BASE + ".FintMultiplicity")
		}
	}

	relationLines, err := relationList(component, t, index, imports)
	if err != nil {
		return "", err
	}

	var nested []metamodel.Attribute
	if resource {
		nested = nestedResourceAttributes(t, index)
		if len(nested) > 0 {
			imports.add(config.KOTLIN_PACKAGE_BASE + ".FintResourceVisitor")
		}
	}

	var b strings.Builder
	writeHeader(&b, pkg, imports)

	if len(properties) > 0 {
		b.WriteString("data class " + t.Name + "(\n")
		for _, p := range properties {
			b.WriteString("    ")
			if p.override {
				b.WriteString("override ")
			}
			b.WriteString("val " + p.name + ": " + p.typ + "? = null,\n")
		}
		b.WriteString(")")
	} else {
		b.WriteString("class " + t.Name)
	}
	b.WriteString(superClause(supers))
	b.WriteString(" {\n")

	if resource {
		b.WriteString("    override val links: MutableMap<String, MutableList<Link>> = mutableMapOf()\n\n")
	}
	b.WriteString("    override val metadata: " + metaInterface + " get() = Metadata\n")

	if resource {
		b.WriteString("\n")
		if len(t.IdFields) > 0 {
			b.WriteString("    override fun visitIdentifikators(visitor: IdentifikatorVisitor) {\n")
			for _, f := range t.IdFields {
				b.WriteString("        " + f + "?.identifikatorverdi?.let { visitor.visit(" + strconv.Quote(f) + ", it) }\n")
			}
			b.WriteString("    }\n\n")
			b.WriteString("    override fun identifikatorverdi(field: String): String? = when {\n")
			for _, f := range t.IdFields {
				b.WriteString("        field.equals(" + strconv.Quote(f) + ", ignoreCase = true) -> " + f + "?.identifikatorverdi\n")
			}
			b.WriteString("        else -> null\n")
			b.WriteString("    }\n")
		} else {
			b.WriteString("    override fun visitIdentifikators(visitor: IdentifikatorVisitor) {}\n\n")
			b.WriteString("    override fun identifikatorverdi(field: String): String? = null\n")
		}

		if len(nested) > 0 {
			b.WriteString("\n    override fun visitNested(visitor: FintResourceVisitor) {\n")
			for _, a := range nested {
				quoted := strconv.Quote(a.Name)
				if a.List {
					b.WriteString("        " + a.Name + "?.forEach { visitor.visit(" + quoted + ", it) }\n")
				} else {
					b.WriteString("        " + a.Name + "?.let { visitor.visit(" + quoted + ", it) }\n")
				}
			}
			b.WriteString("    }\n")
		}
	}

	b.WriteString("\n    companion object Metadata : " + metaInterface + " {\n")
	b.WriteString("        override val type = " + t.Name + "::class\n")
	b.WriteString("        override val ref = " + strconv.Quote(component+":"+t.Name) + "\n")
	if resource {
		if t.Path != nil {
			b.WriteString("        override val path = " + strconv.Quote(*t.Path) + "\n")
		} else {
			b.WriteString("        override val path: String? = null\n")
		}
		b.WriteString("        override val name = " + strconv.Quote(strings.ToLower(t.Name)) + "\n")
		b.WriteString(fmt.Sprintf("        override val isCommon = %t\n", t.Common))
		if len(t.IdFields) > 0 {
			quoted := make([]string, len(t.IdFields))
			for i, f := range t.IdFields {
				quoted[i] = strconv.Quote(f)
			}
			b.WriteString("        override val idFields = listOf(" + strings.Join(quoted, ", ") + ")\n")
		} else {
			b.WriteString("        override val idFields = emptyList<String>()\n")
		}
	}
	if len(t.Attributes) > 0 {
		b.WriteString("        override val attributes = listOf(\n")
		for i := range t.Attributes {
			a := &t.Attributes[i]
			element, _, err := kotlinElementType(a, component, index)
			if err != nil {
				return "", fmt.Errorf("%s:%s: %w", component, t.Name, err)
			}
			b.WriteString(fmt.Sprintf("            FintAttribute(%s, %s::class, list = %t, optional = %t),\n",
				strconv.Quote(a.Name), element, a.List, a.Optional))
		}
		b.WriteString("        )\n")
	} else {
		b.WriteString("        override val attributes = emptyList<FintAttribute>()\n")
	}
	if resource {
		if len(relationLines) > 0 {
			b.WriteString("        override val relations = listOf(\n")
			for _, line := range relationLines {
				b.WriteString(line)
			}
			b.WriteString("        )\n")
		} else {
			b.WriteString("        override val relations = emptyList<FintRelation>()\n")
		}
	}
	b.WriteString("    }\n")
	b.WriteString("}\n")
	return b.String(), nil
}

func relationList(component string, t *metamodel.Type, index map[string]typeEntry, imports importSet) ([]string, error) {
	if !isResource(t) || len(t.Relations) == 0 {
		return nil, nil
	}
	lines := make([]string, 0, len(t.Relations))
	for i := range t.Relations {
		r := &t.Relations[i]
		target, ok := index[r.Target]
		if !ok {
			return nil, fmt.Errorf("%s:%s: relation %q: unresolved target %q", component, t.Name, r.Name, r.Target)
		}
		if target.component != component {
			imports.add(packageFor(target.component) + "." + target.t.Name)
		}
		if _, ok := multiplicityKinds[r.MultiplicityKind]; !ok {
			return nil, fmt.Errorf("%s:%s: relation %q: unknown multiplicityKind %q", component, t.Name, r.Name, r.MultiplicityKind)
		}

		targetPath := "null"
		if target.t.Path != nil {
			targetPath = strconv.Quote(*target.t.Path)
		}

		var b strings.Builder
		b.WriteString("            FintRelation(\n")
		b.WriteString("                name = " + strconv.Quote(r.Name) + ",\n")
		b.WriteString("                target = " + target.t.Name + "::class,\n")
		b.WriteString("                targetPath = " + targetPath + ",\n")
		b.WriteString("                multiplicity = FintMultiplicity." + r.MultiplicityKind + ",\n")
		if r.Bidirectional != nil {
			inverse := findRelation(target.t, r.Bidirectional.InverseName)
			if inverse == nil {
				return nil, fmt.Errorf("%s:%s: relation %q: target %q has no inverse relation %q",
					component, t.Name, r.Name, r.Target, r.Bidirectional.InverseName)
			}
			if _, ok := multiplicityKinds[inverse.MultiplicityKind]; !ok {
				return nil, fmt.Errorf("%s:%s: relation %q: inverse %q has unknown multiplicityKind %q",
					component, t.Name, r.Name, inverse.Name, inverse.MultiplicityKind)
			}
			imports.add(config.KOTLIN_PACKAGE_BASE + ".Bidirectional")
			b.WriteString(fmt.Sprintf("                bidirectional = Bidirectional(inverseName = %s, isSource = %t, inverseMultiplicity = FintMultiplicity.%s),\n",
				strconv.Quote(r.Bidirectional.InverseName), r.Bidirectional.IsSource, inverse.MultiplicityKind))
		}
		b.WriteString("            ),\n")
		lines = append(lines, b.String())
	}
	return lines, nil
}

// The attributes holding a resource rather than a plain value, in declaration
// order. A resource nested in a field carries links of its own, so consumers
// mapping links have to reach it — and only the generator knows the fields,
// which is the point of emitting the walk instead of leaving them to reflect.
func nestedResourceAttributes(t *metamodel.Type, index map[string]typeEntry) []metamodel.Attribute {
	var out []metamodel.Attribute
	for i := range t.Attributes {
		a := &t.Attributes[i]
		target, ok := index[a.Type]
		if !ok || !isResource(target.t) {
			continue
		}
		out = append(out, *a)
	}
	return out
}

func findRelation(t *metamodel.Type, name string) *metamodel.Relation {
	for i := range t.Relations {
		if t.Relations[i].Name == name {
			return &t.Relations[i]
		}
	}
	return nil
}

func writeHeader(b *strings.Builder, pkg string, imports importSet) {
	b.WriteString("package " + pkg + "\n\n")
	for _, imp := range imports.sorted() {
		b.WriteString("import " + imp + "\n")
	}
	if len(imports) > 0 {
		b.WriteString("\n")
	}
}

func superClause(supers []string) string {
	if len(supers) == 0 {
		return ""
	}
	return " : " + strings.Join(supers, ", ")
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

func kotlinElementType(a *metamodel.Attribute, component string, index map[string]typeEntry) (string, string, error) {
	if strings.Contains(a.Type, ":") {
		target, ok := index[a.Type]
		if !ok {
			return "", "", fmt.Errorf("attribute %q: unresolved type %q", a.Name, a.Type)
		}
		imp := ""
		if target.component != component {
			imp = packageFor(target.component) + "." + target.t.Name
		}
		return target.t.Name, imp, nil
	}
	mapped, ok := primitiveTypes[a.Type]
	if !ok {
		return "", "", fmt.Errorf("attribute %q: unknown primitive %q", a.Name, a.Type)
	}
	return mapped, primitiveImports[a.Type], nil
}

func renderRegistry(doc *metamodel.Document) string {
	pkg := config.KOTLIN_PACKAGE_BASE

	var b strings.Builder
	b.WriteString("package " + pkg + "\n\n")
	b.WriteString("import kotlin.reflect.KClass\n\n")
	b.WriteString(`/**
 * Registry over every type in the FINT model.
 *
 * Use [byPath] to find a resource from the three parts of a REST path,
 * follow a relation with [FintRelation.targetMetadata], or list everything
 * the model serves with [paths], [refs] and [served].
 */
object FintModel {

    /** Metadata for every concrete type in the model. */
    val types: List<FintTypeMetadata> = listOf(
`)
	for ci := range doc.Components {
		comp := &doc.Components[ci]
		for ti := range comp.Types {
			t := &comp.Types[ti]
			if t.Stereotype == metamodel.StereotypeAbstract {
				continue
			}
			b.WriteString("        " + packageFor(comp.Name) + "." + t.Name + ".Metadata,\n")
		}
	}
	b.WriteString(`    )

    /** Metadata for every resource: the types that can carry links. */
    val resources: List<FintResourceMetadata> = types.filterIsInstance<FintResourceMetadata>()

    internal val typeIndex: Map<KClass<*>, FintTypeMetadata> = types.associateBy { it.type }

    private val refIndex: Map<String, FintResourceMetadata> =
        resources.mapNotNull { meta ->
            meta.path?.lowercase()?.split('/')?.let { "${it.first()}/${it[1]}/${it.last()}" to meta }
        }.toMap()

    private val commonIndex: Map<String, FintResourceMetadata> =
        resources.filter { it.isCommon }.associateBy { it.name.lowercase() }

    /**
     * Finds the resource served at /[domainName]/[packageName]/[resourceName].
     * Returns null when no such resource exists. Case does not matter.
     *
     * A common resource answers under the domain and package it is served
     * through, so byPath("utdanning", "elev", "person") and
     * byPath("administrasjon", "personal", "person") both find felles:Person.
     * A felles/kodeverk/iso resource answers at its identity, so
     * byPath("felles", "kodeverk", "landkode") finds Landkode even though its
     * path keeps the extra segment.
     */
    fun byPath(domainName: String, packageName: String, resourceName: String): FintResourceMetadata? =
        refIndex["$domainName/$packageName/$resourceName".lowercase()]
            ?: commonIndex[resourceName.lowercase()]

    /** Metadata for [type], or null when it is not a type from the model. */
    fun byType(type: KClass<*>): FintTypeMetadata? = typeIndex[type]

    /**
     * Every REST path the model serves.
     *
     * Holds every resource's own [FintResourceMetadata.path], and adds each
     * common resource under every domain and package it can be reached from:
     * Elev links to Person, so "utdanning/elev/person" is included. The walk
     * also follows relations between common resources until nothing new
     * appears: Person links to Kontaktperson, so "utdanning/elev/kontaktperson"
     * is included even though no elev resource links to Kontaktperson directly.
     *
     * These are the paths as URLs show them, so the felles/kodeverk/iso
     * entries keep their extra segment. [refs] holds the same set as
     * identities.
     */
    val paths: Set<String> by lazy {
        val result = resources
            .flatMap { meta -> listOfNotNull(meta.path) + meta.relations.mapNotNull { meta.relationPath(it.name) } }
            .toMutableSet()
        var grew = true
        while (grew) {
            grew = false
            for (path in result.toList()) {
                val (domainName, packageName, resourceName) = path.split('/').takeIf { it.size == 3 } ?: continue
                val meta = byPath(domainName, packageName, resourceName)?.takeIf { it.isCommon } ?: continue
                for (relation in meta.relations) {
                    meta.relationPath(relation.name, path)?.let { if (result.add(it)) grew = true }
                }
            }
        }
        result
    }

    /**
     * Every place the model serves, as identities: one [FintResourceRef] for
     * each entry in [paths].
     *
     * Identity and path name the same three parts for every resource except
     * the felles/kodeverk/iso three, whose extra path segment the identity
     * drops: "felles/kodeverk/iso/landkode" appears here as
     * ("felles", "kodeverk", "landkode"). Every entry resolves through
     * [byPath], and [refsIn] narrows to one domain and package.
     */
    val refs: Set<FintResourceRef> by lazy { refByPath.values.toSet() }

    /**
     * The identities served under /[domainName]/[packageName], empty when the
     * model serves nothing there. Case does not matter.
     *
     * Common resources show up under every domain and package that can reach
     * them, so refsIn("utdanning", "elev") includes person and kontaktperson,
     * and the felles/kodeverk/iso resources show up collapsed, so
     * refsIn("felles", "kodeverk") includes landkode.
     */
    fun refsIn(domainName: String, packageName: String): Set<FintResourceRef> =
        refs.filter {
            it.domainName.equals(domainName, ignoreCase = true) &&
                it.packageName.equals(packageName, ignoreCase = true)
        }.toSet()

    /**
     * Every place the model serves a resource: one [FintServedResource] for each
     * entry in [paths], in the same order. The counterpart of [paths] and [refs]
     * with the metadata already looked up.
     */
    val served: List<FintServedResource> by lazy {
        refByPath.map { (path, ref) ->
            FintServedResource(
                path = path,
                ref = ref,
                metadata = checkNotNull(byPath(ref.domainName, ref.packageName, ref.resourceName)) {
                    "Served path $path has no resource in the model"
                },
            )
        }
    }

    /**
     * The resources served under /[domainName]/[packageName], in the order they
     * appear in [served], empty when the model serves nothing there. Case does not
     * matter.
     *
     * The same set as [refsIn], with path and metadata attached: resourcesIn("utdanning", "elev")
     * includes felles:Person at "utdanning/elev/person", and resourcesIn("felles", "kodeverk")
     * includes Landkode at "felles/kodeverk/iso/landkode".
     */
    fun resourcesIn(domainName: String, packageName: String): List<FintServedResource> =
        served.filter {
            it.ref.domainName.equals(domainName, ignoreCase = true) &&
                it.ref.packageName.equals(packageName, ignoreCase = true)
        }

    /**
     * The identity of the resource served at [path], or null when the model
     * serves nothing at that path. Case does not matter, and a leading or
     * trailing "/" is ignored.
     *
     * [path] is matched against [paths], so a felles/kodeverk/iso path answers
     * with its extra segment dropped, refOf("felles/kodeverk/iso/landkode") is
     * ("felles", "kodeverk", "landkode"), while the already collapsed
     * "felles/kodeverk/landkode" is not a served path and answers null.
     */
    fun refOf(path: String): FintResourceRef? = refByPath[path.trim('/').lowercase()]

    private val refByPath: Map<String, FintResourceRef> by lazy {
        paths.associateWith { path ->
            path.split('/').let { FintResourceRef(it.first(), it[1], it.last()) }
        }
    }
}

/** Metadata for the type this relation points to, or null for targets outside the model. */
val FintRelation.targetMetadata: FintTypeMetadata?
    get() = FintModel.typeIndex[target]
`)
	return b.String()
}

func runtimeFiles() map[string]string {
	pkg := config.KOTLIN_PACKAGE_BASE
	dir := baseDir()

	return map[string]string{
		dir + "/FintObject.kt": "package " + pkg + `

/**
 * Base type for everything generated from the FINT model.
 */
interface FintObject {

    /** Metadata describing this type. */
    val metadata: FintTypeMetadata
}
`,
		dir + "/FintTypeMetadata.kt": "package " + pkg + `

import kotlin.reflect.KClass

/**
 * Describes one type from the FINT model.
 */
interface FintTypeMetadata {

    /** The Kotlin class this metadata belongs to. */
    val type: KClass<*>

    /** The model reference, e.g. "utdanning-elev:Elev". */
    val ref: String

    /** Every field on the type, including inherited ones. */
    val attributes: List<FintAttribute>
}
`,
		dir + "/FintResourceMetadata.kt": "package " + pkg + `

import kotlin.reflect.KClass

/**
 * Describes a resource: a type with id fields, relations and — unless it is
 * common — a REST path of its own.
 */
interface FintResourceMetadata : FintTypeMetadata {

    /** The Kotlin class this metadata belongs to. */
    override val type: KClass<out FintResource>

    /** The REST path, e.g. "utdanning/elev/elev", or null when the resource has no endpoint of its own. */
    val path: String?

    /** This resource's own segment of a path, e.g. "elev". */
    val name: String

    /**
     * True when the resource is served under the domain and package of whoever
     * links to it. felles:Person is reached at "utdanning/elev/person" from
     * utdanning/elev/elev and at "administrasjon/personal/person" from
     * administrasjon/personal/personalressurs, so it has no [path] of its own —
     * build one with [pathIn] or [relationPath].
     */
    val isCommon: Boolean

    /** The names of the fields that can identify this resource. */
    val idFields: List<String>

    /** Every relation from this resource, including inherited ones. */
    val relations: List<FintRelation>

    /** True when [name] is one of this resource's id fields. Case does not matter. */
    fun isIdField(name: String): Boolean = idFields.any { it.equals(name, ignoreCase = true) }

    /** The relation called [relationName], or null. Case does not matter. */
    fun relation(relationName: String): FintRelation? =
        relations.firstOrNull { it.name.equals(relationName, ignoreCase = true) }

    /**
     * The REST path this resource is served at when it is reached through
     * [contextPath]. A common resource takes the domain and package from
     * [contextPath]; every other resource ignores it and answers [path].
     */
    fun pathIn(contextPath: String): String? =
        if (isCommon) domainAndPackageOf(contextPath)?.let { "$it/$name" } else path

    /**
     * Where this resource is served when it is reached through [context], or
     * null when it has no location of its own to report.
     *
     * The identity counterpart of [pathIn]. A common resource takes the domain
     * and package from [context] and contributes its own [name]; every other
     * resource ignores [context] and answers the domain, package and resource
     * name from its own [path]. The felles/kodeverk/iso resources have one
     * more path segment than that, and the extra segment is not part of the
     * identity: Landkode is served at "felles/kodeverk/iso/landkode" and
     * identified as ("felles", "kodeverk", "landkode"). A resource served
     * inside another one answers null: it has no path and is not common, which
     * is correct because nothing links to it (it arrives nested inside its
     * owner).
     */
    fun refIn(context: FintResourceRef): FintResourceRef? =
        if (isCommon) {
            context.copy(resourceName = name)
        } else {
            path?.split('/')?.takeIf { it.size >= 3 }?.let { FintResourceRef(it.first(), it[1], it.last()) }
        }

    /**
     * The REST path of the resource [relationName] points to, or null when
     * there is no such relation or its target has no path. Common targets are
     * resolved against this resource's own [path]: Elev.Metadata.relationPath("person")
     * is "utdanning/elev/person". Case does not matter.
     */
    fun relationPath(relationName: String): String? = relationPath(relationName, path.orEmpty())

    /**
     * The REST path of the resource [relationName] points to, given that this
     * resource was reached through [contextPath]. Needed when this resource is
     * itself common, and so has no path of its own to resolve the target against.
     */
    fun relationPath(relationName: String, contextPath: String): String? {
        val relation = relation(relationName) ?: return null
        return relation.targetPath ?: (relation.targetMetadata as? FintResourceMetadata)?.pathIn(contextPath)
    }
}

private fun domainAndPackageOf(path: String): String? {
    val segments = path.split('/').filter { it.isNotEmpty() }
    return if (segments.size < 2) null else segments[0] + "/" + segments[1]
}
`,
		dir + "/FintResourceRef.kt": "package " + pkg + `

/**
 * Where a resource is served: the domain, package and resource name the
 * platform routes and stores by, the "utdanning", "elev" and "elev" of
 * "utdanning/elev/elev".
 *
 * Usually these are exactly the three segments of the REST path. The
 * felles/kodeverk/iso resources are the exception: their path has a fourth
 * segment, and the identity keeps the first two segments and the last, so
 * Landkode is served at "felles/kodeverk/iso/landkode" and identified as
 * ("felles", "kodeverk", "landkode").
 *
 * Not the same thing as [FintTypeMetadata.ref], which is the model reference
 * string ("utdanning-elev:Elev") naming a type inside the model. The two share
 * a word and nothing else: this one says where a resource is reached, that one
 * says which type it is.
 *
 * Build one for a relation's target with [FintRelation.targetIn], for a
 * resource reached through a known context with [FintResourceMetadata.refIn],
 * or from a served path with [FintModel.refOf].
 *
 * @property domainName the first segment, "utdanning" in "utdanning/elev/elev"
 * @property packageName the second segment, "elev"
 * @property resourceName the last segment, "elev"
 */
data class FintResourceRef(
    val domainName: String,
    val packageName: String,
    val resourceName: String,
)
`,
		dir + "/FintServedResource.kt": "package " + pkg + `

/**
 * A resource at one of the places the model serves it.
 *
 * [path] is what a URL shows, [ref] is the identity the platform routes and
 * stores by, and [metadata] describes the type served there. The three agree:
 * [FintModel.refOf] of the path is the ref, [FintModel.byPath] of the ref is
 * the metadata, and [FintResourceMetadata.name] is the last segment of the path.
 *
 * A common resource appears once per place it is reached from, so felles:Person
 * appears with path "utdanning/elev/person" and again with
 * "administrasjon/personal/person". The felles/kodeverk/iso resources keep
 * their extra segment in [path] while [ref] drops it, so Landkode appears with
 * path "felles/kodeverk/iso/landkode" and ref ("felles", "kodeverk", "landkode").
 *
 * Get them with [FintModel.served] or [FintModel.resourcesIn].
 */
data class FintServedResource(
    val path: String,
    val ref: FintResourceRef,
    val metadata: FintResourceMetadata,
)
`,
		dir + "/FintAttribute.kt": "package " + pkg + `

import kotlin.reflect.KClass

/**
 * One field on a model type.
 *
 * @property name the field name
 * @property type the Kotlin class of the field's value
 * @property list true when the field holds a list of values
 * @property optional true when the model allows the field to be empty
 */
data class FintAttribute(
    val name: String,
    val type: KClass<*>,
    val list: Boolean,
    val optional: Boolean,
)
`,
		dir + "/FintRelation.kt": "package " + pkg + `

import kotlin.reflect.KClass

/**
 * A relation from one model type to another.
 *
 * @property name the relation name, as used in links
 * @property target the resource on the other end of the relation, held as a class. A relation
 * points at a resource, never at another relation, so [name] is what the relation itself is
 * called and [targetName] is what the resource it points at is called. Those two names differ
 * for a quarter of the model's relations. [targetPath], [targetIdFields] and [targetMetadata]
 * all describe this same target.
 * @property targetPath the REST path of the target, or null when the target has none of its own —
 * a common resource, a resource served inside another one, or a type outside the model. Build the
 * path for those with [FintResourceMetadata.relationPath].
 * @property multiplicity how many links the model expects on this side
 * @property bidirectional set when the relation goes both ways, null when it only goes one way
 */
data class FintRelation(
    val name: String,
    val target: KClass<out FintObject>,
    val targetPath: String?,
    val multiplicity: FintMultiplicity,
    val bidirectional: Bidirectional? = null,
) {
    /** True when the relation goes both ways. */
    val isBidirectional: Boolean get() = bidirectional != null
}

/** The declared id fields of this relation's target, empty when it has none. */
val FintRelation.targetIdFields: List<String>
    get() = (targetMetadata as? FintResourceMetadata)?.idFields.orEmpty()

/**
 * The name of this relation's target resource, or null when the target is not a
 * resource of its own -- Grepreferanse and Vigoreferanse, the same two whose
 * [targetIdFields] is empty.
 *
 * Not the same as [name]. A relation is named for the role it plays and the
 * target for what it is, so the two part company often: a relation named "elev"
 * has Elevforhold for its target, and "gruppemedlemskap" has four different
 * targets depending on which resource declares it. Read links off a payload
 * with [name]; say which resource is on the other end with this.
 */
val FintRelation.targetName: String?
    get() = (targetMetadata as? FintResourceMetadata)?.name

/**
 * Where this relation's target is served, given that the resource declaring the
 * relation is served at [context]. Null when the target is no resource of its
 * own (Grepreferanse and Vigoreferanse, the same two [targetName] is null for),
 * and null when the target has no serving location of its own because it is
 * served inside another resource. A felles/kodeverk/iso target answers its
 * identity, ("felles", "kodeverk", "landkode"), while [targetPath] keeps the
 * full path for links.
 *
 * [context] is what a common target is resolved against. felles:Person has no
 * path: it is served under the domain and package of whoever links to it, so
 * Elev's "person" relation answers "utdanning/elev/person", while the same
 * target reached from an administrasjon resource answers under that domain
 * instead. The typed counterpart of [FintResourceMetadata.relationPath].
 */
fun FintRelation.targetIn(context: FintResourceRef): FintResourceRef? =
    (targetMetadata as? FintResourceMetadata)?.refIn(context)

/**
 * Reads [href] into a [Link]: the id value is the last segment, the id field
 * the one before it, validated against [targetIdFields].
 *
 * Hrefs arrive percent-encoded, so an id value is exactly one segment and the
 * id is positional, as in any other URL. The model is not what finds the id —
 * it is what confirms it. An href whose second-to-last segment names none of
 * the target's id fields is kept verbatim in [Link.unresolved] rather than
 * having an id invented for it, and so is one whose target declares no id
 * fields at all, like "https://data.udir.no/kl06/v201906/fagkoder/FSP01-01" —
 * Grepreferanse and Vigoreferanse have none. Absolute and relative hrefs are
 * read the same way.
 *
 * That makes an unencoded href visible rather than repaired. A sender that
 * still writes ".../fodselsnummer/ABC/DEF" resolves to nothing instead of
 * silently yielding "DEF", so the fault surfaces at the sender. To tell a
 * malformed href from one that was never resolvable, check whether the target
 * declares id fields at all:
 *
 *     if (link.unresolved != null && relation.targetIdFields.isNotEmpty()) …
 *
 * [href] is taken exactly as it arrives and [Link.idValue] is returned still
 * encoded: this splits, it never decodes. The split has to happen while the
 * value is encoded, or a "%2F" inside it would already have become a
 * structural "/". Decoding the value segment afterwards is the caller's, since
 * only the caller knows the wire contract — which codec, whether an ingress
 * already decoded, whether a gateway double-encoded. [Link.href] is the
 * mirror: the caller encodes, this never does.
 */
fun FintRelation.resolveLink(href: String): Link {
    val idFields = targetIdFields
    if (idFields.isEmpty()) return Link(unresolved = href)

    val parts = href.substringAfter("://").split('/')
    if (parts.size < 2) return Link(unresolved = href)

    val idField = parts[parts.size - 2]
    val idValue = parts.last()
    if (idValue.isEmpty()) return Link(unresolved = href)
    if (idFields.none { it.equals(idField, ignoreCase = true) }) return Link(unresolved = href)

    return Link(idField = idField.lowercase(), idValue = idValue)
}
`,
		dir + "/Bidirectional.kt": "package " + pkg + `

/**
 * Extra information for a relation that goes both ways.
 *
 * @property inverseName the relation name seen from the other side
 * @property isSource true when this side owns the relation in the model
 * @property inverseMultiplicity how many links the other side expects
 */
data class Bidirectional(
    val inverseName: String,
    val isSource: Boolean,
    val inverseMultiplicity: FintMultiplicity,
)
`,
		dir + "/FintMultiplicity.kt": "package " + pkg + `

/**
 * How many of something the model expects, given as a range.
 */
enum class FintMultiplicity(val lower: Int, val upper: Int?) {
    EXACTLY_ONE(1, 1),
    ZERO_OR_ONE(0, 1),
    ONE_OR_MORE(1, null),
    ZERO_OR_MORE(0, null);

    /** True when at least one is required. */
    val required: Boolean get() = lower > 0

    /** True when there can be more than one. */
    val many: Boolean get() = upper == null
}
`,
		dir + "/IdentifikatorVisitor.kt": "package " + pkg + `

/**
 * Receives id fields from [FintResource.visitIdentifikators], one at a time.
 */
fun interface IdentifikatorVisitor {

    /** Called with the field name and its value. */
    fun visit(field: String, value: String)
}
`,
		dir + "/FintResourceVisitor.kt": "package " + pkg + `

/**
 * Receives nested resources from [FintResource.visitNested], one at a time.
 */
fun interface FintResourceVisitor {

    /** Called with the field name and the resource held in it. */
    fun visit(field: String, resource: FintResource)
}
`,
		dir + "/FintResource.kt": "package " + pkg + `

/**
 * A resource from the FINT model: a type that can carry links to other
 * resources.
 *
 * Fields are immutable — the [links] map is the only thing that can change.
 * Note that equals, hashCode and copy() ignore links on purpose.
 */
interface FintResource : FintObject {

    /** Links to related resources, grouped by relation name. */
    val links: MutableMap<String, MutableList<Link>>

    /** Metadata for this resource: its path, id fields and relations. */
    override val metadata: FintResourceMetadata

    /** Calls [visitor] once for every id field that has a value. */
    fun visitIdentifikators(visitor: IdentifikatorVisitor)

    /** Returns the id value for [field], or null when it is not set. Case does not matter. */
    fun identifikatorverdi(field: String): String?

    /**
     * The id field and value of this resource whose value is [value], or null
     * when no id field of it holds that value.
     *
     * Values are matched exactly. An id value is opaque here, so S-1 and s-1
     * are different ids, unlike the field names everywhere else in this
     * interface. When more than one id field holds [value] the first in
     * declared order wins, and only this resource is looked at, never the
     * resources nested below it.
     *
     * The field comes back spelled as the model declares it, the same spelling
     * [visitIdentifikators] hands out. Hrefs carry it lowercased, so lowercase
     * it on the way to the wire as [FintRelation.resolveLink] does on the way
     * in.
     */
    fun idFor(value: String): Pair<String, String>? {
        var found: Pair<String, String>? = null
        visitIdentifikators { field, held -> if (found == null && held == value) found = field to held }
        return found
    }

    /**
     * Calls [visitor] once for every resource held in a field of this one —
     * Personalmappe.journalpost, .part, .skjerming and so on — skipping the
     * fields that are not set. Lists are visited element by element under the
     * field's own name.
     *
     * One level deep: the resources handed to [visitor] are not themselves
     * walked, so call [visitNested] again on each to reach the whole tree.
     * Resources with no such fields never call [visitor].
     */
    fun visitNested(visitor: FintResourceVisitor) {}

    /**
     * The resources held in the fields of this one, in field order. Builds a
     * new list per call — use [visitNested] to walk them without one.
     */
    val nestedResources: List<FintResource>
        get() = buildList { visitNested { _, resource -> add(resource) } }

    /** Returns the links stored under [name], or an empty list. */
    fun relationLinks(name: String): List<Link> = links[name].orEmpty()

    /** Adds [link] under [relation]. */
    fun addLink(relation: String, link: Link) {
        links.getOrPut(relation) { mutableListOf() }.add(link)
    }

    /**
     * Removes the self links from this resource and from every resource held
     * below it, however deep. Nothing else in [links] is touched.
     *
     * A self link says what [metadata] and the id fields already say, so
     * storing one duplicates what is stored anyway — once per id field, and
     * again for every node of a nested tree. Strip them on the way into
     * storage and build them again on the way out.
     */
    fun removeSelfLinks() {
        links.remove(SELF)
        visitNested { _, nested -> nested.removeSelfLinks() }
    }

    companion object {

        /**
         * The relation name a resource's own href travels under. Not a model
         * relation — the wire's word for it, and no model type declares a
         * relation by this name.
         */
        const val SELF: String = "self"
    }
}
`,
		dir + "/Link.kt": "package " + pkg + `

/**
 * A link to a resource, stored as the id that points it out instead of the
 * full href.
 *
 * Read an href with [FintRelation.resolveLink]. It takes the relation because
 * an id field is only an id field if the target declares it, and nothing in an
 * href alone can say that.
 *
 * This type holds no codec. Hrefs cross the wire percent-encoded, but which
 * codec produced them, and whether an ingress or gateway already decoded, is
 * knowledge the caller has and this library does not — so [FintRelation.resolveLink]
 * never decodes and [href] never encodes. Everything here is the value exactly
 * as it arrived.
 *
 * @property idField the id field name from the href, lowercased
 * @property idValue the id value from the href, exactly as it arrived — still encoded
 * @property unresolved the original href, kept as-is when it names no id field of the target
 */
data class Link(
    val idField: String? = null,
    val idValue: String? = null,
    val unresolved: String? = null,
) {
    /**
     * The href a reader outside the platform follows: [baseUrl], the target's
     * [path], the id field, and [encodedIdValue] as the final segment.
     *
     * The caller encodes, for the reason [Link] gives. Use a path-segment
     * encoder — Spring's UriUtils.encodePathSegment — and not URLEncoder,
     * which writes application/x-www-form-urlencoded: there a space becomes
     * "+", and in a path "+" is a literal plus that decodes back as a space.
     * Only the id value is encoded; [path] and the id field name are
     * structural.
     *
     * Pass [baseUrl] empty for a root-relative href. Unresolved links are
     * emitted verbatim and ignore [encodedIdValue], so null is the right thing
     * to pass for them — it mirrors [idValue].
     */
    fun href(baseUrl: String, path: String, encodedIdValue: String?): String =
        unresolved ?: baseUrl.trimEnd('/') + "/" + path + "/" + idField + "/" + encodedIdValue.orEmpty()
}
`,
	}
}
