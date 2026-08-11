package metamodel

import (
	"fmt"
	"strings"
	"time"

	"github.com/FINTLabs/fint-core-information-model/generator/common/types"
)

func Build(classes []*types.Class, fintVersion, sourceCommit string) (*Document, error) {
	prefix := detectPackagePrefix(classes)

	byQualified := make(map[string]*types.Class, len(classes))
	for _, c := range classes {
		byQualified[c.Package+"."+c.Name] = c
	}

	byComponent := make(map[string][]*types.Class)
	componentOrder := make([]string, 0)
	for _, c := range classes {
		name := componentNameFromPackage(c.Package, prefix)
		if _, seen := byComponent[name]; !seen {
			componentOrder = append(componentOrder, name)
		}
		byComponent[name] = append(byComponent[name], c)
	}

	components := make([]Component, 0, len(componentOrder))
	for _, name := range componentOrder {
		converted, err := convertTypes(name, byComponent[name], byQualified, prefix)
		if err != nil {
			return nil, err
		}
		components = append(components, Component{
			Name:  name,
			Types: converted,
		})
	}

	return &Document{
		SchemaVersion: SchemaVersion,
		FintVersion:   fintVersion,
		GeneratedAt:   time.Now().UTC().Format(time.RFC3339),
		SourceCommit:  sourceCommit,
		Components:    components,
	}, nil
}

func detectPackagePrefix(classes []*types.Class) string {
	if len(classes) == 0 {
		return ""
	}
	return commonPrefix(classes, func(c *types.Class) string { return c.Package })
}

func commonPrefix(classes []*types.Class, get func(*types.Class) string) string {
	prefix := strings.Split(get(classes[0]), ".")
	for _, c := range classes[1:] {
		parts := strings.Split(get(c), ".")
		n := len(prefix)
		if len(parts) < n {
			n = len(parts)
		}
		i := 0
		for ; i < n; i++ {
			if prefix[i] != parts[i] {
				break
			}
		}
		prefix = prefix[:i]
	}
	return strings.Join(prefix, ".")
}

func componentNameFromPackage(pkg, prefix string) string {
	rest := strings.TrimPrefix(pkg, prefix+".")
	return strings.ReplaceAll(rest, ".", "-")
}

func componentRefForQualified(qualified, prefix string) string {
	rest := strings.TrimPrefix(qualified, prefix+".")
	lastDot := strings.LastIndex(rest, ".")
	if lastDot < 0 {
		return rest
	}
	pkgRel := rest[:lastDot]
	name := rest[lastDot+1:]
	return strings.ReplaceAll(pkgRel, ".", "-") + ":" + name
}

func convertTypes(componentName string, classes []*types.Class, byQualified map[string]*types.Class, prefix string) ([]Type, error) {
	out := make([]Type, 0, len(classes))
	for _, c := range classes {
		converted, err := convertType(componentName, c, byQualified, prefix)
		if err != nil {
			return nil, err
		}
		out = append(out, converted)
	}
	return out, nil
}

func convertType(componentName string, c *types.Class, byQualified map[string]*types.Class, prefix string) (Type, error) {
	parent, err := resolveParent(c, byQualified, prefix)
	if err != nil {
		return Type{}, err
	}
	t := Type{
		Name:          c.Name,
		Stereotype:    c.Stereotype,
		Abstract:      c.Abstract,
		Deprecated:    c.Deprecated,
		Parent:        parent,
		Documentation: c.Documentation,
	}
	if c.Stereotype == StereotypeMain {
		if isCommonComponent(componentName) {
			t.Common = true
		} else {
			path := restPathFor(componentName, c.Name)
			t.Path = &path
		}
	}
	if ids := collectIdFields(c, byQualified); len(ids) > 0 {
		t.IdFields = ids
	}
	t.Attributes = flatAttributes(c, byQualified, prefix)
	t.Relations = flatRelations(c, byQualified, prefix)
	return t, nil
}

func typeRefFor(c *types.Class, prefix string) string {
	return componentNameFromPackage(c.Package, prefix) + ":" + c.Name
}

func flatAttributes(c *types.Class, byQualified map[string]*types.Class, prefix string) []Attribute {
	out := []Attribute{}
	seen := map[string]struct{}{}
	visited := map[*types.Class]struct{}{}

	var visit func(cur *types.Class, isOwn bool)
	visit = func(cur *types.Class, isOwn bool) {
		if cur == nil {
			return
		}
		if _, dup := visited[cur]; dup {
			return
		}
		visited[cur] = struct{}{}

		from := typeRefFor(cur, prefix)
		for _, a := range cur.Attributes {
			if _, dup := seen[a.Name]; dup {
				continue
			}
			seen[a.Name] = struct{}{}
			out = append(out, Attribute{
				Name:       a.Name,
				Type:       resolveTypeRef(a.Type, cur, byQualified, prefix),
				List:       a.List,
				Optional:   a.Optional,
				Deprecated: a.Deprecated,
				Inherited:  !isOwn,
				From:       from,
			})
		}

		if cur.Extends != "" {
			if qualified, ok := resolveShortName(cur.Extends, cur, byQualified); ok {
				visit(byQualified[qualified], false)
			}
		}
	}
	visit(c, true)
	return out
}

func flatRelations(c *types.Class, byQualified map[string]*types.Class, prefix string) []Relation {
	out := []Relation{}
	seen := map[string]struct{}{}
	visited := map[*types.Class]struct{}{}

	var visit func(cur *types.Class, isOwn bool)
	visit = func(cur *types.Class, isOwn bool) {
		if cur == nil {
			return
		}
		if _, dup := visited[cur]; dup {
			return
		}
		visited[cur] = struct{}{}

		from := typeRefFor(cur, prefix)
		for _, r := range cur.Relations {
			if _, dup := seen[r.Name]; dup {
				continue
			}
			seen[r.Name] = struct{}{}
			var bidi *Bidirectional
			if r.InverseName != "" {
				bidi = &Bidirectional{IsSource: r.IsSource, InverseName: r.InverseName}
			}
			out = append(out, Relation{
				Name:             r.Name,
				Target:           componentRefForQualified(r.Package+"."+r.Target, prefix),
				Multiplicity:     r.Multiplicity,
				MultiplicityKind: MultiplicityKind(r.Multiplicity),
				Bidirectional:    bidi,
				Deprecated:       r.Deprecated,
				Inherited:        !isOwn,
				From:             from,
			})
		}

		if cur.Extends != "" {
			if qualified, ok := resolveShortName(cur.Extends, cur, byQualified); ok {
				visit(byQualified[qualified], false)
			}
		}
	}
	visit(c, true)
	return out
}

func restPathFor(componentName, typeName string) string {
	return strings.ReplaceAll(componentName, "-", "/") + "/" + strings.ToLower(typeName)
}

// A component name is domain-package, so one without a separator is a bare
// domain: it has no package to serve endpoints under, and its types are the
// common ones every domain reaches through its own package.
func isCommonComponent(componentName string) bool {
	return !strings.Contains(componentName, "-")
}

func collectIdFields(c *types.Class, byQualified map[string]*types.Class) []string {
	seen := map[string]struct{}{}
	visited := map[*types.Class]struct{}{}
	out := make([]string, 0, 1)

	var visit func(cur *types.Class)
	visit = func(cur *types.Class) {
		if cur == nil {
			return
		}
		if _, dup := visited[cur]; dup {
			return
		}
		visited[cur] = struct{}{}

		if cur.Extends != "" {
			if qualified, ok := resolveShortName(cur.Extends, cur, byQualified); ok {
				visit(byQualified[qualified])
			}
		}

		for _, a := range cur.Attributes {
			if !strings.EqualFold(a.Type, "identifikator") {
				continue
			}
			if _, dup := seen[a.Name]; dup {
				continue
			}
			seen[a.Name] = struct{}{}
			out = append(out, a.Name)
		}
	}
	visit(c)
	return out
}

func resolveParent(c *types.Class, byQualified map[string]*types.Class, prefix string) (*string, error) {
	if c.Extends == "" {
		return nil, nil
	}
	qualified, ok := resolveShortName(c.Extends, c, byQualified)
	if !ok {
		return nil, fmt.Errorf("%s.%s: unresolved parent %q", c.Package, c.Name, c.Extends)
	}
	ref := componentRefForQualified(qualified, prefix)
	return &ref, nil
}

func resolveShortName(name string, c *types.Class, byQualified map[string]*types.Class) (string, bool) {
	if candidate := c.Package + "." + name; byQualified[candidate] != nil {
		return candidate, true
	}
	for _, imp := range c.Imports {
		if strings.HasSuffix(imp, "."+name) {
			if byQualified[imp] != nil {
				return imp, true
			}
		}
	}
	return "", false
}

func resolveTypeRef(typeName string, c *types.Class, byQualified map[string]*types.Class, prefix string) string {
	lower := strings.ToLower(typeName)
	if IsPrimitive(lower) {
		return lower
	}
	if qualified, ok := resolveShortName(typeName, c, byQualified); ok {
		return componentRefForQualified(qualified, prefix)
	}
	return typeName
}
