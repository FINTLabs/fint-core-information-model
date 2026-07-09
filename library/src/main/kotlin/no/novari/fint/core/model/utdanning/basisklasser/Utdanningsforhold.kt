package no.novari.fint.core.model.utdanning.basisklasser

import no.novari.fint.core.model.FintObject
import no.novari.fint.core.model.felles.kompleksedatatyper.Identifikator

interface Utdanningsforhold : FintObject {
    val beskrivelse: String?
    val systemId: Identifikator?
}
