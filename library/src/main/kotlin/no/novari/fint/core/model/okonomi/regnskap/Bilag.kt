package no.novari.fint.core.model.okonomi.regnskap

import java.util.Date
import no.novari.fint.core.model.FintAttribute
import no.novari.fint.core.model.FintObject
import no.novari.fint.core.model.FintTypeMetadata

data class Bilag(
    val bilagsdato: Date? = null,
    val bilagsnummer: String? = null,
    val data: String? = null,
    val filnavn: String? = null,
    val referanse: String? = null,
    val url: String? = null,
) : FintObject {
    override val metadata: FintTypeMetadata get() = Metadata

    companion object Metadata : FintTypeMetadata {
        override val type = Bilag::class
        override val ref = "okonomi-regnskap:Bilag"
        override val attributes = listOf(
            FintAttribute("bilagsdato", Date::class, list = false, optional = false),
            FintAttribute("bilagsnummer", String::class, list = false, optional = true),
            FintAttribute("data", String::class, list = false, optional = true),
            FintAttribute("filnavn", String::class, list = false, optional = true),
            FintAttribute("referanse", String::class, list = false, optional = true),
            FintAttribute("url", String::class, list = false, optional = true),
        )
    }
}
