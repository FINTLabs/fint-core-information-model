package no.novari.fint.core.model.okonomi.faktura

import no.novari.fint.core.model.FintAttribute
import no.novari.fint.core.model.FintMultiplicity
import no.novari.fint.core.model.FintRelation
import no.novari.fint.core.model.FintResource
import no.novari.fint.core.model.FintResourceMetadata
import no.novari.fint.core.model.IdentifikatorVisitor
import no.novari.fint.core.model.Link
import no.novari.fint.core.model.okonomi.kodeverk.Vare

data class Fakturalinje(
    var antall: Float? = null,
    var fritekst: List<String>? = null,
    var pris: Long? = null,
) : FintResource {
    override val links: MutableMap<String, MutableList<Link>> = mutableMapOf()

    override val metadata: FintResourceMetadata get() = Metadata

    override fun visitIdentifikators(visitor: IdentifikatorVisitor) {}

    override fun identifikatorverdi(field: String): String? = null

    companion object Metadata : FintResourceMetadata {
        override val type = Fakturalinje::class
        override val ref = "okonomi-faktura:Fakturalinje"
        override val path: String? = null
        override val idFields = emptyList<String>()
        override val attributes = listOf(
            FintAttribute("antall", Float::class, list = false, optional = false),
            FintAttribute("fritekst", String::class, list = true, optional = true),
            FintAttribute("pris", Long::class, list = false, optional = false),
        )
        override val relations = listOf(
            FintRelation(
                name = "vare",
                target = Vare::class,
                targetPath = "okonomi/kodeverk/vare",
                multiplicity = FintMultiplicity.EXACTLY_ONE,
            ),
        )
    }
}
