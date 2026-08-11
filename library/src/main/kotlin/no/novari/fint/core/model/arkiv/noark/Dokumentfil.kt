package no.novari.fint.core.model.arkiv.noark

import no.novari.fint.core.model.FintAttribute
import no.novari.fint.core.model.FintRelation
import no.novari.fint.core.model.FintResource
import no.novari.fint.core.model.FintResourceMetadata
import no.novari.fint.core.model.IdentifikatorVisitor
import no.novari.fint.core.model.Link
import no.novari.fint.core.model.felles.kompleksedatatyper.Identifikator

data class Dokumentfil(
    val data: String? = null,
    val filnavn: String? = null,
    val format: String? = null,
    val systemId: Identifikator? = null,
) : FintResource {
    override val links: MutableMap<String, MutableList<Link>> = mutableMapOf()

    override val metadata: FintResourceMetadata get() = Metadata

    override fun visitIdentifikators(visitor: IdentifikatorVisitor) {
        systemId?.identifikatorverdi?.let { visitor.visit("systemId", it) }
    }

    override fun identifikatorverdi(field: String): String? = when {
        field.equals("systemId", ignoreCase = true) -> systemId?.identifikatorverdi
        else -> null
    }

    companion object Metadata : FintResourceMetadata {
        override val type = Dokumentfil::class
        override val ref = "arkiv-noark:Dokumentfil"
        override val path = "arkiv/noark/dokumentfil"
        override val name = "dokumentfil"
        override val isCommon = false
        override val idFields = listOf("systemId")
        override val attributes = listOf(
            FintAttribute("data", String::class, list = false, optional = false),
            FintAttribute("filnavn", String::class, list = false, optional = true),
            FintAttribute("format", String::class, list = false, optional = false),
            FintAttribute("systemId", Identifikator::class, list = false, optional = false),
        )
        override val relations = emptyList<FintRelation>()
    }
}
