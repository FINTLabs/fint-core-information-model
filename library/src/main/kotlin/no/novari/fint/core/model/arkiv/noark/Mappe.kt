package no.novari.fint.core.model.arkiv.noark

import java.time.LocalDateTime
import no.novari.fint.core.model.FintResource
import no.novari.fint.core.model.felles.kompleksedatatyper.Identifikator

interface Mappe : FintResource {
    var avsluttetDato: LocalDateTime?
    var beskrivelse: String?
    var klasse: List<Klasse>?
    var mappeId: Identifikator?
    var merknad: List<Merknad>?
    var noekkelord: List<String>?
    var offentligTittel: String?
    var opprettetDato: LocalDateTime?
    var part: List<Part>?
    var skjerming: Skjerming?
    var systemId: Identifikator?
    var tittel: String?
}
