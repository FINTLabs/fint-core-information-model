package no.novari.fint.core.model.arkiv.noark

import no.novari.fint.core.model.Bidirectional
import no.novari.fint.core.model.FintAttribute
import no.novari.fint.core.model.FintMultiplicity
import no.novari.fint.core.model.FintRelation
import no.novari.fint.core.model.FintResource
import no.novari.fint.core.model.FintResourceMetadata
import no.novari.fint.core.model.IdentifikatorVisitor
import no.novari.fint.core.model.Link
import no.novari.fint.core.model.administrasjon.personal.Personalressurs
import no.novari.fint.core.model.felles.kompleksedatatyper.Identifikator

data class Arkivressurs(
    var kildesystemId: Identifikator? = null,
    var systemId: Identifikator? = null,
) : FintResource {
    override val links: MutableMap<String, MutableList<Link>> = mutableMapOf()

    override val metadata: FintResourceMetadata get() = Metadata

    override fun visitIdentifikators(visitor: IdentifikatorVisitor) {
        kildesystemId?.identifikatorverdi?.let { visitor.visit("kildesystemId", it) }
        systemId?.identifikatorverdi?.let { visitor.visit("systemId", it) }
    }

    override fun identifikatorverdi(field: String): String? = when {
        field.equals("kildesystemId", ignoreCase = true) -> kildesystemId?.identifikatorverdi
        field.equals("systemId", ignoreCase = true) -> systemId?.identifikatorverdi
        else -> null
    }

    companion object Metadata : FintResourceMetadata {
        override val type = Arkivressurs::class
        override val ref = "arkiv-noark:Arkivressurs"
        override val path = "arkiv/noark/arkivressurs"
        override val idFields = listOf("kildesystemId", "systemId")
        override val attributes = listOf(
            FintAttribute("kildesystemId", Identifikator::class, list = false, optional = true),
            FintAttribute("systemId", Identifikator::class, list = false, optional = true),
        )
        override val relations = listOf(
            FintRelation(
                name = "personalressurs",
                target = Personalressurs::class,
                targetPath = "administrasjon/personal/personalressurs",
                multiplicity = FintMultiplicity.EXACTLY_ONE,
            ),
            FintRelation(
                name = "autorisasjon",
                target = Autorisasjon::class,
                targetPath = "arkiv/noark/autorisasjon",
                multiplicity = FintMultiplicity.ZERO_OR_MORE,
                bidirectional = Bidirectional(inverseName = "arkivressurs", isSource = true, inverseMultiplicity = FintMultiplicity.ZERO_OR_MORE),
            ),
            FintRelation(
                name = "tilgang",
                target = Tilgang::class,
                targetPath = "arkiv/noark/tilgang",
                multiplicity = FintMultiplicity.ZERO_OR_MORE,
                bidirectional = Bidirectional(inverseName = "arkivressurs", isSource = false, inverseMultiplicity = FintMultiplicity.ZERO_OR_MORE),
            ),
        )
    }
}
