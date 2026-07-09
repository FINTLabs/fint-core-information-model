package no.novari.fint.core.model.arkiv.noark

import no.novari.fint.core.model.FintAttribute
import no.novari.fint.core.model.FintMultiplicity
import no.novari.fint.core.model.FintRelation
import no.novari.fint.core.model.FintResource
import no.novari.fint.core.model.FintResourceMetadata
import no.novari.fint.core.model.IdentifikatorVisitor
import no.novari.fint.core.model.Link
import no.novari.fint.core.model.arkiv.kodeverk.Skjermingshjemmel
import no.novari.fint.core.model.arkiv.kodeverk.Tilgangsrestriksjon
import no.novari.fint.core.model.felles.kompleksedatatyper.Identifikator

class Skjerming : FintResource {
    override val links: MutableMap<String, MutableList<Link>> = mutableMapOf()

    override val metadata: FintResourceMetadata get() = Metadata

    override fun visitIdentifikators(visitor: IdentifikatorVisitor) {}

    override fun identifikator(field: String): Identifikator? = null

    companion object Metadata : FintResourceMetadata {
        override val type = Skjerming::class
        override val ref = "arkiv-noark:Skjerming"
        override val path: String? = null
        override val idFields = emptyList<String>()
        override val attributes = emptyList<FintAttribute>()
        override val relations = listOf(
            FintRelation(
                name = "skjermingshjemmel",
                target = Skjermingshjemmel::class,
                targetPath = "arkiv/kodeverk/skjermingshjemmel",
                multiplicity = FintMultiplicity.EXACTLY_ONE,
            ),
            FintRelation(
                name = "tilgangsrestriksjon",
                target = Tilgangsrestriksjon::class,
                targetPath = "arkiv/kodeverk/tilgangsrestriksjon",
                multiplicity = FintMultiplicity.EXACTLY_ONE,
            ),
        )
    }
}
