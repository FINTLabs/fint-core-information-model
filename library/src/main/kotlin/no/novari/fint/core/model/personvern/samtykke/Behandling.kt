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
import no.novari.fint.core.model.personvern.kodeverk.Behandlingsgrunnlag
import no.novari.fint.core.model.personvern.kodeverk.Personopplysning

data class Behandling(
    val aktiv: Boolean? = null,
    val formal: String? = null,
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
        override val type = Behandling::class
        override val ref = "personvern-samtykke:Behandling"
        override val path = "personvern/samtykke/behandling"
        override val name = "behandling"
        override val isCommon = false
        override val idFields = listOf("systemId")
        override val attributes = listOf(
            FintAttribute("aktiv", Boolean::class, list = false, optional = false),
            FintAttribute("formal", String::class, list = false, optional = false),
            FintAttribute("slettet", LocalDateTime::class, list = false, optional = true),
            FintAttribute("systemId", Identifikator::class, list = false, optional = false),
        )
        override val relations = listOf(
            FintRelation(
                name = "behandlingsgrunnlag",
                target = Behandlingsgrunnlag::class,
                targetPath = "personvern/kodeverk/behandlingsgrunnlag",
                multiplicity = FintMultiplicity.EXACTLY_ONE,
            ),
            FintRelation(
                name = "personopplysning",
                target = Personopplysning::class,
                targetPath = "personvern/kodeverk/personopplysning",
                multiplicity = FintMultiplicity.EXACTLY_ONE,
            ),
            FintRelation(
                name = "samtykke",
                target = Samtykke::class,
                targetPath = "personvern/samtykke/samtykke",
                multiplicity = FintMultiplicity.ZERO_OR_MORE,
                bidirectional = Bidirectional(inverseName = "behandling", isSource = false, inverseMultiplicity = FintMultiplicity.EXACTLY_ONE),
            ),
            FintRelation(
                name = "tjeneste",
                target = Tjeneste::class,
                targetPath = "personvern/samtykke/tjeneste",
                multiplicity = FintMultiplicity.EXACTLY_ONE,
                bidirectional = Bidirectional(inverseName = "behandling", isSource = false, inverseMultiplicity = FintMultiplicity.ZERO_OR_MORE),
            ),
        )
    }
}
