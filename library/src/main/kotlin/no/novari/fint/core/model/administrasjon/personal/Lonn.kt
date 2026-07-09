package no.novari.fint.core.model.administrasjon.personal

import java.time.LocalDateTime
import no.novari.fint.core.model.FintResource
import no.novari.fint.core.model.administrasjon.kompleksedatatyper.Kontostreng
import no.novari.fint.core.model.felles.kompleksedatatyper.Identifikator
import no.novari.fint.core.model.felles.kompleksedatatyper.Periode

interface Lonn : FintResource {
    var anvist: LocalDateTime?
    var attestert: LocalDateTime?
    var beskrivelse: String?
    var kildesystemId: Identifikator?
    var kontert: LocalDateTime?
    var kontostreng: Kontostreng?
    var opptjent: Periode?
    var periode: Periode?
    var systemId: Identifikator?
}
