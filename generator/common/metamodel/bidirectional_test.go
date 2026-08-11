package metamodel

import (
	"fmt"
	"testing"
)

// Every relation that claims to go both ways must be answered from the other
// side: the target has to carry the named inverse, and that inverse has to
// point back at it.
func TestGolden_BidirectionalRelationsAnswerFromBothSides(t *testing.T) {
	doc, err := Load("../../testdata/golden/v4.1.0-rc-2/metamodel.json")
	if err != nil {
		t.Fatalf("load golden: %v", err)
	}

	byRef := map[string]*Type{}
	for ci := range doc.Components {
		comp := &doc.Components[ci]
		for ti := range comp.Types {
			byRef[comp.Name+":"+comp.Types[ti].Name] = &comp.Types[ti]
		}
	}

	pairs := 0
	for ref, typ := range byRef {
		for _, rel := range typ.Relations {
			if rel.Bidirectional == nil {
				continue
			}
			target, ok := byRef[rel.Target]
			if !ok {
				t.Errorf("%s.%s: unresolved target %s", ref, rel.Name, rel.Target)
				continue
			}
			inverse := findRelationNamed(target, rel.Bidirectional.InverseName)
			if inverse == nil {
				t.Errorf("%s.%s: target %s has no relation %q", ref, rel.Name, rel.Target, rel.Bidirectional.InverseName)
				continue
			}
			if inverse.Bidirectional == nil {
				t.Errorf("%s.%s: inverse %s.%s does not go both ways", ref, rel.Name, rel.Target, inverse.Name)
				continue
			}
			if inverse.Bidirectional.InverseName != rel.Name {
				t.Errorf("%s.%s: inverse %s.%s points back at %q", ref, rel.Name, rel.Target, inverse.Name, inverse.Bidirectional.InverseName)
			}
			pairs++
		}
	}
	if pairs == 0 {
		t.Fatalf("no bidirectional relations in the golden document")
	}
}

// Three pairs the EA model named a role at each end of while leaving the
// connector at "Source -> Destination" or "Unspecified", so v4.0.20 reported
// them one-way and consumers walking inverses skipped them. The model fixed all
// three in v4.0.30; this pins that we are on a model where they go both ways.
func TestGolden_PairsRepairedInModelV4_0_30(t *testing.T) {
	doc, err := Load("../../testdata/golden/v4.1.0-rc-2/metamodel.json")
	if err != nil {
		t.Fatalf("load golden: %v", err)
	}

	want := map[string]string{
		"administrasjon-organisasjon:Organisasjonselement.overordnet":  "underordnet",
		"administrasjon-organisasjon:Organisasjonselement.underordnet": "overordnet",
		"okonomi-regnskap:Leverandor.leverandorgruppe":                 "leverandor",
		"okonomi-regnskap:Leverandorgruppe.leverandor":                 "leverandorgruppe",
		"utdanning-elev:Elevforhold.faggruppemedlemskap":               "elevforhold",
		"utdanning-timeplan:Faggruppemedlemskap.elevforhold":           "faggruppemedlemskap",
	}

	for ci := range doc.Components {
		comp := &doc.Components[ci]
		for ti := range comp.Types {
			typ := &comp.Types[ti]
			for _, rel := range typ.Relations {
				key := fmt.Sprintf("%s:%s.%s", comp.Name, typ.Name, rel.Name)
				inverse, tracked := want[key]
				if !tracked {
					continue
				}
				delete(want, key)
				if rel.Bidirectional == nil {
					t.Errorf("%s: expected inverse %q, got a one-way relation", key, inverse)
				} else if rel.Bidirectional.InverseName != inverse {
					t.Errorf("%s: expected inverse %q, got %q", key, inverse, rel.Bidirectional.InverseName)
				}
			}
		}
	}
	for key := range want {
		t.Errorf("%s: relation missing from the model", key)
	}
}

func findRelationNamed(t *Type, name string) *Relation {
	for i := range t.Relations {
		if t.Relations[i].Name == name {
			return &t.Relations[i]
		}
	}
	return nil
}
