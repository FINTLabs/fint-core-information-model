package no.novari.fint.core.model.arkiv.noark

import java.time.LocalDateTime
import no.novari.fint.core.model.FintAttribute
import no.novari.fint.core.model.FintObject
import no.novari.fint.core.model.FintTypeMetadata

data class Avskrivning(
    var avskrevetAv: String? = null,
    var avskrivningsdato: LocalDateTime? = null,
    var avskrivningsmate: String? = null,
) : FintObject {
    override val metadata: FintTypeMetadata get() = Metadata

    companion object Metadata : FintTypeMetadata {
        override val type = Avskrivning::class
        override val ref = "arkiv-noark:Avskrivning"
        override val attributes = listOf(
            FintAttribute("avskrevetAv", String::class, list = false, optional = false),
            FintAttribute("avskrivningsdato", LocalDateTime::class, list = false, optional = false),
            FintAttribute("avskrivningsmate", String::class, list = false, optional = false),
        )
    }
}
