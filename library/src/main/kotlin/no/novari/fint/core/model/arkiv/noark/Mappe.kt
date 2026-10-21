package no.novari.fint.core.model.arkiv.noark

import java.util.Date
import no.novari.fint.core.model.FintResource
import no.novari.fint.core.model.felles.kompleksedatatyper.Identifikator

interface Mappe : FintResource {
    val avsluttetDato: Date?
    val beskrivelse: String?
    val klasse: List<Klasse>?
    val mappeId: Identifikator?
    val merknad: List<Merknad>?
    val noekkelord: List<String>?
    val offentligTittel: String?
    val opprettetDato: Date?
    val part: List<Part>?
    val skjerming: Skjerming?
    val systemId: Identifikator?
    val tittel: String?
}
