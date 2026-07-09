package no.novari.fint.core.model.felles

import no.novari.fint.core.model.Bidirectional
import no.novari.fint.core.model.FintAttribute
import no.novari.fint.core.model.FintMultiplicity
import no.novari.fint.core.model.FintRelation
import no.novari.fint.core.model.FintResource
import no.novari.fint.core.model.FintResourceMetadata
import no.novari.fint.core.model.IdentifikatorVisitor
import no.novari.fint.core.model.Link
import no.novari.fint.core.model.felles.basisklasser.Enhet
import no.novari.fint.core.model.felles.kompleksedatatyper.Adresse
import no.novari.fint.core.model.felles.kompleksedatatyper.Identifikator
import no.novari.fint.core.model.felles.kompleksedatatyper.Kontaktinformasjon
import no.novari.fint.core.model.utdanning.larling.Larling

data class Virksomhet(
    var virksomhetsId: Identifikator? = null,
    override var forretningsadresse: Adresse? = null,
    override var organisasjonsnavn: String? = null,
    override var organisasjonsnummer: Identifikator? = null,
    override var kontaktinformasjon: Kontaktinformasjon? = null,
    override var postadresse: Adresse? = null,
) : Enhet, FintResource {
    override val links: MutableMap<String, MutableList<Link>> = mutableMapOf()

    override val metadata: FintResourceMetadata get() = Metadata

    override fun visitIdentifikators(visitor: IdentifikatorVisitor) {
        visitor.visit("organisasjonsnummer", organisasjonsnummer)
        visitor.visit("virksomhetsId", virksomhetsId)
    }

    override fun identifikator(field: String): Identifikator? = when {
        field.equals("organisasjonsnummer", ignoreCase = true) -> organisasjonsnummer
        field.equals("virksomhetsId", ignoreCase = true) -> virksomhetsId
        else -> null
    }

    companion object Metadata : FintResourceMetadata {
        override val type = Virksomhet::class
        override val ref = "felles:Virksomhet"
        override val path = "felles/virksomhet"
        override val idFields = listOf("organisasjonsnummer", "virksomhetsId")
        override val attributes = listOf(
            FintAttribute("virksomhetsId", Identifikator::class, list = false, optional = false),
            FintAttribute("forretningsadresse", Adresse::class, list = false, optional = true),
            FintAttribute("organisasjonsnavn", String::class, list = false, optional = true),
            FintAttribute("organisasjonsnummer", Identifikator::class, list = false, optional = true),
            FintAttribute("kontaktinformasjon", Kontaktinformasjon::class, list = false, optional = true),
            FintAttribute("postadresse", Adresse::class, list = false, optional = true),
        )
        override val relations = listOf(
            FintRelation(
                name = "larling",
                target = Larling::class,
                targetPath = "utdanning/larling/larling",
                multiplicity = FintMultiplicity.ZERO_OR_MORE,
                bidirectional = Bidirectional(inverseName = "bedrift", isSource = false, inverseMultiplicity = FintMultiplicity.ZERO_OR_ONE),
            ),
        )
    }
}
