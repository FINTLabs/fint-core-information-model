package kotlin

import (
	"reflect"
	"strings"
	"testing"

	"github.com/FINTLabs/fint-model/common/metamodel"
)

func loadGolden(t *testing.T) *metamodel.Document {
	t.Helper()
	doc, err := metamodel.Load("../../testdata/golden/v4.0.20/metamodel.json")
	if err != nil {
		t.Fatalf("load golden metamodel: %v", err)
	}
	return doc
}

func TestFiles_CountAndDeterminism(t *testing.T) {
	doc := loadGolden(t)

	first, err := Files(doc)
	if err != nil {
		t.Fatalf("Files: %v", err)
	}

	typeCount := 0
	for _, comp := range doc.Components {
		typeCount += len(comp.Types)
	}
	if want := typeCount + 3; len(first) != want {
		t.Fatalf("expected %d files (types + 3 runtime), got %d", want, len(first))
	}

	second, err := Files(doc)
	if err != nil {
		t.Fatalf("Files second run: %v", err)
	}
	if !reflect.DeepEqual(first, second) {
		t.Fatalf("output is not deterministic across runs")
	}
}

func TestFiles_EveryConcreteResourceCarriesLinks(t *testing.T) {
	doc := loadGolden(t)
	files, err := Files(doc)
	if err != nil {
		t.Fatalf("Files: %v", err)
	}

	for _, comp := range doc.Components {
		for _, typ := range comp.Types {
			content, ok := files[filePath(comp.Name, typ.Name)]
			if !ok {
				t.Fatalf("missing file for %s:%s", comp.Name, typ.Name)
			}
			abstract := typ.Stereotype == metamodel.StereotypeAbstract
			resource := typ.Stereotype == metamodel.StereotypeMain || len(typ.Relations) > 0
			hasLinks := strings.Contains(content, "override val links")
			if !abstract && resource && !hasLinks {
				t.Errorf("%s:%s is a concrete resource but has no links override", comp.Name, typ.Name)
			}
			if (abstract || !resource) && hasLinks {
				t.Errorf("%s:%s should not implement links", comp.Name, typ.Name)
			}
			if abstract && !strings.Contains(content, "interface "+typ.Name) {
				t.Errorf("%s:%s is abstrakt but not emitted as interface", comp.Name, typ.Name)
			}
		}
	}
}

func TestFiles_MainClassWithComplexAttributes(t *testing.T) {
	want := `package no.novari.fint.kmodel.utdanning.elev

import no.novari.fint.kmodel.FintResource
import no.novari.fint.kmodel.Link
import no.novari.fint.kmodel.felles.kompleksedatatyper.Adresse
import no.novari.fint.kmodel.felles.kompleksedatatyper.Identifikator
import no.novari.fint.kmodel.felles.kompleksedatatyper.Kontaktinformasjon

data class Elev(
    var brukernavn: Identifikator? = null,
    var elevnummer: Identifikator? = null,
    var feidenavn: Identifikator? = null,
    var gjest: Boolean? = null,
    var hybeladresse: Adresse? = null,
    var kontaktinformasjon: Kontaktinformasjon? = null,
    var systemId: Identifikator? = null,
) : FintResource {
    override val links: MutableMap<String, MutableList<Link>> = mutableMapOf()
}
`
	assertFile(t, "no/novari/fint/kmodel/utdanning/elev/Elev.kt", want)
}

func TestFiles_AbstractTypeBecomesInterface(t *testing.T) {
	want := `package no.novari.fint.kmodel.felles.basisklasser

import no.novari.fint.kmodel.FintObject
import no.novari.fint.kmodel.felles.kompleksedatatyper.Identifikator
import no.novari.fint.kmodel.felles.kompleksedatatyper.Periode

interface Begrep : FintObject {
    var gyldighetsperiode: Periode?
    var kode: String?
    var navn: String?
    var passiv: Boolean?
    var systemId: Identifikator?
}
`
	assertFile(t, "no/novari/fint/kmodel/felles/basisklasser/Begrep.kt", want)
}

func TestFiles_InheritedAttributesGetOverride(t *testing.T) {
	want := `package no.novari.fint.kmodel.utdanning.kodeverk

import no.novari.fint.kmodel.FintResource
import no.novari.fint.kmodel.Link
import no.novari.fint.kmodel.felles.basisklasser.Begrep
import no.novari.fint.kmodel.felles.kompleksedatatyper.Identifikator
import no.novari.fint.kmodel.felles.kompleksedatatyper.Periode

data class Fravarstype(
    override var gyldighetsperiode: Periode? = null,
    override var kode: String? = null,
    override var navn: String? = null,
    override var passiv: Boolean? = null,
    override var systemId: Identifikator? = null,
) : Begrep, FintResource {
    override val links: MutableMap<String, MutableList<Link>> = mutableMapOf()
}
`
	assertFile(t, "no/novari/fint/kmodel/utdanning/kodeverk/Fravarstype.kt", want)
}

func TestFiles_AttributelessTypesAreNotDataClasses(t *testing.T) {
	wantPlain := `package no.novari.fint.kmodel.utdanning.kodeverk

import no.novari.fint.kmodel.FintObject

class Grepreferanse : FintObject
`
	assertFile(t, "no/novari/fint/kmodel/utdanning/kodeverk/Grepreferanse.kt", wantPlain)

	wantResource := `package no.novari.fint.kmodel.administrasjon.kompleksedatatyper

import no.novari.fint.kmodel.FintResource
import no.novari.fint.kmodel.Link

class Kontostreng : FintResource {
    override val links: MutableMap<String, MutableList<Link>> = mutableMapOf()
}
`
	assertFile(t, "no/novari/fint/kmodel/administrasjon/kompleksedatatyper/Kontostreng.kt", wantResource)
}

func TestFiles_RuntimeInterface(t *testing.T) {
	want := `package no.novari.fint.kmodel

interface FintResource : FintObject {
    val links: MutableMap<String, MutableList<Link>>
}
`
	assertFile(t, "no/novari/fint/kmodel/FintResource.kt", want)
}

func assertFile(t *testing.T, path, want string) {
	t.Helper()
	files, err := Files(loadGolden(t))
	if err != nil {
		t.Fatalf("Files: %v", err)
	}
	got, ok := files[path]
	if !ok {
		t.Fatalf("missing file %s", path)
	}
	if got != want {
		t.Fatalf("unexpected content for %s\n--- got ---\n%s\n--- want ---\n%s", path, got, want)
	}
}

func TestFiles_FailsOnUnknownPrimitive(t *testing.T) {
	doc := &metamodel.Document{
		Components: []metamodel.Component{
			{
				Name: "test",
				Types: []metamodel.Type{
					{
						Name:       "Broken",
						Stereotype: metamodel.StereotypeDatatype,
						Attributes: []metamodel.Attribute{{Name: "x", Type: "decimal"}},
					},
				},
			},
		},
	}
	if _, err := Files(doc); err == nil || !strings.Contains(err.Error(), "unknown primitive") {
		t.Fatalf("expected unknown primitive error, got %v", err)
	}
}

func TestFiles_FailsOnConcreteParent(t *testing.T) {
	parent := "test:Base"
	doc := &metamodel.Document{
		Components: []metamodel.Component{
			{
				Name: "test",
				Types: []metamodel.Type{
					{Name: "Base", Stereotype: metamodel.StereotypeDatatype},
					{Name: "Child", Stereotype: metamodel.StereotypeDatatype, Parent: &parent},
				},
			},
		},
	}
	if _, err := Files(doc); err == nil || !strings.Contains(err.Error(), "expected abstrakt") {
		t.Fatalf("expected abstrakt-parent error, got %v", err)
	}
}

func TestFiles_FailsOnUnresolvedReference(t *testing.T) {
	doc := &metamodel.Document{
		Components: []metamodel.Component{
			{
				Name: "test",
				Types: []metamodel.Type{
					{
						Name:       "Dangling",
						Stereotype: metamodel.StereotypeDatatype,
						Attributes: []metamodel.Attribute{{Name: "x", Type: "missing:Type"}},
					},
				},
			},
		},
	}
	if _, err := Files(doc); err == nil || !strings.Contains(err.Error(), "unresolved type") {
		t.Fatalf("expected unresolved type error, got %v", err)
	}
}
