package no.novari.fint.core.model.administrasjon.fullmakt

import no.novari.fint.core.model.Bidirectional
import no.novari.fint.core.model.FintAttribute
import no.novari.fint.core.model.FintMultiplicity
import no.novari.fint.core.model.FintRelation
import no.novari.fint.core.model.FintResource
import no.novari.fint.core.model.FintResourceMetadata
import no.novari.fint.core.model.IdentifikatorVisitor
import no.novari.fint.core.model.Link
import no.novari.fint.core.model.felles.kompleksedatatyper.Identifikator

data class Rolle(
    val beskrivelse: String? = null,
    val navn: Identifikator? = null,
) : FintResource {
    override val links: MutableMap<String, MutableList<Link>> = mutableMapOf()

    override val metadata: FintResourceMetadata get() = Metadata

    override fun visitIdentifikators(visitor: IdentifikatorVisitor) {
        navn?.identifikatorverdi?.let { visitor.visit("navn", it) }
    }

    override fun identifikatorverdi(field: String): String? = when {
        field.equals("navn", ignoreCase = true) -> navn?.identifikatorverdi
        else -> null
    }

    companion object Metadata : FintResourceMetadata {
        override val type = Rolle::class
        override val ref = "administrasjon-fullmakt:Rolle"
        override val path = "administrasjon/fullmakt/rolle"
        override val name = "rolle"
        override val isCommon = false
        override val idFields = listOf("navn")
        override val attributes = listOf(
            FintAttribute("beskrivelse", String::class, list = false, optional = false),
            FintAttribute("navn", Identifikator::class, list = false, optional = false),
        )
        override val relations = listOf(
            FintRelation(
                name = "fullmakt",
                target = Fullmakt::class,
                targetPath = "administrasjon/fullmakt/fullmakt",
                multiplicity = FintMultiplicity.ONE_OR_MORE,
                bidirectional = Bidirectional(inverseName = "rolle", isSource = false, inverseMultiplicity = FintMultiplicity.EXACTLY_ONE),
            ),
        )
    }
}
