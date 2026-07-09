package no.novari.fint.core.model.utdanning.vurdering

import no.novari.fint.core.model.FintAttribute
import no.novari.fint.core.model.FintObject
import no.novari.fint.core.model.FintTypeMetadata

data class Fravarsprosent(
    val fravarstimer: Int? = null,
    val prosent: Int? = null,
    val undervisningstimer: Int? = null,
) : FintObject {
    override val metadata: FintTypeMetadata get() = Metadata

    companion object Metadata : FintTypeMetadata {
        override val type = Fravarsprosent::class
        override val ref = "utdanning-vurdering:Fravarsprosent"
        override val attributes = listOf(
            FintAttribute("fravarstimer", Int::class, list = false, optional = false),
            FintAttribute("prosent", Int::class, list = false, optional = false),
            FintAttribute("undervisningstimer", Int::class, list = false, optional = false),
        )
    }
}
