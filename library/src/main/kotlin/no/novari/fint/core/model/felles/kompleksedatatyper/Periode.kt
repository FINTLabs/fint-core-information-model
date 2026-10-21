package no.novari.fint.core.model.felles.kompleksedatatyper

import java.util.Date
import no.novari.fint.core.model.FintAttribute
import no.novari.fint.core.model.FintObject
import no.novari.fint.core.model.FintTypeMetadata

data class Periode(
    val beskrivelse: String? = null,
    val slutt: Date? = null,
    val start: Date? = null,
) : FintObject {
    override val metadata: FintTypeMetadata get() = Metadata

    companion object Metadata : FintTypeMetadata {
        override val type = Periode::class
        override val ref = "felles-kompleksedatatyper:Periode"
        override val attributes = listOf(
            FintAttribute("beskrivelse", String::class, list = false, optional = true),
            FintAttribute("slutt", Date::class, list = false, optional = true),
            FintAttribute("start", Date::class, list = false, optional = false),
        )
    }
}
