package kotlin

import (
	"fmt"
	"reflect"
	"strings"
	"sync"
	"testing"

	"github.com/FINTLabs/fint-core-information-model/generator/common/metamodel"
)

var (
	goldenOnce  sync.Once
	goldenDoc   *metamodel.Document
	goldenFiles map[string]string
	goldenErr   error
)

func golden(t *testing.T) (*metamodel.Document, map[string]string) {
	t.Helper()
	goldenOnce.Do(func() {
		goldenDoc, goldenErr = metamodel.Load("../../testdata/golden/v4.1.0-rc-2/metamodel.json")
		if goldenErr != nil {
			return
		}
		goldenFiles, goldenErr = Files(goldenDoc)
	})
	if goldenErr != nil {
		t.Fatalf("golden setup: %v", goldenErr)
	}
	return goldenDoc, goldenFiles
}

func TestFiles_CountAndDeterminism(t *testing.T) {
	doc, files := golden(t)

	typeCount := 0
	for _, comp := range doc.Components {
		typeCount += len(comp.Types)
	}
	if want := typeCount + 11; len(files) != want {
		t.Fatalf("expected %d files (types + 10 runtime + registry), got %d", want, len(files))
	}

	second, err := Files(doc)
	if err != nil {
		t.Fatalf("Files second run: %v", err)
	}
	if !reflect.DeepEqual(files, second) {
		t.Fatalf("output is not deterministic across runs")
	}
}

func TestFiles_MetadataMatchesMetamodel(t *testing.T) {
	doc, files := golden(t)

	for _, comp := range doc.Components {
		for _, typ := range comp.Types {
			content, ok := files[filePath(comp.Name, typ.Name)]
			if !ok {
				t.Fatalf("missing file for %s:%s", comp.Name, typ.Name)
			}
			id := comp.Name + ":" + typ.Name

			if typ.Stereotype == metamodel.StereotypeAbstract {
				if strings.Contains(content, "companion object") {
					t.Errorf("%s: abstrakt type should not carry a metadata companion", id)
				}
				if !strings.Contains(content, "interface "+typ.Name) {
					t.Errorf("%s: abstrakt type not emitted as interface", id)
				}
				continue
			}

			resource := typ.Stereotype == metamodel.StereotypeMain || len(typ.Relations) > 0
			metaInterface := "FintTypeMetadata"
			if resource {
				metaInterface = "FintResourceMetadata"
			}
			if !strings.Contains(content, "companion object Metadata : "+metaInterface+" {") {
				t.Errorf("%s: missing companion implementing %s", id, metaInterface)
			}
			if !strings.Contains(content, fmt.Sprintf("override val ref = %q", id)) {
				t.Errorf("%s: missing or wrong ref", id)
			}
			if got, want := strings.Count(content, `FintAttribute("`), len(typ.Attributes); got != want {
				t.Errorf("%s: expected %d FintAttribute entries, found %d", id, want, got)
			}

			hasLinks := strings.Contains(content, "override val links")
			if resource != hasLinks {
				t.Errorf("%s: resource=%t but links override present=%t", id, resource, hasLinks)
			}
			if !resource {
				continue
			}

			if typ.Path != nil {
				if !strings.Contains(content, fmt.Sprintf("override val path = %q", *typ.Path)) {
					t.Errorf("%s: missing path %q", id, *typ.Path)
				}
			} else if !strings.Contains(content, "override val path: String? = null") {
				t.Errorf("%s: missing null path", id)
			}
			if typ.Common && typ.Path != nil {
				t.Errorf("%s: common resource must not name an endpoint of its own", id)
			}
			if !strings.Contains(content, fmt.Sprintf("override val name = %q", strings.ToLower(typ.Name))) {
				t.Errorf("%s: missing or wrong name", id)
			}
			if !strings.Contains(content, fmt.Sprintf("override val isCommon = %t", typ.Common)) {
				t.Errorf("%s: missing or wrong isCommon", id)
			}

			if len(typ.IdFields) > 0 {
				for _, f := range typ.IdFields {
					if !strings.Contains(content, fmt.Sprintf("%s?.identifikatorverdi?.let { visitor.visit(%q, it) }", f, f)) {
						t.Errorf("%s: visitIdentifikators missing field %s", id, f)
					}
				}
			} else if !strings.Contains(content, "override val idFields = emptyList<String>()") {
				t.Errorf("%s: missing empty idFields", id)
			}

			if got, want := strings.Count(content, "FintRelation(\n"), len(typ.Relations); got != want {
				t.Errorf("%s: expected %d FintRelation entries, found %d", id, want, got)
			}
			for _, rel := range typ.Relations {
				if rel.Bidirectional != nil {
					if !strings.Contains(content, fmt.Sprintf("inverseName = %q", rel.Bidirectional.InverseName)) {
						t.Errorf("%s: relation %s missing inverseName %q", id, rel.Name, rel.Bidirectional.InverseName)
					}
				}
			}
		}
	}
}

// A common resource is served under the domain and package of whoever links to
// it, so no relation may bake a path for one — the owner resolves it.
func TestFiles_RelationsToCommonResourcesCarryNoPath(t *testing.T) {
	doc, files := golden(t)

	common := map[string]string{}
	for _, comp := range doc.Components {
		for _, typ := range comp.Types {
			if typ.Common {
				common[comp.Name+":"+typ.Name] = typ.Name
			}
		}
	}
	if len(common) == 0 {
		t.Fatalf("no common types in the golden document")
	}

	checked := 0
	for _, comp := range doc.Components {
		for _, typ := range comp.Types {
			content := files[filePath(comp.Name, typ.Name)]
			for _, rel := range typ.Relations {
				name, ok := common[rel.Target]
				if !ok {
					continue
				}
				checked++
				want := fmt.Sprintf("                target = %s::class,\n                targetPath = null,\n", name)
				if !strings.Contains(content, want) {
					t.Errorf("%s:%s: relation %q bakes a path for common target %s", comp.Name, typ.Name, rel.Name, rel.Target)
				}
			}
		}
	}
	if checked == 0 {
		t.Fatalf("no relations point at a common resource")
	}
}

func TestFiles_RegistryListsEveryConcreteType(t *testing.T) {
	doc, files := golden(t)
	registry, ok := files["no/novari/fint/core/model/FintModel.kt"]
	if !ok {
		t.Fatalf("missing FintModel.kt")
	}
	if !strings.Contains(registry, "object FintModel {") || !strings.Contains(registry, "fun byPath(domainName: String, packageName: String, resourceName: String)") {
		t.Fatalf("registry missing FintModel object")
	}
	for _, comp := range doc.Components {
		for _, typ := range comp.Types {
			entry := packageFor(comp.Name) + "." + typ.Name + ".Metadata,"
			if typ.Stereotype == metamodel.StereotypeAbstract {
				if strings.Contains(registry, entry) {
					t.Errorf("registry must not list abstrakt type %s:%s", comp.Name, typ.Name)
				}
			} else if !strings.Contains(registry, entry) {
				t.Errorf("registry missing %s", entry)
			}
		}
	}
}

func TestFiles_MainClassWithMetadata(t *testing.T) {
	want := `package no.novari.fint.core.model.utdanning.elev

import no.novari.fint.core.model.Bidirectional
import no.novari.fint.core.model.FintAttribute
import no.novari.fint.core.model.FintMultiplicity
import no.novari.fint.core.model.FintRelation
import no.novari.fint.core.model.FintResource
import no.novari.fint.core.model.FintResourceMetadata
import no.novari.fint.core.model.IdentifikatorVisitor
import no.novari.fint.core.model.Link
import no.novari.fint.core.model.felles.Person
import no.novari.fint.core.model.felles.kompleksedatatyper.Adresse
import no.novari.fint.core.model.felles.kompleksedatatyper.Identifikator
import no.novari.fint.core.model.felles.kompleksedatatyper.Kontaktinformasjon

data class Elev(
    val brukernavn: Identifikator? = null,
    val elevnummer: Identifikator? = null,
    val feidenavn: Identifikator? = null,
    val gjest: Boolean? = null,
    val hybeladresse: Adresse? = null,
    val kontaktinformasjon: Kontaktinformasjon? = null,
    val systemId: Identifikator? = null,
) : FintResource {
    override val links: MutableMap<String, MutableList<Link>> = mutableMapOf()

    override val metadata: FintResourceMetadata get() = Metadata

    override fun visitIdentifikators(visitor: IdentifikatorVisitor) {
        brukernavn?.identifikatorverdi?.let { visitor.visit("brukernavn", it) }
        elevnummer?.identifikatorverdi?.let { visitor.visit("elevnummer", it) }
        feidenavn?.identifikatorverdi?.let { visitor.visit("feidenavn", it) }
        systemId?.identifikatorverdi?.let { visitor.visit("systemId", it) }
    }

    override fun identifikatorverdi(field: String): String? = when {
        field.equals("brukernavn", ignoreCase = true) -> brukernavn?.identifikatorverdi
        field.equals("elevnummer", ignoreCase = true) -> elevnummer?.identifikatorverdi
        field.equals("feidenavn", ignoreCase = true) -> feidenavn?.identifikatorverdi
        field.equals("systemId", ignoreCase = true) -> systemId?.identifikatorverdi
        else -> null
    }

    companion object Metadata : FintResourceMetadata {
        override val type = Elev::class
        override val ref = "utdanning-elev:Elev"
        override val path = "utdanning/elev/elev"
        override val name = "elev"
        override val isCommon = false
        override val idFields = listOf("brukernavn", "elevnummer", "feidenavn", "systemId")
        override val attributes = listOf(
            FintAttribute("brukernavn", Identifikator::class, list = false, optional = true),
            FintAttribute("elevnummer", Identifikator::class, list = false, optional = true),
            FintAttribute("feidenavn", Identifikator::class, list = false, optional = true),
            FintAttribute("gjest", Boolean::class, list = false, optional = true),
            FintAttribute("hybeladresse", Adresse::class, list = false, optional = true),
            FintAttribute("kontaktinformasjon", Kontaktinformasjon::class, list = false, optional = true),
            FintAttribute("systemId", Identifikator::class, list = false, optional = false),
        )
        override val relations = listOf(
            FintRelation(
                name = "person",
                target = Person::class,
                targetPath = null,
                multiplicity = FintMultiplicity.EXACTLY_ONE,
                bidirectional = Bidirectional(inverseName = "elev", isSource = true, inverseMultiplicity = FintMultiplicity.ZERO_OR_ONE),
            ),
            FintRelation(
                name = "elevforhold",
                target = Elevforhold::class,
                targetPath = "utdanning/elev/elevforhold",
                multiplicity = FintMultiplicity.ZERO_OR_MORE,
                bidirectional = Bidirectional(inverseName = "elev", isSource = false, inverseMultiplicity = FintMultiplicity.EXACTLY_ONE),
            ),
        )
    }
}
`
	assertFile(t, "no/novari/fint/core/model/utdanning/elev/Elev.kt", want)
}

func TestFiles_AbstractTypeBecomesInterface(t *testing.T) {
	want := `package no.novari.fint.core.model.felles.basisklasser

import no.novari.fint.core.model.FintObject
import no.novari.fint.core.model.felles.kompleksedatatyper.Identifikator
import no.novari.fint.core.model.felles.kompleksedatatyper.Periode

interface Begrep : FintObject {
    val gyldighetsperiode: Periode?
    val kode: String?
    val navn: String?
    val passiv: Boolean?
    val systemId: Identifikator?
}
`
	assertFile(t, "no/novari/fint/core/model/felles/basisklasser/Begrep.kt", want)
}

func TestFiles_DatatypeCarriesTypeMetadata(t *testing.T) {
	want := `package no.novari.fint.core.model.felles.kompleksedatatyper

import no.novari.fint.core.model.FintAttribute
import no.novari.fint.core.model.FintObject
import no.novari.fint.core.model.FintTypeMetadata

data class Identifikator(
    val gyldighetsperiode: Periode? = null,
    val identifikatorverdi: String? = null,
) : FintObject {
    override val metadata: FintTypeMetadata get() = Metadata

    companion object Metadata : FintTypeMetadata {
        override val type = Identifikator::class
        override val ref = "felles-kompleksedatatyper:Identifikator"
        override val attributes = listOf(
            FintAttribute("gyldighetsperiode", Periode::class, list = false, optional = true),
            FintAttribute("identifikatorverdi", String::class, list = false, optional = false),
        )
    }
}
`
	assertFile(t, "no/novari/fint/core/model/felles/kompleksedatatyper/Identifikator.kt", want)
}

func TestFiles_AttributelessReferanse(t *testing.T) {
	want := `package no.novari.fint.core.model.utdanning.kodeverk

import no.novari.fint.core.model.FintAttribute
import no.novari.fint.core.model.FintObject
import no.novari.fint.core.model.FintTypeMetadata

class Grepreferanse : FintObject {
    override val metadata: FintTypeMetadata get() = Metadata

    companion object Metadata : FintTypeMetadata {
        override val type = Grepreferanse::class
        override val ref = "utdanning-kodeverk:Grepreferanse"
        override val attributes = emptyList<FintAttribute>()
    }
}
`
	assertFile(t, "no/novari/fint/core/model/utdanning/kodeverk/Grepreferanse.kt", want)
}

func TestFiles_AttributelessResource(t *testing.T) {
	_, files := golden(t)
	content := files["no/novari/fint/core/model/administrasjon/kompleksedatatyper/Kontostreng.kt"]
	for _, want := range []string{
		"class Kontostreng : FintResource {",
		"override val path: String? = null",
		"override val idFields = emptyList<String>()",
		"override val attributes = emptyList<FintAttribute>()",
		"override fun visitIdentifikators(visitor: IdentifikatorVisitor) {}",
		"override fun identifikatorverdi(field: String): String? = null",
	} {
		if !strings.Contains(content, want) {
			t.Errorf("Kontostreng.kt missing %q", want)
		}
	}
	if got := strings.Count(content, "FintRelation(\n"); got != 13 {
		t.Errorf("Kontostreng.kt expected 13 relations, found %d", got)
	}
}

func TestFiles_RuntimeInterface(t *testing.T) {
	want := `package no.novari.fint.core.model

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

    /** Returns the links stored under [name], or an empty list. */
    fun relationLinks(name: String): List<Link> = links[name].orEmpty()

    /** Adds [link] under [relation]. */
    fun addLink(relation: String, link: Link) {
        links.getOrPut(relation) { mutableListOf() }.add(link)
    }
}
`
	assertFile(t, "no/novari/fint/core/model/FintResource.kt", want)
}

func TestFiles_RuntimeMultiplicity(t *testing.T) {
	want := `package no.novari.fint.core.model

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
`
	assertFile(t, "no/novari/fint/core/model/FintMultiplicity.kt", want)
}

func assertFile(t *testing.T, path, want string) {
	t.Helper()
	_, files := golden(t)
	got, ok := files[path]
	if !ok {
		t.Fatalf("missing file %s", path)
	}
	if got != want {
		t.Fatalf("unexpected content for %s\n--- got ---\n%s\n--- want ---\n%s", path, got, want)
	}
}

func minimalDoc(types ...metamodel.Type) *metamodel.Document {
	return &metamodel.Document{
		Components: []metamodel.Component{
			{Name: "test", Types: types},
		},
	}
}

func TestFiles_FailsOnUnknownPrimitive(t *testing.T) {
	doc := minimalDoc(metamodel.Type{
		Name:       "Broken",
		Stereotype: metamodel.StereotypeDatatype,
		Attributes: []metamodel.Attribute{{Name: "x", Type: "decimal"}},
	})
	if _, err := Files(doc); err == nil || !strings.Contains(err.Error(), "unknown primitive") {
		t.Fatalf("expected unknown primitive error, got %v", err)
	}
}

func TestFiles_FailsOnConcreteParent(t *testing.T) {
	parent := "test:Base"
	doc := minimalDoc(
		metamodel.Type{Name: "Base", Stereotype: metamodel.StereotypeDatatype},
		metamodel.Type{Name: "Child", Stereotype: metamodel.StereotypeDatatype, Parent: &parent},
	)
	if _, err := Files(doc); err == nil || !strings.Contains(err.Error(), "expected abstrakt") {
		t.Fatalf("expected abstrakt-parent error, got %v", err)
	}
}

func TestFiles_FailsOnUnresolvedReference(t *testing.T) {
	doc := minimalDoc(metamodel.Type{
		Name:       "Dangling",
		Stereotype: metamodel.StereotypeDatatype,
		Attributes: []metamodel.Attribute{{Name: "x", Type: "missing:Type"}},
	})
	if _, err := Files(doc); err == nil || !strings.Contains(err.Error(), "unresolved type") {
		t.Fatalf("expected unresolved type error, got %v", err)
	}
}

func TestFiles_FailsOnDuplicateTypeRef(t *testing.T) {
	doc := minimalDoc(
		metamodel.Type{Name: "A", Stereotype: metamodel.StereotypeDatatype},
		metamodel.Type{Name: "A", Stereotype: metamodel.StereotypeDatatype},
	)
	if _, err := Files(doc); err == nil || !strings.Contains(err.Error(), "duplicate type") {
		t.Fatalf("expected duplicate type error, got %v", err)
	}
}

func TestFiles_FailsOnUnresolvedRelationTarget(t *testing.T) {
	doc := minimalDoc(metamodel.Type{
		Name:       "A",
		Stereotype: metamodel.StereotypeMain,
		Relations: []metamodel.Relation{
			{Name: "b", Target: "missing:B", Multiplicity: "1", MultiplicityKind: "EXACTLY_ONE"},
		},
	})
	if _, err := Files(doc); err == nil || !strings.Contains(err.Error(), "unresolved target") {
		t.Fatalf("expected unresolved target error, got %v", err)
	}
}

func TestFiles_FailsOnUnknownMultiplicityKind(t *testing.T) {
	doc := minimalDoc(metamodel.Type{
		Name:       "A",
		Stereotype: metamodel.StereotypeMain,
		Relations: []metamodel.Relation{
			{Name: "b", Target: "test:A", Multiplicity: "1", MultiplicityKind: "ONE_TO_ONE"},
		},
	})
	if _, err := Files(doc); err == nil || !strings.Contains(err.Error(), "unknown multiplicityKind") {
		t.Fatalf("expected unknown multiplicityKind error (old-schema values must be rejected), got %v", err)
	}
}

func TestFiles_FailsOnMissingInverse(t *testing.T) {
	doc := minimalDoc(metamodel.Type{
		Name:       "A",
		Stereotype: metamodel.StereotypeMain,
		Relations: []metamodel.Relation{
			{
				Name:             "b",
				Target:           "test:A",
				Multiplicity:     "1",
				MultiplicityKind: "EXACTLY_ONE",
				Bidirectional:    &metamodel.Bidirectional{IsSource: true, InverseName: "missing"},
			},
		},
	})
	if _, err := Files(doc); err == nil || !strings.Contains(err.Error(), "no inverse relation") {
		t.Fatalf("expected missing inverse error, got %v", err)
	}
}
