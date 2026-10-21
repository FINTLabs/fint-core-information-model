package no.novari.fint.core.model.utdanning.vurdering

import java.util.Date
import no.novari.fint.core.model.FintResource
import no.novari.fint.core.model.felles.kompleksedatatyper.Identifikator

interface Ordensvurdering : FintResource {
    val kommentar: String?
    val systemId: Identifikator?
    val vurderingsdato: Date?
}
