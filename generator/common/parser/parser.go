package parser

import (
	"fmt"
	"strconv"
	"strings"

	"github.com/FINTLabs/fint-core-information-model/generator/common/config"
	"github.com/FINTLabs/fint-core-information-model/generator/common/document"
	"github.com/FINTLabs/fint-core-information-model/generator/common/types"
	"github.com/FINTLabs/fint-core-information-model/generator/common/utils"
	xmlquery "github.com/antchfx/xquery/xml"
)

type importCandidate struct {
	Package string
	Import  string
}

func qualifiedKey(pkg string, name string) string {
	return fmt.Sprintf("%s.%s", pkg, name)
}

func buildImportNameMap(imports map[string]string) map[string][]importCandidate {
	result := make(map[string][]importCandidate)
	for key, imp := range imports {
		lastDot := strings.LastIndex(key, ".")
		if lastDot < 0 {
			continue
		}
		name := key[lastDot+1:]
		pkg := key[:lastDot]
		result[name] = append(result[name], importCandidate{Package: pkg, Import: imp})
	}
	return result
}

func pickPackage(packageContext string, candidatePackage string, currentBest string) (string, bool) {
	if candidatePackage != packageContext && !strings.HasPrefix(candidatePackage, packageContext+".") {
		return currentBest, false
	}
	if currentBest == "" || len(candidatePackage) < len(currentBest) {
		return candidatePackage, false
	}
	if len(candidatePackage) == len(currentBest) {
		return currentBest, true
	}
	return currentBest, false
}

func resolveClassCandidate(packageContext string, candidates []*types.Class) (*types.Class, bool) {
	var best *types.Class
	bestPackage := ""
	tie := false
	for _, c := range candidates {
		var t bool
		bestPackage, t = pickPackage(packageContext, c.Package, bestPackage)
		tie = tie || t
		if bestPackage == c.Package {
			best = c
		}
	}
	return best, best != nil && !tie
}

func resolveImportCandidate(packageContext string, candidates []importCandidate) (string, bool) {
	var best string
	bestPackage := ""
	tie := false
	for _, c := range candidates {
		var t bool
		bestPackage, t = pickPackage(packageContext, c.Package, bestPackage)
		tie = tie || t
		if bestPackage == c.Package {
			best = c.Import
		}
	}
	return best, len(bestPackage) > 0 && !tie
}

func GetClasses(owner string, repo string, tag string, filename string, force bool) []*types.Class {
	return ClassesFromDocument(document.Get(owner, repo, tag, filename, force))
}

func ClassesFromDocument(doc *xmlquery.Node) []*types.Class {
	var classes []*types.Class
	packageMap := make(map[string]string)
	classMap := make(map[string]*types.Class)
	classNameMap := make(map[string][]*types.Class)

	classElements := xmlquery.Find(doc, "//element[@type='Class']")
	for _, classElement := range classElements {

		properties := classElement.SelectElement("properties")
		class := new(types.Class)

		class.Name = replaceNO(classElement.SelectAttr("name"))
		class.Abstract = toBool(properties.SelectAttr("isAbstract"))
		class.Extends = getExtends(doc, classElement)
		class.Attributes = getAttributes(classElement)
		class.Relations = getAssociations(doc, classElement)
		class.Package = getPackagePath(classElement, doc)
		class.Stereotype = properties.SelectAttr("stereotype")
		class.Documentation = properties.SelectAttr("documentation")
		class.Deprecated = classElement.SelectElement("tags/tag[@name='DEPRECATED']") != nil

		if len(class.Stereotype) == 0 {
			if class.Abstract {
				class.Stereotype = "abstrakt"
			}
		}

		key := qualifiedKey(class.Package, class.Name)
		packageMap[key] = key

		classes = append(classes, class)
		classMap[key] = class
		classNameMap[class.Name] = append(classNameMap[class.Name], class)
	}

	for name, list := range classNameMap {
		if len(list) == 1 {
			packageMap[name] = qualifiedKey(list[0].Package, list[0].Name)
			classMap[name] = list[0]
		}
	}

	importNameMap := buildImportNameMap(packageMap)

	for _, class := range classes {
		class.Imports = getImports(class, packageMap, importNameMap)
		if len(class.Stereotype) == 0 {
			if chainIdentifiable(class, classMap, classNameMap) {
				class.Stereotype = "hovedklasse"
			} else {
				class.Stereotype = "datatype"
			}
		}
	}

	return classes
}

func findClass(className string, packageContext string, classMap map[string]*types.Class, classNameMap map[string][]*types.Class) (*types.Class, bool) {
	if class, found := classMap[className]; found {
		return class, true
	}
	if class, found := classMap[qualifiedKey(packageContext, className)]; found {
		return class, true
	}
	candidates := classNameMap[className]
	if len(candidates) == 1 {
		return candidates[0], true
	}
	return resolveClassCandidate(packageContext, candidates)
}

func chainIdentifiable(class *types.Class, classMap map[string]*types.Class, classNameMap map[string][]*types.Class) bool {
	if identifiable(class.Attributes) {
		return true
	}
	if len(class.Extends) > 0 {
		if extendedClass, found := findClass(class.Extends, class.Package, classMap, classNameMap); found {
			return chainIdentifiable(extendedClass, classMap, classNameMap)
		}
	}
	return false
}

func identifiable(attribs []types.Attribute) bool {

	for _, value := range attribs {
		if value.Type == "Identifikator" {
			return true
		}
	}

	return false

}

func findImport(typeName string, packageContext string, imports map[string]string, importNameMap map[string][]importCandidate) (string, bool) {
	if imp, found := imports[typeName]; found {
		return imp, true
	}
	if imp, found := imports[qualifiedKey(packageContext, typeName)]; found {
		return imp, true
	}
	return resolveImportCandidate(packageContext, importNameMap[typeName])
}

func getImports(c *types.Class, imports map[string]string, importNameMap map[string][]importCandidate) []string {

	attribs := c.Attributes
	self := qualifiedKey(c.Package, c.Name)
	var imps []string
	for _, att := range attribs {
		if len(att.Type) > 0 {
			imp, found := findImport(att.Type, c.Package, imports, importNameMap)
			if found && len(imp) > 0 && imp != self {
				imps = append(imps, imp)
			}
		}
	}

	if len(c.Extends) > 0 {
		imp, found := findImport(c.Extends, c.Package, imports, importNameMap)
		if found && len(imp) > 0 && imp != self {
			imps = append(imps, imp)
		}
	}

	return utils.Distinct(utils.TrimArray(imps))
}

func getPackagePath(c *xmlquery.Node, doc *xmlquery.Node) string {

	var pkgs []string
	var parentPkg string
	classPkgId := getPackage(c)
	pkgs = append(pkgs, getNameLower(classPkgId, doc))

	parentPkg = getParentPackage(classPkgId, doc)
	for parentPkg != "" {
		pkgs = append(pkgs, getNameLower(parentPkg, doc))
		parentPkg = getParentPackage(parentPkg, doc)
	}
	pkgs = utils.TrimArray(pkgs)
	pkgs = utils.Reverse(pkgs)
	return replaceNO(fmt.Sprintf("%s.%s", config.PACKAGE_BASE, strings.Join(pkgs, ".")))

}

func getName(idref string, doc *xmlquery.Node) string {
	name := ""
	if len(idref) > 0 {
		xpath := fmt.Sprintf("//element[@idref='%s']", idref)
		parent := xmlquery.Find(doc, xpath)

		name = parent[0].SelectAttr("name")
		name = excludeName(name)
	}
	return strings.Replace(name, " ", "", -1)
}

func excludeName(name string) string {
	if name == "FINT" {
		name = strings.Replace(name, "FINT", "", -1)
	}
	if name == "Model" {
		name = strings.Replace(name, "Model", "", -1)
	}
	return name
}

func getNameLower(idref string, doc *xmlquery.Node) string {

	return strings.ToLower(getName(idref, doc))
}

func getParentPackage(idref string, doc *xmlquery.Node) string {
	xpath := fmt.Sprintf("//element[@idref='%s']", idref)

	parent := xmlquery.Find(doc, xpath)

	if len(parent) > 1 {
		fmt.Printf("More than one element with idref %s\n", idref)
		return ""
	}
	if len(parent) < 1 {
		return ""
	}

	model := parent[0].SelectElement("model")
	if model == nil {
		return ""
	}

	return model.SelectAttr("package")
}

func getPackage(c *xmlquery.Node) string {
	return c.SelectElement("model").SelectAttr("package")
}

func getExtends(doc *xmlquery.Node, c *xmlquery.Node) string {

	var extends []string
	for _, rr := range xmlquery.Find(doc, fmt.Sprintf("//connectors/connector/properties[@ea_type='Generalization']/../source[@idref='%s']/../target/model[@name]", c.SelectAttr("idref"))) {
		if len(rr.SelectAttr("name")) > 0 {
			extends = append(extends, replaceNO(rr.SelectAttr("name")))
		}
	}

	if len(extends) == 1 {
		return extends[0]
	}

	return ""
}

func getAttributes(c *xmlquery.Node) []types.Attribute {
	var attributes []types.Attribute
	for _, a := range xmlquery.Find(c, "//attributes/attribute") {

		attrib := types.Attribute{}
		attrib.Name = replaceNO(a.SelectAttr("name"))
		attrib.Deprecated = a.SelectElement("tags/tag[@name='DEPRECATED']") != nil
		attrib.List = strings.Compare(a.SelectElement("bounds").SelectAttr("upper"), "*") == 0
		attrib.Optional = strings.Compare(a.SelectElement("bounds").SelectAttr("lower"), "0") == 0
		attrib.Type = replaceNO(a.SelectElement("properties").SelectAttr("type"))

		attributes = append(attributes, attrib)
	}

	return attributes
}

func buildAssociationQueries(idref string) []types.AssociationQuery {
	return []types.AssociationQuery{
		{
			XPath: fmt.Sprintf("//connectors/connector/properties[@ea_type='Association']/../source[@idref='%s']/../target/role", idref),
			Role:  types.RoleSource,
		},
		{
			XPath: fmt.Sprintf("//connectors/connector/properties[@ea_type='Association']/../target[@idref='%s']/../source/role", idref),
			Role:  types.RoleTarget,
		},
	}
}

func getAssociations(doc *xmlquery.Node, c *xmlquery.Node) []types.Association {
	var assocs []types.Association

	classId := c.SelectAttr("idref")
	isParent := isExtendedByOthers(doc, classId)
	queries := buildAssociationQueries(c.SelectAttr("idref"))

	for _, q := range queries {
		for _, relationElement := range xmlquery.Find(doc, q.XPath) {
			if len(relationElement.SelectAttr("name")) == 0 {
				continue
			}

			connector := relationElement.Parent.Parent
			isSource := isStructuralSource(connector, classId)

			assoc := buildAssociation(doc, relationElement, q.Role, isParent, isSource)
			assocs = append(assocs, assoc)
		}
	}
	return assocs
}

func isStructuralSource(connector *xmlquery.Node, classId string) bool {
	structuralSourceID := connector.SelectElement("source").SelectAttr("idref")
	return structuralSourceID == classId
}

func buildAssociation(doc *xmlquery.Node, rel *xmlquery.Node, role types.AssociationRole, isParent bool, isSource bool) types.Association {
	targetId := rel.Parent.SelectAttr("idref")
	targetClassElement := findClassElementByID(doc, targetId)

	return types.Association{
		Name:         replaceNO(rel.SelectAttr("name")),
		Target:       replaceNO(rel.SelectElement("../model").SelectAttr("name")),
		Multiplicity: rel.SelectElement("../type").SelectAttr("multiplicity"),
		Package:      getPackagePath(targetClassElement, doc),
		Deprecated:   rel.SelectElement("../../tags/tag[@name='DEPRECATED']") != nil,
		InverseName:  getAssociationInverseName(rel, role, isParent),
		IsSource:     isSource,
	}
}

func getAssociationInverseName(rel *xmlquery.Node, role types.AssociationRole, isParent bool) string {
	direction := rel.SelectElement("../../properties").SelectAttr("direction")

	if direction != "Bi-Directional" || isParent {
		return ""
	}

	var sourceNode *xmlquery.Node
	if role == types.RoleSource {
		sourceNode = rel.SelectElement("../../source/role")
	} else {
		sourceNode = rel.SelectElement("../../target/role")
	}

	if sourceNode != nil {
		return replaceNO(sourceNode.SelectAttr("name"))
	}

	return ""
}

func isExtendedByOthers(doc *xmlquery.Node, classId string) bool {
	xpath := fmt.Sprintf("//connectors/connector/properties[@ea_type='Generalization']/../target[@idref='%s']", classId)
	return xmlquery.FindOne(doc, xpath) != nil
}

func findClassElementByID(doc *xmlquery.Node, id string) *xmlquery.Node {
	query := fmt.Sprintf("//element[@type='Class'][@idref='%s']", id)
	return xmlquery.FindOne(doc, query)
}

func replaceNO(s string) string {
	r := strings.Replace(s, "æ", "a", -1)
	r = strings.Replace(r, "ø", "o", -1)
	r = strings.Replace(r, "å", "a", -1)
	r = strings.Replace(r, "Æ", "A", -1)
	r = strings.Replace(r, "Ø", "O", -1)
	r = strings.Replace(r, "Å", "A", -1)
	return r
}

func toBool(s string) bool {
	b, err := strconv.ParseBool(s)

	if err != nil {
		return false
	}

	return b
}
