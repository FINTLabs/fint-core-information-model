package no.novari.fint.core.model.felles.kompleksedatatyper

import java.time.LocalDateTime
import no.novari.fint.core.model.FintAttribute
import no.novari.fint.core.model.FintObject
import no.novari.fint.core.model.FintTypeMetadata

data class Periode(
    val beskrivelse: String? = null,
    val slutt: LocalDateTime? = null,
    val start: LocalDateTime? = null,
) : FintObject {
    override val metadata: FintTypeMetadata get() = Metadata

    companion object Metadata : FintTypeMetadata {
        override val type = Periode::class
        override val ref = "felles-kompleksedatatyper:Periode"
        override val attributes = listOf(
            FintAttribute("beskrivelse", String::class, list = false, optional = true),
            FintAttribute("slutt", LocalDateTime::class, list = false, optional = true),
            FintAttribute("start", LocalDateTime::class, list = false, optional = false),
        )
    }
}
