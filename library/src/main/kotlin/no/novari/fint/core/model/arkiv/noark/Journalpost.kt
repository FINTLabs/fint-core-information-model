package no.novari.fint.core.model.arkiv.noark

import java.time.LocalDateTime
import no.novari.fint.core.model.FintAttribute
import no.novari.fint.core.model.FintMultiplicity
import no.novari.fint.core.model.FintRelation
import no.novari.fint.core.model.FintResourceMetadata
import no.novari.fint.core.model.IdentifikatorVisitor
import no.novari.fint.core.model.Link
import no.novari.fint.core.model.arkiv.kodeverk.JournalStatus
import no.novari.fint.core.model.arkiv.kodeverk.JournalpostType
import no.novari.fint.core.model.arkiv.kodeverk.Tilgangsgruppe

data class Journalpost(
    val antallVedlegg: Long? = null,
    val avskrivning: Avskrivning? = null,
    val dokumentetsDato: LocalDateTime? = null,
    val forfallsDato: LocalDateTime? = null,
    val journalAr: String? = null,
    val journalDato: LocalDateTime? = null,
    val journalPostnummer: Long? = null,
    val journalSekvensnummer: Long? = null,
    val mottattDato: LocalDateTime? = null,
    val offentlighetsvurdertDato: LocalDateTime? = null,
    val sendtDato: LocalDateTime? = null,
    override val arkivertDato: LocalDateTime? = null,
    override val beskrivelse: String? = null,
    override val dokumentbeskrivelse: List<Dokumentbeskrivelse>? = null,
    override val forfatter: List<String>? = null,
    override val klasse: Klasse? = null,
    override val korrespondansepart: List<Korrespondansepart>? = null,
    override val merknad: List<Merknad>? = null,
    override val nokkelord: List<String>? = null,
    override val offentligTittel: String? = null,
    override val opprettetDato: LocalDateTime? = null,
    override val part: List<Part>? = null,
    override val referanseArkivDel: List<String>? = null,
    override val registreringsId: String? = null,
    override val skjerming: Skjerming? = null,
    override val tittel: String? = null,
) : Registrering {
    override val links: MutableMap<String, MutableList<Link>> = mutableMapOf()

    override val metadata: FintResourceMetadata get() = Metadata

    override fun visitIdentifikators(visitor: IdentifikatorVisitor) {}

    override fun identifikatorverdi(field: String): String? = null

    companion object Metadata : FintResourceMetadata {
        override val type = Journalpost::class
        override val ref = "arkiv-noark:Journalpost"
        override val path: String? = null
        override val idFields = emptyList<String>()
        override val attributes = listOf(
            FintAttribute("antallVedlegg", Long::class, list = false, optional = true),
            FintAttribute("avskrivning", Avskrivning::class, list = false, optional = true),
            FintAttribute("dokumentetsDato", LocalDateTime::class, list = false, optional = true),
            FintAttribute("forfallsDato", LocalDateTime::class, list = false, optional = true),
            FintAttribute("journalAr", String::class, list = false, optional = true),
            FintAttribute("journalDato", LocalDateTime::class, list = false, optional = true),
            FintAttribute("journalPostnummer", Long::class, list = false, optional = true),
            FintAttribute("journalSekvensnummer", Long::class, list = false, optional = true),
            FintAttribute("mottattDato", LocalDateTime::class, list = false, optional = true),
            FintAttribute("offentlighetsvurdertDato", LocalDateTime::class, list = false, optional = true),
            FintAttribute("sendtDato", LocalDateTime::class, list = false, optional = true),
            FintAttribute("arkivertDato", LocalDateTime::class, list = false, optional = true),
            FintAttribute("beskrivelse", String::class, list = false, optional = true),
            FintAttribute("dokumentbeskrivelse", Dokumentbeskrivelse::class, list = true, optional = true),
            FintAttribute("forfatter", String::class, list = true, optional = true),
            FintAttribute("klasse", Klasse::class, list = false, optional = true),
            FintAttribute("korrespondansepart", Korrespondansepart::class, list = true, optional = true),
            FintAttribute("merknad", Merknad::class, list = true, optional = true),
            FintAttribute("nokkelord", String::class, list = true, optional = true),
            FintAttribute("offentligTittel", String::class, list = false, optional = true),
            FintAttribute("opprettetDato", LocalDateTime::class, list = false, optional = true),
            FintAttribute("part", Part::class, list = true, optional = true),
            FintAttribute("referanseArkivDel", String::class, list = true, optional = true),
            FintAttribute("registreringsId", String::class, list = false, optional = true),
            FintAttribute("skjerming", Skjerming::class, list = false, optional = true),
            FintAttribute("tittel", String::class, list = false, optional = false),
        )
        override val relations = listOf(
            FintRelation(
                name = "journalposttype",
                target = JournalpostType::class,
                targetPath = "arkiv/kodeverk/journalposttype",
                multiplicity = FintMultiplicity.EXACTLY_ONE,
            ),
            FintRelation(
                name = "journalstatus",
                target = JournalStatus::class,
                targetPath = "arkiv/kodeverk/journalstatus",
                multiplicity = FintMultiplicity.EXACTLY_ONE,
            ),
            FintRelation(
                name = "journalenhet",
                target = AdministrativEnhet::class,
                targetPath = "arkiv/noark/administrativenhet",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "tilgangsgruppe",
                target = Tilgangsgruppe::class,
                targetPath = "arkiv/kodeverk/tilgangsgruppe",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "administrativEnhet",
                target = AdministrativEnhet::class,
                targetPath = "arkiv/noark/administrativenhet",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "arkivdel",
                target = Arkivdel::class,
                targetPath = "arkiv/noark/arkivdel",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "saksbehandler",
                target = Arkivressurs::class,
                targetPath = "arkiv/noark/arkivressurs",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "arkivertAv",
                target = Arkivressurs::class,
                targetPath = "arkiv/noark/arkivressurs",
                multiplicity = FintMultiplicity.EXACTLY_ONE,
            ),
            FintRelation(
                name = "opprettetAv",
                target = Arkivressurs::class,
                targetPath = "arkiv/noark/arkivressurs",
                multiplicity = FintMultiplicity.EXACTLY_ONE,
            ),
        )
    }
}
