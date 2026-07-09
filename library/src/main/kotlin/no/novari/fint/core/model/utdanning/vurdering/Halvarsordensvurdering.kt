package no.novari.fint.core.model.utdanning.vurdering

import java.time.LocalDateTime
import no.novari.fint.core.model.Bidirectional
import no.novari.fint.core.model.FintAttribute
import no.novari.fint.core.model.FintMultiplicity
import no.novari.fint.core.model.FintRelation
import no.novari.fint.core.model.FintResourceMetadata
import no.novari.fint.core.model.IdentifikatorVisitor
import no.novari.fint.core.model.Link
import no.novari.fint.core.model.felles.kompleksedatatyper.Identifikator
import no.novari.fint.core.model.utdanning.kodeverk.Skolear

data class Halvarsordensvurdering(
    override var kommentar: String? = null,
    override var systemId: Identifikator? = null,
    override var vurderingsdato: LocalDateTime? = null,
) : Ordensvurdering {
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
        override val type = Halvarsordensvurdering::class
        override val ref = "utdanning-vurdering:Halvarsordensvurdering"
        override val path = "utdanning/vurdering/halvarsordensvurdering"
        override val idFields = listOf("systemId")
        override val attributes = listOf(
            FintAttribute("kommentar", String::class, list = false, optional = false),
            FintAttribute("systemId", Identifikator::class, list = false, optional = false),
            FintAttribute("vurderingsdato", LocalDateTime::class, list = false, optional = false),
        )
        override val relations = listOf(
            FintRelation(
                name = "elevvurdering",
                target = Elevvurdering::class,
                targetPath = "utdanning/vurdering/elevvurdering",
                multiplicity = FintMultiplicity.EXACTLY_ONE,
                bidirectional = Bidirectional(inverseName = "halvarsordensvurdering", isSource = false, inverseMultiplicity = FintMultiplicity.ZERO_OR_MORE),
            ),
            FintRelation(
                name = "atferd",
                target = Karakterverdi::class,
                targetPath = "utdanning/vurdering/karakterverdi",
                multiplicity = FintMultiplicity.EXACTLY_ONE,
            ),
            FintRelation(
                name = "orden",
                target = Karakterverdi::class,
                targetPath = "utdanning/vurdering/karakterverdi",
                multiplicity = FintMultiplicity.EXACTLY_ONE,
            ),
            FintRelation(
                name = "skolear",
                target = Skolear::class,
                targetPath = "utdanning/kodeverk/skolear",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
        )
    }
}
