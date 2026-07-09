package no.novari.fint.core.model.felles.kompleksedatatyper

import no.novari.fint.core.model.FintAttribute
import no.novari.fint.core.model.FintMultiplicity
import no.novari.fint.core.model.FintRelation
import no.novari.fint.core.model.FintResource
import no.novari.fint.core.model.FintResourceMetadata
import no.novari.fint.core.model.IdentifikatorVisitor
import no.novari.fint.core.model.Link
import no.novari.fint.core.model.felles.kodeverk.iso.Landkode

data class Adresse(
    var adresselinje: List<String>? = null,
    var postnummer: String? = null,
    var poststed: String? = null,
) : FintResource {
    override val links: MutableMap<String, MutableList<Link>> = mutableMapOf()

    override val metadata: FintResourceMetadata get() = Metadata

    override fun visitIdentifikators(visitor: IdentifikatorVisitor) {}

    override fun identifikatorverdi(field: String): String? = null

    companion object Metadata : FintResourceMetadata {
        override val type = Adresse::class
        override val ref = "felles-kompleksedatatyper:Adresse"
        override val path: String? = null
        override val idFields = emptyList<String>()
        override val attributes = listOf(
            FintAttribute("adresselinje", String::class, list = true, optional = true),
            FintAttribute("postnummer", String::class, list = false, optional = true),
            FintAttribute("poststed", String::class, list = false, optional = true),
        )
        override val relations = listOf(
            FintRelation(
                name = "land",
                target = Landkode::class,
                targetPath = "felles/kodeverk/iso/landkode",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
        )
    }
}
