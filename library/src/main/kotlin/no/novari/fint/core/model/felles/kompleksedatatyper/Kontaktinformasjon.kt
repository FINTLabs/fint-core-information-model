package no.novari.fint.core.model.felles.kompleksedatatyper

import no.novari.fint.core.model.FintAttribute
import no.novari.fint.core.model.FintObject
import no.novari.fint.core.model.FintTypeMetadata

data class Kontaktinformasjon(
    var epostadresse: String? = null,
    var mobiltelefonnummer: String? = null,
    var nettsted: String? = null,
    var sip: String? = null,
    var telefonnummer: String? = null,
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
