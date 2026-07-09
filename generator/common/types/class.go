package types

type Class struct {
	Name          string
	Abstract      bool
	Deprecated    bool
	Extends       string
	Package       string
	Imports       []string
	Documentation string
	Attributes    []Attribute
	Relations     []Association
	Stereotype    string
}
