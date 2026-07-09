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
import no.novari.fint.core.model.utdanning.timeplan.Fag

data class Sluttfagvurdering(
    override var kommentar: String? = null,
    override var systemId: Identifikator? = null,
    override var vurderingsdato: LocalDateTime? = null,
) : Fagvurdering {
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
        override val type = Sluttfagvurdering::class
        override val ref = "utdanning-vurdering:Sluttfagvurdering"
        override val path = "utdanning/vurdering/sluttfagvurdering"
        override val idFields = listOf("systemId")
        override val attributes = listOf(
            FintAttribute("kommentar", String::class, list = false, optional = false),
            FintAttribute("systemId", Identifikator::class, list = false, optional = false),
            FintAttribute("vurderingsdato", LocalDateTime::class, list = false, optional = false),
        )
        override val relations = listOf(
            FintRelation(
                name = "eksamensgruppe",
                target = Eksamensgruppe::class,
                targetPath = "utdanning/vurdering/eksamensgruppe",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "karakterhistorie",
                target = Karakterhistorie::class,
                targetPath = "utdanning/vurdering/karakterhistorie",
                multiplicity = FintMultiplicity.ZERO_OR_MORE,
            ),
            FintRelation(
                name = "elevvurdering",
                target = Elevvurdering::class,
                targetPath = "utdanning/vurdering/elevvurdering",
                multiplicity = FintMultiplicity.EXACTLY_ONE,
                bidirectional = Bidirectional(inverseName = "sluttfagvurdering", isSource = false, inverseMultiplicity = FintMultiplicity.ZERO_OR_MORE),
            ),
            FintRelation(
                name = "fag",
                target = Fag::class,
                targetPath = "utdanning/timeplan/fag",
                multiplicity = FintMultiplicity.EXACTLY_ONE,
            ),
            FintRelation(
                name = "skolear",
                target = Skolear::class,
                targetPath = "utdanning/kodeverk/skolear",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "karakter",
                target = Karakterverdi::class,
                targetPath = "utdanning/vurdering/karakterverdi",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
        )
    }
}
