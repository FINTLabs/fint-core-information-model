package no.novari.fint.core.model.administrasjon.organisasjon

import no.novari.fint.core.model.Bidirectional
import no.novari.fint.core.model.FintAttribute
import no.novari.fint.core.model.FintMultiplicity
import no.novari.fint.core.model.FintRelation
import no.novari.fint.core.model.FintResource
import no.novari.fint.core.model.FintResourceMetadata
import no.novari.fint.core.model.IdentifikatorVisitor
import no.novari.fint.core.model.Link
import no.novari.fint.core.model.administrasjon.personal.Arbeidsforhold
import no.novari.fint.core.model.felles.basisklasser.Enhet
import no.novari.fint.core.model.felles.kompleksedatatyper.Adresse
import no.novari.fint.core.model.felles.kompleksedatatyper.Identifikator
import no.novari.fint.core.model.felles.kompleksedatatyper.Kontaktinformasjon

data class Arbeidslokasjon(
    var lokasjonskode: Identifikator? = null,
    var lokasjonsnavn: String? = null,
    override var forretningsadresse: Adresse? = null,
    override var organisasjonsnavn: String? = null,
    override var organisasjonsnummer: Identifikator? = null,
    override var kontaktinformasjon: Kontaktinformasjon? = null,
    override var postadresse: Adresse? = null,
) : Enhet, FintResource {
    override val links: MutableMap<String, MutableList<Link>> = mutableMapOf()

    override val metadata: FintResourceMetadata get() = Metadata

    override fun visitIdentifikators(visitor: IdentifikatorVisitor) {
        organisasjonsnummer?.identifikatorverdi?.let { visitor.visit("organisasjonsnummer", it) }
        lokasjonskode?.identifikatorverdi?.let { visitor.visit("lokasjonskode", it) }
    }

    override fun identifikatorverdi(field: String): String? = when {
        field.equals("organisasjonsnummer", ignoreCase = true) -> organisasjonsnummer?.identifikatorverdi
        field.equals("lokasjonskode", ignoreCase = true) -> lokasjonskode?.identifikatorverdi
        else -> null
    }

    companion object Metadata : FintResourceMetadata {
        override val type = Arbeidslokasjon::class
        override val ref = "administrasjon-organisasjon:Arbeidslokasjon"
        override val path = "administrasjon/organisasjon/arbeidslokasjon"
        override val idFields = listOf("organisasjonsnummer", "lokasjonskode")
        override val attributes = listOf(
            FintAttribute("lokasjonskode", Identifikator::class, list = false, optional = false),
            FintAttribute("lokasjonsnavn", String::class, list = false, optional = true),
            FintAttribute("forretningsadresse", Adresse::class, list = false, optional = true),
            FintAttribute("organisasjonsnavn", String::class, list = false, optional = true),
            FintAttribute("organisasjonsnummer", Identifikator::class, list = false, optional = true),
            FintAttribute("kontaktinformasjon", Kontaktinformasjon::class, list = false, optional = true),
            FintAttribute("postadresse", Adresse::class, list = false, optional = true),
        )
        override val relations = listOf(
            FintRelation(
                name = "arbeidsforhold",
                target = Arbeidsforhold::class,
                targetPath = "administrasjon/personal/arbeidsforhold",
                multiplicity = FintMultiplicity.ZERO_OR_MORE,
                bidirectional = Bidirectional(inverseName = "arbeidslokasjon", isSource = false, inverseMultiplicity = FintMultiplicity.ZERO_OR_ONE),
            ),
        )
    }
}
