package no.novari.fint.core.model.arkiv.noark

import no.novari.fint.core.model.Bidirectional
import no.novari.fint.core.model.FintAttribute
import no.novari.fint.core.model.FintMultiplicity
import no.novari.fint.core.model.FintRelation
import no.novari.fint.core.model.FintResource
import no.novari.fint.core.model.FintResourceMetadata
import no.novari.fint.core.model.IdentifikatorVisitor
import no.novari.fint.core.model.Link
import no.novari.fint.core.model.arkiv.kodeverk.Tilgangsrestriksjon
import no.novari.fint.core.model.felles.kompleksedatatyper.Identifikator

data class Autorisasjon(
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
        override val type = Autorisasjon::class
        override val ref = "arkiv-noark:Autorisasjon"
        override val path = "arkiv/noark/autorisasjon"
        override val idFields = listOf("systemId")
        override val attributes = listOf(
            FintAttribute("systemId", Identifikator::class, list = false, optional = false),
        )
        override val relations = listOf(
            FintRelation(
                name = "tilgangsrestriksjon",
                target = Tilgangsrestriksjon::class,
                targetPath = "arkiv/kodeverk/tilgangsrestriksjon",
                multiplicity = FintMultiplicity.ONE_OR_MORE,
            ),
            FintRelation(
                name = "administrativenhet",
                target = AdministrativEnhet::class,
                targetPath = "arkiv/noark/administrativenhet",
                multiplicity = FintMultiplicity.ZERO_OR_MORE,
            ),
            FintRelation(
                name = "arkivressurs",
                target = Arkivressurs::class,
                targetPath = "arkiv/noark/arkivressurs",
                multiplicity = FintMultiplicity.ZERO_OR_MORE,
                bidirectional = Bidirectional(inverseName = "autorisasjon", isSource = false, inverseMultiplicity = FintMultiplicity.ZERO_OR_MORE),
            ),
        )
    }
}
