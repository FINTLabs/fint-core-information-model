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
    var kontaktinformasjon: Kontaktinformasjon? = null,
    var navn: Personnavn? = null,
    var systemId: Identifikator? = null,
    var type: String? = null,
) : FintResource {
    override val links: MutableMap<String, MutableList<Link>> = mutableMapOf()

    override val metadata: FintResourceMetadata get() = Metadata

    override fun visitIdentifikators(visitor: IdentifikatorVisitor) {
        visitor.visit("systemId", systemId)
    }

    override fun identifikator(field: String): Identifikator? = when {
        field.equals("systemId", ignoreCase = true) -> systemId
        else -> null
    }

    companion object Metadata : FintResourceMetadata {
        override val type = Kontaktperson::class
        override val ref = "felles:Kontaktperson"
        override val path = "felles/kontaktperson"
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
                targetPath = "felles/person",
                multiplicity = FintMultiplicity.ZERO_OR_MORE,
                bidirectional = Bidirectional(inverseName = "parorende", isSource = true, inverseMultiplicity = FintMultiplicity.ZERO_OR_MORE),
            ),
        )
    }
}
