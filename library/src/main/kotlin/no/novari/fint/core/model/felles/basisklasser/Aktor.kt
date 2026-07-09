package no.novari.fint.core.model.felles.basisklasser

import no.novari.fint.core.model.FintObject
import no.novari.fint.core.model.felles.kompleksedatatyper.Adresse
import no.novari.fint.core.model.felles.kompleksedatatyper.Kontaktinformasjon

interface Aktor : FintObject {
    var kontaktinformasjon: Kontaktinformasjon?
    var postadresse: Adresse?
}
