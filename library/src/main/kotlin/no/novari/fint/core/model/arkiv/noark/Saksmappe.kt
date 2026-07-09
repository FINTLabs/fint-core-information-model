package no.novari.fint.core.model.arkiv.noark

import java.time.LocalDateTime

interface Saksmappe : Mappe {
    val journalpost: List<Journalpost>?
    val saksaar: String?
    val saksdato: LocalDateTime?
    val sakssekvensnummer: String?
    val utlaantDato: LocalDateTime?
}
