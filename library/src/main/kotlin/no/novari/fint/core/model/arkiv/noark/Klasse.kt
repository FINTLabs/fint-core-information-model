package no.novari.fint.core.model.arkiv.noark

import no.novari.fint.core.model.FintAttribute
import no.novari.fint.core.model.FintMultiplicity
import no.novari.fint.core.model.FintRelation
import no.novari.fint.core.model.FintResource
import no.novari.fint.core.model.FintResourceMetadata
import no.novari.fint.core.model.IdentifikatorVisitor
import no.novari.fint.core.model.Link

data class Klasse(
    val klasseId: String? = null,
    val rekkefolge: Int? = null,
    val skjerming: Skjerming? = null,
    val tittel: String? = null,
) : FintResource {
    override val links: MutableMap<String, MutableList<Link>> = mutableMapOf()

    override val metadata: FintResourceMetadata get() = Metadata

    override fun visitIdentifikators(visitor: IdentifikatorVisitor) {}

    override fun identifikatorverdi(field: String): String? = null

    companion object Metadata : FintResourceMetadata {
        override val type = Klasse::class
        override val ref = "arkiv-noark:Klasse"
        override val path: String? = null
        override val name = "klasse"
        override val isCommon = false
        override val idFields = emptyList<String>()
        override val attributes = listOf(
            FintAttribute("klasseId", String::class, list = false, optional = false),
            FintAttribute("rekkefolge", Int::class, list = false, optional = true),
            FintAttribute("skjerming", Skjerming::class, list = false, optional = true),
            FintAttribute("tittel", String::class, list = false, optional = false),
        )
        override val relations = listOf(
            FintRelation(
                name = "klassifikasjonssystem",
                target = Klassifikasjonssystem::class,
                targetPath = "arkiv/noark/klassifikasjonssystem",
                multiplicity = FintMultiplicity.EXACTLY_ONE,
            ),
        )
    }
}
