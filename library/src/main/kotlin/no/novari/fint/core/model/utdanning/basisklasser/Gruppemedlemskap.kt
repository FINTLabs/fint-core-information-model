package no.novari.fint.core.model.utdanning.basisklasser

import no.novari.fint.core.model.FintObject
import no.novari.fint.core.model.felles.kompleksedatatyper.Identifikator
import no.novari.fint.core.model.felles.kompleksedatatyper.Periode

interface Gruppemedlemskap : FintObject {
    var gyldighetsperiode: Periode?
    var systemId: Identifikator?
}
