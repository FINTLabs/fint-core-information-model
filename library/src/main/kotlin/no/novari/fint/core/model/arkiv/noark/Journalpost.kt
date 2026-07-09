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
import no.novari.fint.core.model.felles.kompleksedatatyper.Identifikator

data class Journalpost(
    var antallVedlegg: Long? = null,
    var avskrivning: Avskrivning? = null,
    var dokumentetsDato: LocalDateTime? = null,
    var forfallsDato: LocalDateTime? = null,
    var journalAr: String? = null,
    var journalDato: LocalDateTime? = null,
    var journalPostnummer: Long? = null,
    var journalSekvensnummer: Long? = null,
    var mottattDato: LocalDateTime? = null,
    var offentlighetsvurdertDato: LocalDateTime? = null,
    var sendtDato: LocalDateTime? = null,
    override var arkivertDato: LocalDateTime? = null,
    override var beskrivelse: String? = null,
    override var dokumentbeskrivelse: List<Dokumentbeskrivelse>? = null,
    override var forfatter: List<String>? = null,
    override var klasse: Klasse? = null,
    override var korrespondansepart: List<Korrespondansepart>? = null,
    override var merknad: List<Merknad>? = null,
    override var nokkelord: List<String>? = null,
    override var offentligTittel: String? = null,
    override var opprettetDato: LocalDateTime? = null,
    override var part: List<Part>? = null,
    override var referanseArkivDel: List<String>? = null,
    override var registreringsId: String? = null,
    override var skjerming: Skjerming? = null,
    override var tittel: String? = null,
) : Registrering {
    override val links: MutableMap<String, MutableList<Link>> = mutableMapOf()

    override val metadata: FintResourceMetadata get() = Metadata

    override fun visitIdentifikators(visitor: IdentifikatorVisitor) {}

    override fun identifikator(field: String): Identifikator? = null

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
