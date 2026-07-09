package no.novari.fint.core.model.administrasjon.personal

import java.time.LocalDate
import no.novari.fint.core.model.Bidirectional
import no.novari.fint.core.model.FintAttribute
import no.novari.fint.core.model.FintMultiplicity
import no.novari.fint.core.model.FintRelation
import no.novari.fint.core.model.FintResource
import no.novari.fint.core.model.FintResourceMetadata
import no.novari.fint.core.model.IdentifikatorVisitor
import no.novari.fint.core.model.Link
import no.novari.fint.core.model.administrasjon.fullmakt.Fullmakt
import no.novari.fint.core.model.administrasjon.kodeverk.Personalressurskategori
import no.novari.fint.core.model.administrasjon.organisasjon.Organisasjonselement
import no.novari.fint.core.model.felles.Person
import no.novari.fint.core.model.felles.kompleksedatatyper.Identifikator
import no.novari.fint.core.model.felles.kompleksedatatyper.Kontaktinformasjon
import no.novari.fint.core.model.felles.kompleksedatatyper.Periode
import no.novari.fint.core.model.utdanning.elev.Skoleressurs

data class Personalressurs(
    var ansattnummer: Identifikator? = null,
    var ansettelsesperiode: Periode? = null,
    var ansiennitet: LocalDate? = null,
    var brukernavn: Identifikator? = null,
    var jobbtittel: String? = null,
    var kontaktinformasjon: Kontaktinformasjon? = null,
    var systemId: Identifikator? = null,
) : FintResource {
    override val links: MutableMap<String, MutableList<Link>> = mutableMapOf()

    override val metadata: FintResourceMetadata get() = Metadata

    override fun visitIdentifikators(visitor: IdentifikatorVisitor) {
        ansattnummer?.identifikatorverdi?.let { visitor.visit("ansattnummer", it) }
        brukernavn?.identifikatorverdi?.let { visitor.visit("brukernavn", it) }
        systemId?.identifikatorverdi?.let { visitor.visit("systemId", it) }
    }

    override fun identifikatorverdi(field: String): String? = when {
        field.equals("ansattnummer", ignoreCase = true) -> ansattnummer?.identifikatorverdi
        field.equals("brukernavn", ignoreCase = true) -> brukernavn?.identifikatorverdi
        field.equals("systemId", ignoreCase = true) -> systemId?.identifikatorverdi
        else -> null
    }

    companion object Metadata : FintResourceMetadata {
        override val type = Personalressurs::class
        override val ref = "administrasjon-personal:Personalressurs"
        override val path = "administrasjon/personal/personalressurs"
        override val idFields = listOf("ansattnummer", "brukernavn", "systemId")
        override val attributes = listOf(
            FintAttribute("ansattnummer", Identifikator::class, list = false, optional = false),
            FintAttribute("ansettelsesperiode", Periode::class, list = false, optional = false),
            FintAttribute("ansiennitet", LocalDate::class, list = false, optional = true),
            FintAttribute("brukernavn", Identifikator::class, list = false, optional = true),
            FintAttribute("jobbtittel", String::class, list = false, optional = true),
            FintAttribute("kontaktinformasjon", Kontaktinformasjon::class, list = false, optional = true),
            FintAttribute("systemId", Identifikator::class, list = false, optional = true),
        )
        override val relations = listOf(
            FintRelation(
                name = "personalressurskategori",
                target = Personalressurskategori::class,
                targetPath = "administrasjon/kodeverk/personalressurskategori",
                multiplicity = FintMultiplicity.EXACTLY_ONE,
            ),
            FintRelation(
                name = "arbeidsforhold",
                target = Arbeidsforhold::class,
                targetPath = "administrasjon/personal/arbeidsforhold",
                multiplicity = FintMultiplicity.ZERO_OR_MORE,
                bidirectional = Bidirectional(inverseName = "personalressurs", isSource = true, inverseMultiplicity = FintMultiplicity.EXACTLY_ONE),
            ),
            FintRelation(
                name = "person",
                target = Person::class,
                targetPath = "felles/person",
                multiplicity = FintMultiplicity.EXACTLY_ONE,
                bidirectional = Bidirectional(inverseName = "personalressurs", isSource = false, inverseMultiplicity = FintMultiplicity.ZERO_OR_ONE),
            ),
            FintRelation(
                name = "stedfortreder",
                target = Fullmakt::class,
                targetPath = "administrasjon/fullmakt/fullmakt",
                multiplicity = FintMultiplicity.ZERO_OR_MORE,
                bidirectional = Bidirectional(inverseName = "stedfortreder", isSource = false, inverseMultiplicity = FintMultiplicity.ZERO_OR_ONE),
            ),
            FintRelation(
                name = "fullmakt",
                target = Fullmakt::class,
                targetPath = "administrasjon/fullmakt/fullmakt",
                multiplicity = FintMultiplicity.ZERO_OR_MORE,
                bidirectional = Bidirectional(inverseName = "fullmektig", isSource = false, inverseMultiplicity = FintMultiplicity.ZERO_OR_ONE),
            ),
            FintRelation(
                name = "leder",
                target = Organisasjonselement::class,
                targetPath = "administrasjon/organisasjon/organisasjonselement",
                multiplicity = FintMultiplicity.ZERO_OR_MORE,
                bidirectional = Bidirectional(inverseName = "leder", isSource = false, inverseMultiplicity = FintMultiplicity.ZERO_OR_ONE),
            ),
            FintRelation(
                name = "personalansvar",
                target = Arbeidsforhold::class,
                targetPath = "administrasjon/personal/arbeidsforhold",
                multiplicity = FintMultiplicity.ZERO_OR_MORE,
                bidirectional = Bidirectional(inverseName = "personalleder", isSource = false, inverseMultiplicity = FintMultiplicity.ZERO_OR_ONE),
            ),
            FintRelation(
                name = "skoleressurs",
                target = Skoleressurs::class,
                targetPath = "utdanning/elev/skoleressurs",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
                bidirectional = Bidirectional(inverseName = "personalressurs", isSource = false, inverseMultiplicity = FintMultiplicity.EXACTLY_ONE),
            ),
        )
    }
}
