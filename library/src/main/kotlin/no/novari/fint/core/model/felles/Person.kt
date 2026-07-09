package no.novari.fint.core.model.felles

import java.time.LocalDate
import no.novari.fint.core.model.Bidirectional
import no.novari.fint.core.model.FintAttribute
import no.novari.fint.core.model.FintMultiplicity
import no.novari.fint.core.model.FintRelation
import no.novari.fint.core.model.FintResource
import no.novari.fint.core.model.FintResourceMetadata
import no.novari.fint.core.model.IdentifikatorVisitor
import no.novari.fint.core.model.Link
import no.novari.fint.core.model.administrasjon.personal.Personalressurs
import no.novari.fint.core.model.felles.basisklasser.Aktor
import no.novari.fint.core.model.felles.kodeverk.Kommune
import no.novari.fint.core.model.felles.kodeverk.iso.Kjonn
import no.novari.fint.core.model.felles.kodeverk.iso.Landkode
import no.novari.fint.core.model.felles.kodeverk.iso.Sprak
import no.novari.fint.core.model.felles.kompleksedatatyper.Adresse
import no.novari.fint.core.model.felles.kompleksedatatyper.Identifikator
import no.novari.fint.core.model.felles.kompleksedatatyper.Kontaktinformasjon
import no.novari.fint.core.model.felles.kompleksedatatyper.Personnavn
import no.novari.fint.core.model.utdanning.elev.Elev
import no.novari.fint.core.model.utdanning.larling.Larling
import no.novari.fint.core.model.utdanning.ot.OtUngdom

data class Person(
    var bilde: String? = null,
    var bostedsadresse: Adresse? = null,
    var fodselsdato: LocalDate? = null,
    var fodselsnummer: Identifikator? = null,
    var navn: Personnavn? = null,
    override var kontaktinformasjon: Kontaktinformasjon? = null,
    override var postadresse: Adresse? = null,
) : Aktor, FintResource {
    override val links: MutableMap<String, MutableList<Link>> = mutableMapOf()

    override val metadata: FintResourceMetadata get() = Metadata

    override fun visitIdentifikators(visitor: IdentifikatorVisitor) {
        fodselsnummer?.let { visitor.visit("fodselsnummer", it) }
    }

    override fun identifikator(field: String): Identifikator? = when {
        field.equals("fodselsnummer", ignoreCase = true) -> fodselsnummer
        else -> null
    }

    companion object Metadata : FintResourceMetadata {
        override val type = Person::class
        override val ref = "felles:Person"
        override val path = "felles/person"
        override val idFields = listOf("fodselsnummer")
        override val attributes = listOf(
            FintAttribute("bilde", String::class, list = false, optional = true),
            FintAttribute("bostedsadresse", Adresse::class, list = false, optional = true),
            FintAttribute("fodselsdato", LocalDate::class, list = false, optional = true),
            FintAttribute("fodselsnummer", Identifikator::class, list = false, optional = false),
            FintAttribute("navn", Personnavn::class, list = false, optional = false),
            FintAttribute("kontaktinformasjon", Kontaktinformasjon::class, list = false, optional = true),
            FintAttribute("postadresse", Adresse::class, list = false, optional = true),
        )
        override val relations = listOf(
            FintRelation(
                name = "statsborgerskap",
                target = Landkode::class,
                targetPath = "felles/kodeverk/iso/landkode",
                multiplicity = FintMultiplicity.ZERO_OR_MORE,
            ),
            FintRelation(
                name = "kommune",
                target = Kommune::class,
                targetPath = "felles/kodeverk/kommune",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "kjonn",
                target = Kjonn::class,
                targetPath = "felles/kodeverk/iso/kjonn",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "foreldreansvar",
                target = Person::class,
                targetPath = "felles/person",
                multiplicity = FintMultiplicity.ZERO_OR_MORE,
                bidirectional = Bidirectional(inverseName = "foreldre", isSource = true, inverseMultiplicity = FintMultiplicity.ZERO_OR_MORE),
            ),
            FintRelation(
                name = "malform",
                target = Sprak::class,
                targetPath = "felles/kodeverk/iso/sprak",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "personalressurs",
                target = Personalressurs::class,
                targetPath = "administrasjon/personal/personalressurs",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
                bidirectional = Bidirectional(inverseName = "person", isSource = true, inverseMultiplicity = FintMultiplicity.EXACTLY_ONE),
            ),
            FintRelation(
                name = "morsmal",
                target = Sprak::class,
                targetPath = "felles/kodeverk/iso/sprak",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "parorende",
                target = Kontaktperson::class,
                targetPath = "felles/kontaktperson",
                multiplicity = FintMultiplicity.ZERO_OR_MORE,
                bidirectional = Bidirectional(inverseName = "kontaktperson", isSource = false, inverseMultiplicity = FintMultiplicity.ZERO_OR_MORE),
            ),
            FintRelation(
                name = "foreldre",
                target = Person::class,
                targetPath = "felles/person",
                multiplicity = FintMultiplicity.ZERO_OR_MORE,
                bidirectional = Bidirectional(inverseName = "foreldreansvar", isSource = true, inverseMultiplicity = FintMultiplicity.ZERO_OR_MORE),
            ),
            FintRelation(
                name = "larling",
                target = Larling::class,
                targetPath = "utdanning/larling/larling",
                multiplicity = FintMultiplicity.ZERO_OR_MORE,
                bidirectional = Bidirectional(inverseName = "person", isSource = false, inverseMultiplicity = FintMultiplicity.EXACTLY_ONE),
            ),
            FintRelation(
                name = "elev",
                target = Elev::class,
                targetPath = "utdanning/elev/elev",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
                bidirectional = Bidirectional(inverseName = "person", isSource = false, inverseMultiplicity = FintMultiplicity.EXACTLY_ONE),
            ),
            FintRelation(
                name = "otungdom",
                target = OtUngdom::class,
                targetPath = "utdanning/ot/otungdom",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
                bidirectional = Bidirectional(inverseName = "person", isSource = false, inverseMultiplicity = FintMultiplicity.EXACTLY_ONE),
            ),
        )
    }
}
