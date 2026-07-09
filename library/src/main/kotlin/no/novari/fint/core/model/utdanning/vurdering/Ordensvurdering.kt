package no.novari.fint.core.model.utdanning.vurdering

import java.time.LocalDateTime
import no.novari.fint.core.model.FintResource
import no.novari.fint.core.model.felles.kompleksedatatyper.Identifikator

interface Ordensvurdering : FintResource {
    var kommentar: String?
    var systemId: Identifikator?
    var vurderingsdato: LocalDateTime?
}
