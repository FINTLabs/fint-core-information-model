package no.novari.fint.core.model.felles.kompleksedatatyper

import no.novari.fint.core.model.FintAttribute
import no.novari.fint.core.model.FintObject
import no.novari.fint.core.model.FintTypeMetadata

data class Personnavn(
    val etternavn: String? = null,
    val fornavn: String? = null,
    val mellomnavn: String? = null,
) : FintObject {
    override val metadata: FintTypeMetadata get() = Metadata

    companion object Metadata : FintTypeMetadata {
        override val type = Personnavn::class
        override val ref = "felles-kompleksedatatyper:Personnavn"
        override val attributes = listOf(
            FintAttribute("etternavn", String::class, list = false, optional = false),
            FintAttribute("fornavn", String::class, list = false, optional = false),
            FintAttribute("mellomnavn", String::class, list = false, optional = true),
        )
    }
}
