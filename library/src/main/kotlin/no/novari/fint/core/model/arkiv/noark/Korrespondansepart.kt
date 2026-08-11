package no.novari.fint.core.model.arkiv.noark

import no.novari.fint.core.model.FintAttribute
import no.novari.fint.core.model.FintMultiplicity
import no.novari.fint.core.model.FintRelation
import no.novari.fint.core.model.FintResource
import no.novari.fint.core.model.FintResourceMetadata
import no.novari.fint.core.model.IdentifikatorVisitor
import no.novari.fint.core.model.Link
import no.novari.fint.core.model.arkiv.kodeverk.KorrespondansepartType
import no.novari.fint.core.model.felles.kompleksedatatyper.Adresse
import no.novari.fint.core.model.felles.kompleksedatatyper.Kontaktinformasjon

data class Korrespondansepart(
    val adresse: Adresse? = null,
    val fodselsnummer: String? = null,
    val kontaktinformasjon: Kontaktinformasjon? = null,
    val kontaktperson: String? = null,
    val korrespondansepartNavn: String? = null,
    val organisasjonsnummer: String? = null,
    val skjerming: Skjerming? = null,
) : FintResource {
    override val links: MutableMap<String, MutableList<Link>> = mutableMapOf()

    override val metadata: FintResourceMetadata get() = Metadata

    override fun visitIdentifikators(visitor: IdentifikatorVisitor) {}

    override fun identifikatorverdi(field: String): String? = null

    companion object Metadata : FintResourceMetadata {
        override val type = Korrespondansepart::class
        override val ref = "arkiv-noark:Korrespondansepart"
        override val path: String? = null
        override val name = "korrespondansepart"
        override val isCommon = false
        override val idFields = emptyList<String>()
        override val attributes = listOf(
            FintAttribute("adresse", Adresse::class, list = false, optional = true),
            FintAttribute("fodselsnummer", String::class, list = false, optional = true),
            FintAttribute("kontaktinformasjon", Kontaktinformasjon::class, list = false, optional = true),
            FintAttribute("kontaktperson", String::class, list = false, optional = true),
            FintAttribute("korrespondansepartNavn", String::class, list = false, optional = true),
            FintAttribute("organisasjonsnummer", String::class, list = false, optional = true),
            FintAttribute("skjerming", Skjerming::class, list = false, optional = true),
        )
        override val relations = listOf(
            FintRelation(
                name = "korrespondanseparttype",
                target = KorrespondansepartType::class,
                targetPath = "arkiv/kodeverk/korrespondanseparttype",
                multiplicity = FintMultiplicity.EXACTLY_ONE,
            ),
        )
    }
}
