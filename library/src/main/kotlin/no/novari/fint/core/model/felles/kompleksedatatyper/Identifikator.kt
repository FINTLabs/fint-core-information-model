package no.novari.fint.core.model.felles.kompleksedatatyper

import no.novari.fint.core.model.FintAttribute
import no.novari.fint.core.model.FintObject
import no.novari.fint.core.model.FintTypeMetadata

data class Identifikator(
    val gyldighetsperiode: Periode? = null,
    val identifikatorverdi: String? = null,
) : FintObject {
    override val metadata: FintTypeMetadata get() = Metadata

    companion object Metadata : FintTypeMetadata {
        override val type = Identifikator::class
        override val ref = "felles-kompleksedatatyper:Identifikator"
        override val attributes = listOf(
            FintAttribute("gyldighetsperiode", Periode::class, list = false, optional = true),
            FintAttribute("identifikatorverdi", String::class, list = false, optional = false),
        )
    }
}
