package no.novari.fint.core.model.arkiv.noark

import java.time.LocalDateTime
import no.novari.fint.core.model.FintResource

interface Registrering : FintResource {
    val arkivertDato: LocalDateTime?
    val beskrivelse: String?
    val dokumentbeskrivelse: List<Dokumentbeskrivelse>?
    val forfatter: List<String>?
    val klasse: Klasse?
    val korrespondansepart: List<Korrespondansepart>?
    val merknad: List<Merknad>?
    val nokkelord: List<String>?
    val offentligTittel: String?
    val opprettetDato: LocalDateTime?
    val part: List<Part>?
    val referanseArkivDel: List<String>?
    val registreringsId: String?
    val skjerming: Skjerming?
    val tittel: String?
}
