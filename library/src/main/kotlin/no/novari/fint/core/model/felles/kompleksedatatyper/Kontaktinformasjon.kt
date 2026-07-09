package no.novari.fint.core.model.felles.kompleksedatatyper

import no.novari.fint.core.model.FintAttribute
import no.novari.fint.core.model.FintObject
import no.novari.fint.core.model.FintTypeMetadata

data class Kontaktinformasjon(
    val epostadresse: String? = null,
    val mobiltelefonnummer: String? = null,
    val nettsted: String? = null,
    val sip: String? = null,
    val telefonnummer: String? = null,
) : FintObject {
    override val metadata: FintTypeMetadata get() = Metadata

    companion object Metadata : FintTypeMetadata {
        override val type = Kontaktinformasjon::class
        override val ref = "felles-kompleksedatatyper:Kontaktinformasjon"
        override val attributes = listOf(
            FintAttribute("epostadresse", String::class, list = false, optional = true),
            FintAttribute("mobiltelefonnummer", String::class, list = false, optional = true),
            FintAttribute("nettsted", String::class, list = false, optional = true),
            FintAttribute("sip", String::class, list = false, optional = true),
            FintAttribute("telefonnummer", String::class, list = false, optional = true),
        )
    }
}
