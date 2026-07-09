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
