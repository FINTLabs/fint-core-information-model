package no.novari.fint.core.model.felles.kodeverk

import no.novari.fint.core.model.FintAttribute
import no.novari.fint.core.model.FintRelation
import no.novari.fint.core.model.FintResource
import no.novari.fint.core.model.FintResourceMetadata
import no.novari.fint.core.model.IdentifikatorVisitor
import no.novari.fint.core.model.Link
import no.novari.fint.core.model.felles.kompleksedatatyper.Identifikator

data class Valuta(
    val bokstavkode: Identifikator? = null,
    val navn: String? = null,
    val nummerkode: Identifikator? = null,
) : FintResource {
    override val links: MutableMap<String, MutableList<Link>> = mutableMapOf()

    override val metadata: FintResourceMetadata get() = Metadata

    override fun visitIdentifikators(visitor: IdentifikatorVisitor) {
        bokstavkode?.identifikatorverdi?.let { visitor.visit("bokstavkode", it) }
        nummerkode?.identifikatorverdi?.let { visitor.visit("nummerkode", it) }
    }

    override fun identifikatorverdi(field: String): String? = when {
        field.equals("bokstavkode", ignoreCase = true) -> bokstavkode?.identifikatorverdi
        field.equals("nummerkode", ignoreCase = true) -> nummerkode?.identifikatorverdi
        else -> null
    }

    companion object Metadata : FintResourceMetadata {
        override val type = Valuta::class
        override val ref = "felles-kodeverk:Valuta"
        override val path = "felles/kodeverk/valuta"
        override val idFields = listOf("bokstavkode", "nummerkode")
        override val attributes = listOf(
            FintAttribute("bokstavkode", Identifikator::class, list = false, optional = false),
            FintAttribute("navn", String::class, list = false, optional = false),
            FintAttribute("nummerkode", Identifikator::class, list = false, optional = false),
        )
        override val relations = emptyList<FintRelation>()
    }
}
