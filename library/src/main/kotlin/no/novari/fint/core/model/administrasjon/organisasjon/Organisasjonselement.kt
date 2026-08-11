package no.novari.fint.core.model.administrasjon.organisasjon

import no.novari.fint.core.model.Bidirectional
import no.novari.fint.core.model.FintAttribute
import no.novari.fint.core.model.FintMultiplicity
import no.novari.fint.core.model.FintRelation
import no.novari.fint.core.model.FintResource
import no.novari.fint.core.model.FintResourceMetadata
import no.novari.fint.core.model.FintResourceVisitor
import no.novari.fint.core.model.IdentifikatorVisitor
import no.novari.fint.core.model.Link
import no.novari.fint.core.model.administrasjon.kodeverk.Ansvar
import no.novari.fint.core.model.administrasjon.kodeverk.Organisasjonstype
import no.novari.fint.core.model.administrasjon.personal.Arbeidsforhold
import no.novari.fint.core.model.administrasjon.personal.Personalressurs
import no.novari.fint.core.model.felles.basisklasser.Enhet
import no.novari.fint.core.model.felles.kompleksedatatyper.Adresse
import no.novari.fint.core.model.felles.kompleksedatatyper.Identifikator
import no.novari.fint.core.model.felles.kompleksedatatyper.Kontaktinformasjon
import no.novari.fint.core.model.felles.kompleksedatatyper.Periode
import no.novari.fint.core.model.utdanning.utdanningsprogram.Skole

data class Organisasjonselement(
    val gyldighetsperiode: Periode? = null,
    val kortnavn: String? = null,
    val navn: String? = null,
    val organisasjonsId: Identifikator? = null,
    val organisasjonsKode: Identifikator? = null,
    override val forretningsadresse: Adresse? = null,
    override val organisasjonsnavn: String? = null,
    override val organisasjonsnummer: Identifikator? = null,
    override val kontaktinformasjon: Kontaktinformasjon? = null,
    override val postadresse: Adresse? = null,
) : Enhet, FintResource {
    override val links: MutableMap<String, MutableList<Link>> = mutableMapOf()

    override val metadata: FintResourceMetadata get() = Metadata

    override fun visitIdentifikators(visitor: IdentifikatorVisitor) {
        organisasjonsnummer?.identifikatorverdi?.let { visitor.visit("organisasjonsnummer", it) }
        organisasjonsId?.identifikatorverdi?.let { visitor.visit("organisasjonsId", it) }
        organisasjonsKode?.identifikatorverdi?.let { visitor.visit("organisasjonsKode", it) }
    }

    override fun identifikatorverdi(field: String): String? = when {
        field.equals("organisasjonsnummer", ignoreCase = true) -> organisasjonsnummer?.identifikatorverdi
        field.equals("organisasjonsId", ignoreCase = true) -> organisasjonsId?.identifikatorverdi
        field.equals("organisasjonsKode", ignoreCase = true) -> organisasjonsKode?.identifikatorverdi
        else -> null
    }

    override fun visitNested(visitor: FintResourceVisitor) {
        forretningsadresse?.let { visitor.visit("forretningsadresse", it) }
        postadresse?.let { visitor.visit("postadresse", it) }
    }

    companion object Metadata : FintResourceMetadata {
        override val type = Organisasjonselement::class
        override val ref = "administrasjon-organisasjon:Organisasjonselement"
        override val path = "administrasjon/organisasjon/organisasjonselement"
        override val name = "organisasjonselement"
        override val isCommon = false
        override val idFields = listOf("organisasjonsnummer", "organisasjonsId", "organisasjonsKode")
        override val attributes = listOf(
            FintAttribute("gyldighetsperiode", Periode::class, list = false, optional = true),
            FintAttribute("kortnavn", String::class, list = false, optional = true),
            FintAttribute("navn", String::class, list = false, optional = true),
            FintAttribute("organisasjonsId", Identifikator::class, list = false, optional = false),
            FintAttribute("organisasjonsKode", Identifikator::class, list = false, optional = false),
            FintAttribute("forretningsadresse", Adresse::class, list = false, optional = true),
            FintAttribute("organisasjonsnavn", String::class, list = false, optional = true),
            FintAttribute("organisasjonsnummer", Identifikator::class, list = false, optional = true),
            FintAttribute("kontaktinformasjon", Kontaktinformasjon::class, list = false, optional = true),
            FintAttribute("postadresse", Adresse::class, list = false, optional = true),
        )
        override val relations = listOf(
            FintRelation(
                name = "ansvar",
                target = Ansvar::class,
                targetPath = "administrasjon/kodeverk/ansvar",
                multiplicity = FintMultiplicity.ZERO_OR_MORE,
                bidirectional = Bidirectional(inverseName = "organisasjonselement", isSource = true, inverseMultiplicity = FintMultiplicity.ZERO_OR_MORE),
            ),
            FintRelation(
                name = "organisasjonstype",
                target = Organisasjonstype::class,
                targetPath = "administrasjon/kodeverk/organisasjonstype",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "leder",
                target = Personalressurs::class,
                targetPath = "administrasjon/personal/personalressurs",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
                bidirectional = Bidirectional(inverseName = "leder", isSource = true, inverseMultiplicity = FintMultiplicity.ZERO_OR_MORE),
            ),
            FintRelation(
                name = "overordnet",
                target = Organisasjonselement::class,
                targetPath = "administrasjon/organisasjon/organisasjonselement",
                multiplicity = FintMultiplicity.EXACTLY_ONE,
                bidirectional = Bidirectional(inverseName = "underordnet", isSource = true, inverseMultiplicity = FintMultiplicity.ZERO_OR_MORE),
            ),
            FintRelation(
                name = "underordnet",
                target = Organisasjonselement::class,
                targetPath = "administrasjon/organisasjon/organisasjonselement",
                multiplicity = FintMultiplicity.ZERO_OR_MORE,
                bidirectional = Bidirectional(inverseName = "overordnet", isSource = true, inverseMultiplicity = FintMultiplicity.EXACTLY_ONE),
            ),
            FintRelation(
                name = "skole",
                target = Skole::class,
                targetPath = "utdanning/utdanningsprogram/skole",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
                bidirectional = Bidirectional(inverseName = "organisasjon", isSource = false, inverseMultiplicity = FintMultiplicity.ZERO_OR_ONE),
            ),
            FintRelation(
                name = "arbeidsforhold",
                target = Arbeidsforhold::class,
                targetPath = "administrasjon/personal/arbeidsforhold",
                multiplicity = FintMultiplicity.ZERO_OR_MORE,
                bidirectional = Bidirectional(inverseName = "arbeidssted", isSource = false, inverseMultiplicity = FintMultiplicity.EXACTLY_ONE),
            ),
        )
    }
}
