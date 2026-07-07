// Package metamodel defines the canonical, language-neutral JSON representation
// of the FINT domain model. A metamodel.json document is the source of truth
// consumed by language emitters (Java, C#, Go, ...); none of those emitters
// know about EA XMI.
//
// Cross-references between types use the form "component:TypeName" — e.g.
// "felles-kompleksedatatyper:Identifikator". The closed set of bare-string
// primitives is enumerated by Primitives below; anything else in an Attribute
// Type, Relation Target, or Type Parent field is a cross-reference.
//
// Design notes:
//
//   - Each Type's Attributes and Relations are pre-flattened: they contain the
//     type's own entries plus everything inherited from the parent chain, in
//     own-first order. Inherited entries carry Inherited=true, and From names
//     the declaring type, so consumers can recover own-only by filtering.
//
//   - Relation.Bidirectional is a nullable struct rather than a flat
//     bidirectional bool plus nullable isSource/inverseName, so invalid combos
//     (e.g. inverseName set on a unidirectional relation) are unrepresentable.
//     IsSource is meaningful chiefly for many-to-many; for 1-1 / 1-* it's
//     either obvious or arbitrary which side is source.
package metamodel

const SchemaVersion = "1.0"

type Document struct {
	SchemaVersion string      `json:"schemaVersion"`
	FintVersion   string      `json:"fintVersion"`
	GeneratedAt   string      `json:"generatedAt"`
	SourceCommit  string      `json:"sourceCommit,omitempty"`
	Components    []Component `json:"components"`
}

type Component struct {
	Name  string `json:"name"`
	Types []Type `json:"types"`
}

type Type struct {
	Name          string      `json:"name"`
	Stereotype    string      `json:"stereotype"`
	Abstract      bool        `json:"abstract"`
	Deprecated    bool        `json:"deprecated"`
	Parent        *string     `json:"parent"`
	Documentation string      `json:"documentation"`
	Path          *string     `json:"path"`
	IdFields      []string    `json:"idFields"`
	Attributes    []Attribute `json:"attributes"`
	Relations     []Relation  `json:"relations"`
}

type Attribute struct {
	Name       string `json:"name"`
	Type       string `json:"type"`
	List       bool   `json:"list"`
	Optional   bool   `json:"optional"`
	Deprecated bool   `json:"deprecated"`
	Inherited  bool   `json:"inherited"`
	From       string `json:"from"`
}

type Relation struct {
	Name             string         `json:"name"`
	Target           string         `json:"target"`
	Multiplicity     string         `json:"multiplicity"`
	MultiplicityKind string         `json:"multiplicityKind"`
	Bidirectional    *Bidirectional `json:"bidirectional"`
	Deprecated       bool           `json:"deprecated"`
	Inherited        bool           `json:"inherited"`
	From             string         `json:"from"`
}

type Bidirectional struct {
	IsSource    bool   `json:"isSource"`
	InverseName string `json:"inverseName"`
}

const (
	StereotypeMain      = "hovedklasse"
	StereotypeDatatype  = "datatype"
	StereotypeAbstract  = "abstrakt"
	StereotypeReference = "referanse"
)

const (
	MultiplicityOne        = "1"
	MultiplicityZeroOrOne  = "0..1"
	MultiplicityZeroOrMany = "0..*"
	MultiplicityOneOrMany  = "1..*"
)

const (
	KindOneToOne   = "ONE_TO_ONE"
	KindNoneToOne  = "NONE_TO_ONE"
	KindOneToMany  = "ONE_TO_MANY"
	KindNoneToMany = "NONE_TO_MANY"
)

func MultiplicityKind(m string) string {
	switch m {
	case MultiplicityOne:
		return KindOneToOne
	case MultiplicityZeroOrOne:
		return KindNoneToOne
	case MultiplicityOneOrMany:
		return KindOneToMany
	case MultiplicityZeroOrMany:
		return KindNoneToMany
	}
	return ""
}

var Primitives = map[string]struct{}{
	"string":   {},
	"boolean":  {},
	"date":     {},
	"datetime": {},
	"int":      {},
	"long":     {},
	"float":    {},
	"double":   {},
}

func IsPrimitive(t string) bool {
	_, ok := Primitives[t]
	return ok
}
