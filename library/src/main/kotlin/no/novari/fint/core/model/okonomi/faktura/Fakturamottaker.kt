package no.novari.fint.core.model.okonomi.faktura

import no.novari.fint.core.model.FintAttribute
import no.novari.fint.core.model.FintMultiplicity
import no.novari.fint.core.model.FintRelation
import no.novari.fint.core.model.FintResource
import no.novari.fint.core.model.FintResourceMetadata
import no.novari.fint.core.model.IdentifikatorVisitor
import no.novari.fint.core.model.Link
import no.novari.fint.core.model.felles.Person
import no.novari.fint.core.model.felles.kompleksedatatyper.Identifikator

class Fakturamottaker : FintResource {
    override val links: MutableMap<String, MutableList<Link>> = mutableMapOf()

    override val metadata: FintResourceMetadata get() = Metadata

    override fun visitIdentifikators(visitor: IdentifikatorVisitor) {}

    override fun identifikator(field: String): Identifikator? = null

    companion object Metadata : FintResourceMetadata {
        override val type = Fakturamottaker::class
        override val ref = "okonomi-faktura:Fakturamottaker"
        override val path: String? = null
        override val idFields = emptyList<String>()
        override val attributes = emptyList<FintAttribute>()
        override val relations = listOf(
            FintRelation(
                name = "person",
                target = Person::class,
                targetPath = "felles/person",
                multiplicity = FintMultiplicity.EXACTLY_ONE,
            ),
        )
    }
}
