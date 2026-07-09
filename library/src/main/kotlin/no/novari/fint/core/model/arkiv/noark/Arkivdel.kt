package no.novari.fint.core.model.arkiv.noark

import no.novari.fint.core.model.Bidirectional
import no.novari.fint.core.model.FintAttribute
import no.novari.fint.core.model.FintMultiplicity
import no.novari.fint.core.model.FintRelation
import no.novari.fint.core.model.FintResource
import no.novari.fint.core.model.FintResourceMetadata
import no.novari.fint.core.model.IdentifikatorVisitor
import no.novari.fint.core.model.Link
import no.novari.fint.core.model.felles.kompleksedatatyper.Identifikator

data class Arkivdel(
    val systemId: Identifikator? = null,
    val tittel: String? = null,
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
        override val type = Arkivdel::class
        override val ref = "arkiv-noark:Arkivdel"
        override val path = "arkiv/noark/arkivdel"
        override val idFields = listOf("systemId")
        override val attributes = listOf(
            FintAttribute("systemId", Identifikator::class, list = false, optional = false),
            FintAttribute("tittel", String::class, list = false, optional = false),
        )
        override val relations = listOf(
            FintRelation(
                name = "klassifikasjonssystem",
                target = Klassifikasjonssystem::class,
                targetPath = "arkiv/noark/klassifikasjonssystem",
                multiplicity = FintMultiplicity.ZERO_OR_MORE,
                bidirectional = Bidirectional(inverseName = "arkivdel", isSource = true, inverseMultiplicity = FintMultiplicity.ONE_OR_MORE),
            ),
        )
    }
}
