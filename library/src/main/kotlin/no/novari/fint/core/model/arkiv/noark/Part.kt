package no.novari.fint.core.model.arkiv.noark

import no.novari.fint.core.model.FintAttribute
import no.novari.fint.core.model.FintMultiplicity
import no.novari.fint.core.model.FintRelation
import no.novari.fint.core.model.FintResource
import no.novari.fint.core.model.FintResourceMetadata
import no.novari.fint.core.model.IdentifikatorVisitor
import no.novari.fint.core.model.Link
import no.novari.fint.core.model.arkiv.kodeverk.PartRolle
import no.novari.fint.core.model.felles.kompleksedatatyper.Adresse
import no.novari.fint.core.model.felles.kompleksedatatyper.Identifikator
import no.novari.fint.core.model.felles.kompleksedatatyper.Kontaktinformasjon

data class Part(
    var adresse: Adresse? = null,
    var fodselsnummer: String? = null,
    var kontaktinformasjon: Kontaktinformasjon? = null,
    var kontaktperson: String? = null,
    var organisasjonsnummer: String? = null,
    var partNavn: String? = null,
) : FintResource {
    override val links: MutableMap<String, MutableList<Link>> = mutableMapOf()

    override val metadata: FintResourceMetadata get() = Metadata

    override fun visitIdentifikators(visitor: IdentifikatorVisitor) {}

    override fun identifikator(field: String): Identifikator? = null

    companion object Metadata : FintResourceMetadata {
        override val type = Part::class
        override val ref = "arkiv-noark:Part"
        override val path: String? = null
        override val idFields = emptyList<String>()
        override val attributes = listOf(
            FintAttribute("adresse", Adresse::class, list = false, optional = true),
            FintAttribute("fodselsnummer", String::class, list = false, optional = true),
            FintAttribute("kontaktinformasjon", Kontaktinformasjon::class, list = false, optional = true),
            FintAttribute("kontaktperson", String::class, list = false, optional = true),
            FintAttribute("organisasjonsnummer", String::class, list = false, optional = true),
            FintAttribute("partNavn", String::class, list = false, optional = false),
        )
        override val relations = listOf(
            FintRelation(
                name = "partRolle",
                target = PartRolle::class,
                targetPath = "arkiv/kodeverk/partrolle",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
        )
    }
}
