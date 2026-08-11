package no.novari.fint.core.model.felles

import no.novari.fint.core.model.Bidirectional
import no.novari.fint.core.model.FintAttribute
import no.novari.fint.core.model.FintMultiplicity
import no.novari.fint.core.model.FintRelation
import no.novari.fint.core.model.FintResource
import no.novari.fint.core.model.FintResourceMetadata
import no.novari.fint.core.model.IdentifikatorVisitor
import no.novari.fint.core.model.Link
import no.novari.fint.core.model.felles.kompleksedatatyper.Identifikator
import no.novari.fint.core.model.felles.kompleksedatatyper.Kontaktinformasjon
import no.novari.fint.core.model.felles.kompleksedatatyper.Personnavn

data class Kontaktperson(
    val kontaktinformasjon: Kontaktinformasjon? = null,
    val navn: Personnavn? = null,
    val systemId: Identifikator? = null,
    val type: String? = null,
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
        override val type = Kontaktperson::class
        override val ref = "felles:Kontaktperson"
        override val path: String? = null
        override val name = "kontaktperson"
        override val isCommon = true
        override val idFields = listOf("systemId")
        override val attributes = listOf(
            FintAttribute("kontaktinformasjon", Kontaktinformasjon::class, list = false, optional = true),
            FintAttribute("navn", Personnavn::class, list = false, optional = true),
            FintAttribute("systemId", Identifikator::class, list = false, optional = false),
            FintAttribute("type", String::class, list = false, optional = false),
        )
        override val relations = listOf(
            FintRelation(
                name = "kontaktperson",
                target = Person::class,
                targetPath = null,
                multiplicity = FintMultiplicity.ZERO_OR_MORE,
                bidirectional = Bidirectional(inverseName = "parorende", isSource = true, inverseMultiplicity = FintMultiplicity.ZERO_OR_MORE),
            ),
        )
    }
}
