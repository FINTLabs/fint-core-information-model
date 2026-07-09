package no.novari.fint.core.model.felles.basisklasser

import no.novari.fint.core.model.FintObject
import no.novari.fint.core.model.felles.kompleksedatatyper.Identifikator
import no.novari.fint.core.model.felles.kompleksedatatyper.Periode

interface Begrep : FintObject {
    var gyldighetsperiode: Periode?
    var kode: String?
    var navn: String?
    var passiv: Boolean?
    var systemId: Identifikator?
}
