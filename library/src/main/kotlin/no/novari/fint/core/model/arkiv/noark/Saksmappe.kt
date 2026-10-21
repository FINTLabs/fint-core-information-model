package no.novari.fint.core.model.arkiv.noark

import java.util.Date

interface Saksmappe : Mappe {
    val journalpost: List<Journalpost>?
    val saksaar: String?
    val saksdato: Date?
    val sakssekvensnummer: String?
    val utlaantDato: Date?
}
