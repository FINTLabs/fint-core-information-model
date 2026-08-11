package no.novari.fint.core.model.utdanning.elev

import java.time.LocalDate
import no.novari.fint.core.model.Bidirectional
import no.novari.fint.core.model.FintAttribute
import no.novari.fint.core.model.FintMultiplicity
import no.novari.fint.core.model.FintRelation
import no.novari.fint.core.model.FintResource
import no.novari.fint.core.model.FintResourceMetadata
import no.novari.fint.core.model.IdentifikatorVisitor
import no.novari.fint.core.model.Link
import no.novari.fint.core.model.felles.kompleksedatatyper.Identifikator
import no.novari.fint.core.model.utdanning.kodeverk.Varseltype
import no.novari.fint.core.model.utdanning.timeplan.Faggruppemedlemskap

data class Varsel(
    val fravarsprosent: Long? = null,
    val sendt: LocalDate? = null,
    val systemId: Identifikator? = null,
    val tekst: String? = null,
) : FintResource {
    override val links: MutableMap<String, MutableList<Link>> = mutableMapOf()

    override val metadata: FintResourceMetadata get() = Metadata

    override fun visitIdentifikators(visitor: IdentifikatorVisitor) {
        systemId?.identifikatorverdi?.let { visitor.visit("systemId", it) }
    }

    override fun identifikatorverdi(field: String): String? = when {
        field.equals("systemId", ignoreCase = true) -> systemId?.identifikatorverdi
        else -> null
    }

    companion object Metadata : FintResourceMetadata {
        override val type = Varsel::class
        override val ref = "utdanning-elev:Varsel"
        override val path = "utdanning/elev/varsel"
        override val name = "varsel"
        override val isCommon = false
        override val idFields = listOf("systemId")
        override val attributes = listOf(
            FintAttribute("fravarsprosent", Long::class, list = false, optional = false),
            FintAttribute("sendt", LocalDate::class, list = false, optional = false),
            FintAttribute("systemId", Identifikator::class, list = false, optional = false),
            FintAttribute("tekst", String::class, list = false, optional = false),
        )
        override val relations = listOf(
            FintRelation(
                name = "utsteder",
                target = Skoleressurs::class,
                targetPath = "utdanning/elev/skoleressurs",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "karakteransvarlig",
                target = Undervisningsforhold::class,
                targetPath = "utdanning/elev/undervisningsforhold",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "type",
                target = Varseltype::class,
                targetPath = "utdanning/kodeverk/varseltype",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "faggruppemedlemskap",
                target = Faggruppemedlemskap::class,
                targetPath = "utdanning/timeplan/faggruppemedlemskap",
                multiplicity = FintMultiplicity.EXACTLY_ONE,
                bidirectional = Bidirectional(inverseName = "varsel", isSource = true, inverseMultiplicity = FintMultiplicity.ZERO_OR_MORE),
            ),
        )
    }
}
