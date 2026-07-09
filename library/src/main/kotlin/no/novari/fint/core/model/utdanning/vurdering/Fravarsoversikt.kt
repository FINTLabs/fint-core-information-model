package no.novari.fint.core.model.utdanning.vurdering

import no.novari.fint.core.model.Bidirectional
import no.novari.fint.core.model.FintAttribute
import no.novari.fint.core.model.FintMultiplicity
import no.novari.fint.core.model.FintRelation
import no.novari.fint.core.model.FintResource
import no.novari.fint.core.model.FintResourceMetadata
import no.novari.fint.core.model.IdentifikatorVisitor
import no.novari.fint.core.model.Link
import no.novari.fint.core.model.felles.kompleksedatatyper.Identifikator
import no.novari.fint.core.model.utdanning.elev.Elevforhold
import no.novari.fint.core.model.utdanning.timeplan.Fag

data class Fravarsoversikt(
    var halvar: Fravarsprosent? = null,
    var skolear: Fravarsprosent? = null,
    var systemId: Identifikator? = null,
) : FintResource {
    override val links: MutableMap<String, MutableList<Link>> = mutableMapOf()

    override val metadata: FintResourceMetadata get() = Metadata

    override fun visitIdentifikators(visitor: IdentifikatorVisitor) {
        systemId?.let { visitor.visit("systemId", it) }
    }

    override fun identifikator(field: String): Identifikator? = when {
        field.equals("systemId", ignoreCase = true) -> systemId
        else -> null
    }

    companion object Metadata : FintResourceMetadata {
        override val type = Fravarsoversikt::class
        override val ref = "utdanning-vurdering:Fravarsoversikt"
        override val path = "utdanning/vurdering/fravarsoversikt"
        override val idFields = listOf("systemId")
        override val attributes = listOf(
            FintAttribute("halvar", Fravarsprosent::class, list = false, optional = false),
            FintAttribute("skolear", Fravarsprosent::class, list = false, optional = false),
            FintAttribute("systemId", Identifikator::class, list = false, optional = false),
        )
        override val relations = listOf(
            FintRelation(
                name = "elevforhold",
                target = Elevforhold::class,
                targetPath = "utdanning/elev/elevforhold",
                multiplicity = FintMultiplicity.EXACTLY_ONE,
                bidirectional = Bidirectional(inverseName = "elevfravar", isSource = true, inverseMultiplicity = FintMultiplicity.ZERO_OR_MORE),
            ),
            FintRelation(
                name = "fag",
                target = Fag::class,
                targetPath = "utdanning/timeplan/fag",
                multiplicity = FintMultiplicity.EXACTLY_ONE,
            ),
        )
    }
}
