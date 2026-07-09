package no.novari.fint.core.model.felles.basisklasser

import no.novari.fint.core.model.FintObject
import no.novari.fint.core.model.felles.kompleksedatatyper.Identifikator
import no.novari.fint.core.model.felles.kompleksedatatyper.Periode

interface Begrep : FintObject {
    val gyldighetsperiode: Periode?
    val kode: String?
    val navn: String?
    val passiv: Boolean?
    val systemId: Identifikator?
}
