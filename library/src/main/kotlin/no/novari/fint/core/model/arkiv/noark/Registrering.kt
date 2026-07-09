package no.novari.fint.core.model.arkiv.noark

import java.time.LocalDateTime
import no.novari.fint.core.model.FintResource

interface Registrering : FintResource {
    var arkivertDato: LocalDateTime?
    var beskrivelse: String?
    var dokumentbeskrivelse: List<Dokumentbeskrivelse>?
    var forfatter: List<String>?
    var klasse: Klasse?
    var korrespondansepart: List<Korrespondansepart>?
    var merknad: List<Merknad>?
    var nokkelord: List<String>?
    var offentligTittel: String?
    var opprettetDato: LocalDateTime?
    var part: List<Part>?
    var referanseArkivDel: List<String>?
    var registreringsId: String?
    var skjerming: Skjerming?
    var tittel: String?
}
