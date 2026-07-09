package no.novari.fint.core.model.felles.basisklasser

import no.novari.fint.core.model.felles.kompleksedatatyper.Adresse
import no.novari.fint.core.model.felles.kompleksedatatyper.Identifikator

interface Enhet : Aktor {
    var forretningsadresse: Adresse?
    var organisasjonsnavn: String?
    var organisasjonsnummer: Identifikator?
}
