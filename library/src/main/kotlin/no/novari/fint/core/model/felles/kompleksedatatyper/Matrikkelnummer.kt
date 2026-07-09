package no.novari.fint.core.model.felles.kompleksedatatyper

import no.novari.fint.core.model.FintAttribute
import no.novari.fint.core.model.FintMultiplicity
import no.novari.fint.core.model.FintRelation
import no.novari.fint.core.model.FintResource
import no.novari.fint.core.model.FintResourceMetadata
import no.novari.fint.core.model.IdentifikatorVisitor
import no.novari.fint.core.model.Link
import no.novari.fint.core.model.felles.kodeverk.Kommune

data class Matrikkelnummer(
    var adresse: Adresse? = null,
    var bruksnummer: String? = null,
    var festenummer: String? = null,
    var gardsnummer: String? = null,
    var seksjonsnummer: String? = null,
) : FintResource {
    override val links: MutableMap<String, MutableList<Link>> = mutableMapOf()

    override val metadata: FintResourceMetadata get() = Metadata

    override fun visitIdentifikators(visitor: IdentifikatorVisitor) {}

    override fun identifikatorverdi(field: String): String? = null

    companion object Metadata : FintResourceMetadata {
        override val type = Matrikkelnummer::class
        override val ref = "felles-kompleksedatatyper:Matrikkelnummer"
        override val path: String? = null
        override val idFields = emptyList<String>()
        override val attributes = listOf(
            FintAttribute("adresse", Adresse::class, list = false, optional = true),
            FintAttribute("bruksnummer", String::class, list = false, optional = true),
            FintAttribute("festenummer", String::class, list = false, optional = true),
            FintAttribute("gardsnummer", String::class, list = false, optional = true),
            FintAttribute("seksjonsnummer", String::class, list = false, optional = true),
        )
        override val relations = listOf(
            FintRelation(
                name = "kommunenummer",
                target = Kommune::class,
                targetPath = "felles/kodeverk/kommune",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
        )
    }
}
