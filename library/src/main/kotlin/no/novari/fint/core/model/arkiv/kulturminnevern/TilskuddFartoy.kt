package no.novari.fint.core.model.arkiv.kulturminnevern

import java.time.LocalDateTime
import no.novari.fint.core.model.FintAttribute
import no.novari.fint.core.model.FintMultiplicity
import no.novari.fint.core.model.FintRelation
import no.novari.fint.core.model.FintResourceMetadata
import no.novari.fint.core.model.IdentifikatorVisitor
import no.novari.fint.core.model.Link
import no.novari.fint.core.model.arkiv.kodeverk.Saksmappetype
import no.novari.fint.core.model.arkiv.kodeverk.Saksstatus
import no.novari.fint.core.model.arkiv.kodeverk.Tilgangsgruppe
import no.novari.fint.core.model.arkiv.noark.AdministrativEnhet
import no.novari.fint.core.model.arkiv.noark.Arkivdel
import no.novari.fint.core.model.arkiv.noark.Arkivressurs
import no.novari.fint.core.model.arkiv.noark.Journalpost
import no.novari.fint.core.model.arkiv.noark.Klasse
import no.novari.fint.core.model.arkiv.noark.Merknad
import no.novari.fint.core.model.arkiv.noark.Part
import no.novari.fint.core.model.arkiv.noark.Saksmappe
import no.novari.fint.core.model.arkiv.noark.Skjerming
import no.novari.fint.core.model.felles.kompleksedatatyper.Identifikator

data class TilskuddFartoy(
    var fartoyNavn: String? = null,
    var kallesignal: String? = null,
    var kulturminneId: String? = null,
    var soknadsnummer: Identifikator? = null,
    override var journalpost: List<Journalpost>? = null,
    override var saksaar: String? = null,
    override var saksdato: LocalDateTime? = null,
    override var sakssekvensnummer: String? = null,
    override var utlaantDato: LocalDateTime? = null,
    override var avsluttetDato: LocalDateTime? = null,
    override var beskrivelse: String? = null,
    override var klasse: List<Klasse>? = null,
    override var mappeId: Identifikator? = null,
    override var merknad: List<Merknad>? = null,
    override var noekkelord: List<String>? = null,
    override var offentligTittel: String? = null,
    override var opprettetDato: LocalDateTime? = null,
    override var part: List<Part>? = null,
    override var skjerming: Skjerming? = null,
    override var systemId: Identifikator? = null,
    override var tittel: String? = null,
) : Saksmappe {
    override val links: MutableMap<String, MutableList<Link>> = mutableMapOf()

    override val metadata: FintResourceMetadata get() = Metadata

    override fun visitIdentifikators(visitor: IdentifikatorVisitor) {
        mappeId?.identifikatorverdi?.let { visitor.visit("mappeId", it) }
        systemId?.identifikatorverdi?.let { visitor.visit("systemId", it) }
        soknadsnummer?.identifikatorverdi?.let { visitor.visit("soknadsnummer", it) }
    }

    override fun identifikatorverdi(field: String): String? = when {
        field.equals("mappeId", ignoreCase = true) -> mappeId?.identifikatorverdi
        field.equals("systemId", ignoreCase = true) -> systemId?.identifikatorverdi
        field.equals("soknadsnummer", ignoreCase = true) -> soknadsnummer?.identifikatorverdi
        else -> null
    }

    companion object Metadata : FintResourceMetadata {
        override val type = TilskuddFartoy::class
        override val ref = "arkiv-kulturminnevern:TilskuddFartoy"
        override val path = "arkiv/kulturminnevern/tilskuddfartoy"
        override val idFields = listOf("mappeId", "systemId", "soknadsnummer")
        override val attributes = listOf(
            FintAttribute("fartoyNavn", String::class, list = false, optional = false),
            FintAttribute("kallesignal", String::class, list = false, optional = false),
            FintAttribute("kulturminneId", String::class, list = false, optional = false),
            FintAttribute("soknadsnummer", Identifikator::class, list = false, optional = false),
            FintAttribute("journalpost", Journalpost::class, list = true, optional = true),
            FintAttribute("saksaar", String::class, list = false, optional = true),
            FintAttribute("saksdato", LocalDateTime::class, list = false, optional = true),
            FintAttribute("sakssekvensnummer", String::class, list = false, optional = true),
            FintAttribute("utlaantDato", LocalDateTime::class, list = false, optional = true),
            FintAttribute("avsluttetDato", LocalDateTime::class, list = false, optional = true),
            FintAttribute("beskrivelse", String::class, list = false, optional = true),
            FintAttribute("klasse", Klasse::class, list = true, optional = true),
            FintAttribute("mappeId", Identifikator::class, list = false, optional = true),
            FintAttribute("merknad", Merknad::class, list = true, optional = true),
            FintAttribute("noekkelord", String::class, list = true, optional = true),
            FintAttribute("offentligTittel", String::class, list = false, optional = true),
            FintAttribute("opprettetDato", LocalDateTime::class, list = false, optional = true),
            FintAttribute("part", Part::class, list = true, optional = true),
            FintAttribute("skjerming", Skjerming::class, list = false, optional = true),
            FintAttribute("systemId", Identifikator::class, list = false, optional = true),
            FintAttribute("tittel", String::class, list = false, optional = true),
        )
        override val relations = listOf(
            FintRelation(
                name = "saksmappetype",
                target = Saksmappetype::class,
                targetPath = "arkiv/kodeverk/saksmappetype",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "saksstatus",
                target = Saksstatus::class,
                targetPath = "arkiv/kodeverk/saksstatus",
                multiplicity = FintMultiplicity.EXACTLY_ONE,
            ),
            FintRelation(
                name = "tilgangsgruppe",
                target = Tilgangsgruppe::class,
                targetPath = "arkiv/kodeverk/tilgangsgruppe",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "journalenhet",
                target = AdministrativEnhet::class,
                targetPath = "arkiv/noark/administrativenhet",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "administrativEnhet",
                target = AdministrativEnhet::class,
                targetPath = "arkiv/noark/administrativenhet",
                multiplicity = FintMultiplicity.EXACTLY_ONE,
            ),
            FintRelation(
                name = "saksansvarlig",
                target = Arkivressurs::class,
                targetPath = "arkiv/noark/arkivressurs",
                multiplicity = FintMultiplicity.EXACTLY_ONE,
            ),
            FintRelation(
                name = "arkivdel",
                target = Arkivdel::class,
                targetPath = "arkiv/noark/arkivdel",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "avsluttetAv",
                target = Arkivressurs::class,
                targetPath = "arkiv/noark/arkivressurs",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
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
