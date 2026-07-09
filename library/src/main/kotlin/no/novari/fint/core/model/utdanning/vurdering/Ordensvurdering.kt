package no.novari.fint.core.model.utdanning.vurdering

import java.time.LocalDateTime
import no.novari.fint.core.model.FintResource
import no.novari.fint.core.model.felles.kompleksedatatyper.Identifikator

interface Ordensvurdering : FintResource {
    val kommentar: String?
    val systemId: Identifikator?
    val vurderingsdato: LocalDateTime?
}
