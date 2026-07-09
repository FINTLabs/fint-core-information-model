package no.novari.fint.core.model.arkiv.noark

import java.time.LocalDateTime

interface Saksmappe : Mappe {
    var journalpost: List<Journalpost>?
    var saksaar: String?
    var saksdato: LocalDateTime?
    var sakssekvensnummer: String?
    var utlaantDato: LocalDateTime?
}
