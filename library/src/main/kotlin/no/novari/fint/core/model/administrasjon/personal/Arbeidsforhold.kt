package no.novari.fint.core.model.administrasjon.personal

import no.novari.fint.core.model.Bidirectional
import no.novari.fint.core.model.FintAttribute
import no.novari.fint.core.model.FintMultiplicity
import no.novari.fint.core.model.FintRelation
import no.novari.fint.core.model.FintResource
import no.novari.fint.core.model.FintResourceMetadata
import no.novari.fint.core.model.IdentifikatorVisitor
import no.novari.fint.core.model.Link
import no.novari.fint.core.model.administrasjon.kodeverk.Aktivitet
import no.novari.fint.core.model.administrasjon.kodeverk.Anlegg
import no.novari.fint.core.model.administrasjon.kodeverk.Ansvar
import no.novari.fint.core.model.administrasjon.kodeverk.Arbeidsforholdstype
import no.novari.fint.core.model.administrasjon.kodeverk.Art
import no.novari.fint.core.model.administrasjon.kodeverk.Diverse
import no.novari.fint.core.model.administrasjon.kodeverk.Formal
import no.novari.fint.core.model.administrasjon.kodeverk.Funksjon
import no.novari.fint.core.model.administrasjon.kodeverk.Kontrakt
import no.novari.fint.core.model.administrasjon.kodeverk.Lopenummer
import no.novari.fint.core.model.administrasjon.kodeverk.Objekt
import no.novari.fint.core.model.administrasjon.kodeverk.Prosjekt
import no.novari.fint.core.model.administrasjon.kodeverk.Ramme
import no.novari.fint.core.model.administrasjon.kodeverk.Stillingskode
import no.novari.fint.core.model.administrasjon.kodeverk.Uketimetall
import no.novari.fint.core.model.administrasjon.organisasjon.Arbeidslokasjon
import no.novari.fint.core.model.administrasjon.organisasjon.Organisasjonselement
import no.novari.fint.core.model.felles.kompleksedatatyper.Identifikator
import no.novari.fint.core.model.felles.kompleksedatatyper.Periode
import no.novari.fint.core.model.utdanning.elev.Undervisningsforhold

data class Arbeidsforhold(
    var ansettelsesprosent: Long? = null,
    var arbeidsforholdsperiode: Periode? = null,
    var arslonn: Long? = null,
    var gyldighetsperiode: Periode? = null,
    var hovedstilling: Boolean? = null,
    var lonnsprosent: Long? = null,
    var stillingsnummer: String? = null,
    var stillingstittel: String? = null,
    var systemId: Identifikator? = null,
    var tilstedeprosent: Long? = null,
) : FintResource {
    override val links: MutableMap<String, MutableList<Link>> = mutableMapOf()

    override val metadata: FintResourceMetadata get() = Metadata

    override fun visitIdentifikators(visitor: IdentifikatorVisitor) {
        systemId?.let { visitor.visit("systemId", it) }
    }

    override fun identifikator(field: String): Identifikator? = when {
        field.equals("systemId", ignoreCase = true) -> systemId
        else -> null
    }

    companion object Metadata : FintResourceMetadata {
        override val type = Arbeidsforhold::class
        override val ref = "administrasjon-personal:Arbeidsforhold"
        override val path = "administrasjon/personal/arbeidsforhold"
        override val idFields = listOf("systemId")
        override val attributes = listOf(
            FintAttribute("ansettelsesprosent", Long::class, list = false, optional = false),
            FintAttribute("arbeidsforholdsperiode", Periode::class, list = false, optional = true),
            FintAttribute("arslonn", Long::class, list = false, optional = false),
            FintAttribute("gyldighetsperiode", Periode::class, list = false, optional = false),
            FintAttribute("hovedstilling", Boolean::class, list = false, optional = false),
            FintAttribute("lonnsprosent", Long::class, list = false, optional = false),
            FintAttribute("stillingsnummer", String::class, list = false, optional = false),
            FintAttribute("stillingstittel", String::class, list = false, optional = true),
            FintAttribute("systemId", Identifikator::class, list = false, optional = false),
            FintAttribute("tilstedeprosent", Long::class, list = false, optional = false),
        )
        override val relations = listOf(
            FintRelation(
                name = "aktivitet",
                target = Aktivitet::class,
                targetPath = "administrasjon/kodeverk/aktivitet",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "anlegg",
                target = Anlegg::class,
                targetPath = "administrasjon/kodeverk/anlegg",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "ansvar",
                target = Ansvar::class,
                targetPath = "administrasjon/kodeverk/ansvar",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "arbeidsforholdstype",
                target = Arbeidsforholdstype::class,
                targetPath = "administrasjon/kodeverk/arbeidsforholdstype",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "art",
                target = Art::class,
                targetPath = "administrasjon/kodeverk/art",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "diverse",
                target = Diverse::class,
                targetPath = "administrasjon/kodeverk/diverse",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "formal",
                target = Formal::class,
                targetPath = "administrasjon/kodeverk/formal",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "funksjon",
                target = Funksjon::class,
                targetPath = "administrasjon/kodeverk/funksjon",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "kontrakt",
                target = Kontrakt::class,
                targetPath = "administrasjon/kodeverk/kontrakt",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "lopenummer",
                target = Lopenummer::class,
                targetPath = "administrasjon/kodeverk/lopenummer",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "objekt",
                target = Objekt::class,
                targetPath = "administrasjon/kodeverk/objekt",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "prosjekt",
                target = Prosjekt::class,
                targetPath = "administrasjon/kodeverk/prosjekt",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "ramme",
                target = Ramme::class,
                targetPath = "administrasjon/kodeverk/ramme",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "stillingskode",
                target = Stillingskode::class,
                targetPath = "administrasjon/kodeverk/stillingskode",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "timerPerUke",
                target = Uketimetall::class,
                targetPath = "administrasjon/kodeverk/uketimetall",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "arbeidslokasjon",
                target = Arbeidslokasjon::class,
                targetPath = "administrasjon/organisasjon/arbeidslokasjon",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
                bidirectional = Bidirectional(inverseName = "arbeidsforhold", isSource = true, inverseMultiplicity = FintMultiplicity.ZERO_OR_MORE),
            ),
            FintRelation(
                name = "arbeidssted",
                target = Organisasjonselement::class,
                targetPath = "administrasjon/organisasjon/organisasjonselement",
                multiplicity = FintMultiplicity.EXACTLY_ONE,
                bidirectional = Bidirectional(inverseName = "arbeidsforhold", isSource = true, inverseMultiplicity = FintMultiplicity.ZERO_OR_MORE),
            ),
            FintRelation(
                name = "personalleder",
                target = Personalressurs::class,
                targetPath = "administrasjon/personal/personalressurs",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
                bidirectional = Bidirectional(inverseName = "personalansvar", isSource = true, inverseMultiplicity = FintMultiplicity.ZERO_OR_MORE),
            ),
            FintRelation(
                name = "fastlonn",
                target = Fastlonn::class,
                targetPath = "administrasjon/personal/fastlonn",
                multiplicity = FintMultiplicity.ZERO_OR_MORE,
                bidirectional = Bidirectional(inverseName = "arbeidsforhold", isSource = false, inverseMultiplicity = FintMultiplicity.EXACTLY_ONE),
            ),
            FintRelation(
                name = "fasttillegg",
                target = Fasttillegg::class,
                targetPath = "administrasjon/personal/fasttillegg",
                multiplicity = FintMultiplicity.ZERO_OR_MORE,
                bidirectional = Bidirectional(inverseName = "arbeidsforhold", isSource = false, inverseMultiplicity = FintMultiplicity.EXACTLY_ONE),
            ),
            FintRelation(
                name = "fravar",
                target = Fravar::class,
                targetPath = "administrasjon/personal/fravar",
                multiplicity = FintMultiplicity.ZERO_OR_MORE,
                bidirectional = Bidirectional(inverseName = "arbeidsforhold", isSource = false, inverseMultiplicity = FintMultiplicity.ONE_OR_MORE),
            ),
            FintRelation(
                name = "variabellonn",
                target = Variabellonn::class,
                targetPath = "administrasjon/personal/variabellonn",
                multiplicity = FintMultiplicity.ZERO_OR_MORE,
                bidirectional = Bidirectional(inverseName = "arbeidsforhold", isSource = false, inverseMultiplicity = FintMultiplicity.EXACTLY_ONE),
            ),
            FintRelation(
                name = "personalressurs",
                target = Personalressurs::class,
                targetPath = "administrasjon/personal/personalressurs",
                multiplicity = FintMultiplicity.EXACTLY_ONE,
                bidirectional = Bidirectional(inverseName = "arbeidsforhold", isSource = false, inverseMultiplicity = FintMultiplicity.ZERO_OR_MORE),
            ),
            FintRelation(
                name = "undervisningsforhold",
                target = Undervisningsforhold::class,
                targetPath = "utdanning/elev/undervisningsforhold",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
                bidirectional = Bidirectional(inverseName = "arbeidsforhold", isSource = false, inverseMultiplicity = FintMultiplicity.EXACTLY_ONE),
            ),
        )
    }
}
