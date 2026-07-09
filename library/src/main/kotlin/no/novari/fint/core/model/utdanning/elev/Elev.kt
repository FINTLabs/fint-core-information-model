package no.novari.fint.core.model.utdanning.elev

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
    var brukernavn: Identifikator? = null,
    var elevnummer: Identifikator? = null,
    var feidenavn: Identifikator? = null,
    var gjest: Boolean? = null,
    var hybeladresse: Adresse? = null,
    var kontaktinformasjon: Kontaktinformasjon? = null,
    var systemId: Identifikator? = null,
) : FintResource {
    override val links: MutableMap<String, MutableList<Link>> = mutableMapOf()

    override val metadata: FintResourceMetadata get() = Metadata

    override fun visitIdentifikators(visitor: IdentifikatorVisitor) {
        brukernavn?.let { visitor.visit("brukernavn", it) }
        elevnummer?.let { visitor.visit("elevnummer", it) }
        feidenavn?.let { visitor.visit("feidenavn", it) }
        systemId?.let { visitor.visit("systemId", it) }
    }

    override fun identifikator(field: String): Identifikator? = when {
        field.equals("brukernavn", ignoreCase = true) -> brukernavn
        field.equals("elevnummer", ignoreCase = true) -> elevnummer
        field.equals("feidenavn", ignoreCase = true) -> feidenavn
        field.equals("systemId", ignoreCase = true) -> systemId
        else -> null
    }

    companion object Metadata : FintResourceMetadata {
        override val type = Elev::class
        override val ref = "utdanning-elev:Elev"
        override val path = "utdanning/elev/elev"
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
                targetPath = "felles/person",
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
