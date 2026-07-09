package no.novari.fint.core.model.utdanning.timeplan

import java.time.LocalDateTime
import no.novari.fint.core.model.Bidirectional
import no.novari.fint.core.model.FintAttribute
import no.novari.fint.core.model.FintMultiplicity
import no.novari.fint.core.model.FintRelation
import no.novari.fint.core.model.FintResource
import no.novari.fint.core.model.FintResourceMetadata
import no.novari.fint.core.model.IdentifikatorVisitor
import no.novari.fint.core.model.Link
import no.novari.fint.core.model.felles.kompleksedatatyper.Identifikator
import no.novari.fint.core.model.felles.kompleksedatatyper.Periode
import no.novari.fint.core.model.utdanning.vurdering.Eksamensgruppe

data class Eksamen(
    val beskrivelse: String? = null,
    val navn: String? = null,
    val oppmotetidspunkt: LocalDateTime? = null,
    val systemId: Identifikator? = null,
    val tidsrom: Periode? = null,
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
        override val type = Eksamen::class
        override val ref = "utdanning-timeplan:Eksamen"
        override val path = "utdanning/timeplan/eksamen"
        override val idFields = listOf("systemId")
        override val attributes = listOf(
            FintAttribute("beskrivelse", String::class, list = false, optional = true),
            FintAttribute("navn", String::class, list = false, optional = false),
            FintAttribute("oppmotetidspunkt", LocalDateTime::class, list = false, optional = true),
            FintAttribute("systemId", Identifikator::class, list = false, optional = false),
            FintAttribute("tidsrom", Periode::class, list = false, optional = false),
        )
        override val relations = listOf(
            FintRelation(
                name = "rom",
                target = Rom::class,
                targetPath = "utdanning/timeplan/rom",
                multiplicity = FintMultiplicity.ZERO_OR_MORE,
                bidirectional = Bidirectional(inverseName = "eksamen", isSource = true, inverseMultiplicity = FintMultiplicity.ZERO_OR_MORE),
            ),
            FintRelation(
                name = "eksamensgruppe",
                target = Eksamensgruppe::class,
                targetPath = "utdanning/vurdering/eksamensgruppe",
                multiplicity = FintMultiplicity.ZERO_OR_MORE,
                bidirectional = Bidirectional(inverseName = "eksamen", isSource = false, inverseMultiplicity = FintMultiplicity.ZERO_OR_ONE),
            ),
        )
    }
}
