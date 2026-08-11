package no.novari.fint.core.model.personvern.samtykke

import java.time.LocalDateTime
import no.novari.fint.core.model.Bidirectional
import no.novari.fint.core.model.FintAttribute
import no.novari.fint.core.model.FintMultiplicity
import no.novari.fint.core.model.FintRelation
import no.novari.fint.core.model.FintResource
import no.novari.fint.core.model.FintResourceMetadata
import no.novari.fint.core.model.IdentifikatorVisitor
import no.novari.fint.core.model.Link
import no.novari.fint.core.model.felles.kompleksedatatyper.Identifikator

data class Tjeneste(
    val navn: String? = null,
    val slettet: LocalDateTime? = null,
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
        override val type = Tjeneste::class
        override val ref = "personvern-samtykke:Tjeneste"
        override val path = "personvern/samtykke/tjeneste"
        override val name = "tjeneste"
        override val isCommon = false
        override val idFields = listOf("systemId")
        override val attributes = listOf(
            FintAttribute("navn", String::class, list = false, optional = false),
            FintAttribute("slettet", LocalDateTime::class, list = false, optional = true),
            FintAttribute("systemId", Identifikator::class, list = false, optional = false),
        )
        override val relations = listOf(
            FintRelation(
                name = "behandling",
                target = Behandling::class,
                targetPath = "personvern/samtykke/behandling",
                multiplicity = FintMultiplicity.ZERO_OR_MORE,
                bidirectional = Bidirectional(inverseName = "tjeneste", isSource = true, inverseMultiplicity = FintMultiplicity.EXACTLY_ONE),
            ),
        )
    }
}
