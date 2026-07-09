package no.novari.fint.core.model.arkiv.noark

import no.novari.fint.core.model.FintAttribute
import no.novari.fint.core.model.FintMultiplicity
import no.novari.fint.core.model.FintRelation
import no.novari.fint.core.model.FintResource
import no.novari.fint.core.model.FintResourceMetadata
import no.novari.fint.core.model.IdentifikatorVisitor
import no.novari.fint.core.model.Link
import no.novari.fint.core.model.arkiv.kodeverk.Format
import no.novari.fint.core.model.arkiv.kodeverk.Variantformat

data class Dokumentobjekt(
    var filstorrelse: String? = null,
    var formatDetaljer: String? = null,
    var sjekksum: String? = null,
    var sjekksumAlgoritme: String? = null,
    var versjonsnummer: Long? = null,
) : FintResource {
    override val links: MutableMap<String, MutableList<Link>> = mutableMapOf()

    override val metadata: FintResourceMetadata get() = Metadata

    override fun visitIdentifikators(visitor: IdentifikatorVisitor) {}

    override fun identifikatorverdi(field: String): String? = null

    companion object Metadata : FintResourceMetadata {
        override val type = Dokumentobjekt::class
        override val ref = "arkiv-noark:Dokumentobjekt"
        override val path: String? = null
        override val idFields = emptyList<String>()
        override val attributes = listOf(
            FintAttribute("filstorrelse", String::class, list = false, optional = true),
            FintAttribute("formatDetaljer", String::class, list = false, optional = true),
            FintAttribute("sjekksum", String::class, list = false, optional = true),
            FintAttribute("sjekksumAlgoritme", String::class, list = false, optional = true),
            FintAttribute("versjonsnummer", Long::class, list = false, optional = true),
        )
        override val relations = listOf(
            FintRelation(
                name = "filformat",
                target = Format::class,
                targetPath = "arkiv/kodeverk/format",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "variantFormat",
                target = Variantformat::class,
                targetPath = "arkiv/kodeverk/variantformat",
                multiplicity = FintMultiplicity.EXACTLY_ONE,
            ),
            FintRelation(
                name = "opprettetAv",
                target = Arkivressurs::class,
                targetPath = "arkiv/noark/arkivressurs",
                multiplicity = FintMultiplicity.EXACTLY_ONE,
            ),
            FintRelation(
                name = "referanseDokumentfil",
                target = Dokumentfil::class,
                targetPath = "arkiv/noark/dokumentfil",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
        )
    }
}
