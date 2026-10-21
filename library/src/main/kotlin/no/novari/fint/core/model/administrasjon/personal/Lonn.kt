package no.novari.fint.core.model.administrasjon.personal

import java.util.Date
import no.novari.fint.core.model.FintResource
import no.novari.fint.core.model.administrasjon.kompleksedatatyper.Kontostreng
import no.novari.fint.core.model.felles.kompleksedatatyper.Identifikator
import no.novari.fint.core.model.felles.kompleksedatatyper.Periode

interface Lonn : FintResource {
    val anvist: Date?
    val attestert: Date?
    val beskrivelse: String?
    val kildesystemId: Identifikator?
    val kontert: Date?
    val kontostreng: Kontostreng?
    val opptjent: Periode?
    val periode: Periode?
    val systemId: Identifikator?
}
