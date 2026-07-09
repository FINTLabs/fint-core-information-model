package no.novari.fint.core.model.administrasjon.personal

import java.time.LocalDateTime
import no.novari.fint.core.model.FintResource
import no.novari.fint.core.model.administrasjon.kompleksedatatyper.Kontostreng
import no.novari.fint.core.model.felles.kompleksedatatyper.Identifikator
import no.novari.fint.core.model.felles.kompleksedatatyper.Periode

interface Lonn : FintResource {
    val anvist: LocalDateTime?
    val attestert: LocalDateTime?
    val beskrivelse: String?
    val kildesystemId: Identifikator?
    val kontert: LocalDateTime?
    val kontostreng: Kontostreng?
    val opptjent: Periode?
    val periode: Periode?
    val systemId: Identifikator?
}
