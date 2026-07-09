package no.novari.fint.core.model.utdanning.kodeverk

import no.novari.fint.core.model.FintAttribute
import no.novari.fint.core.model.FintObject
import no.novari.fint.core.model.FintTypeMetadata

class Vigoreferanse : FintObject {
    override val metadata: FintTypeMetadata get() = Metadata

    companion object Metadata : FintTypeMetadata {
        override val type = Vigoreferanse::class
        override val ref = "utdanning-kodeverk:Vigoreferanse"
        override val attributes = emptyList<FintAttribute>()
    }
}
