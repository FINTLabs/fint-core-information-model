package no.novari.fint.core.model.okonomi.regnskap

import no.novari.fint.core.model.FintAttribute
import no.novari.fint.core.model.FintMultiplicity
import no.novari.fint.core.model.FintRelation
import no.novari.fint.core.model.FintResource
import no.novari.fint.core.model.FintResourceMetadata
import no.novari.fint.core.model.IdentifikatorVisitor
import no.novari.fint.core.model.Link
import no.novari.fint.core.model.felles.Person
import no.novari.fint.core.model.felles.Virksomhet
import no.novari.fint.core.model.felles.kompleksedatatyper.Identifikator

data class Leverandor(
    var kontonummer: String? = null,
    var leverandornummer: Identifikator? = null,
    var systemId: Identifikator? = null,
) : FintResource {
    override val links: MutableMap<String, MutableList<Link>> = mutableMapOf()

    override val metadata: FintResourceMetadata get() = Metadata

    override fun visitIdentifikators(visitor: IdentifikatorVisitor) {
        leverandornummer?.identifikatorverdi?.let { visitor.visit("leverandornummer", it) }
        systemId?.identifikatorverdi?.let { visitor.visit("systemId", it) }
    }

    override fun identifikatorverdi(field: String): String? = when {
        field.equals("leverandornummer", ignoreCase = true) -> leverandornummer?.identifikatorverdi
        field.equals("systemId", ignoreCase = true) -> systemId?.identifikatorverdi
        else -> null
    }

    companion object Metadata : FintResourceMetadata {
        override val type = Leverandor::class
        override val ref = "okonomi-regnskap:Leverandor"
        override val path = "okonomi/regnskap/leverandor"
        override val idFields = listOf("leverandornummer", "systemId")
        override val attributes = listOf(
            FintAttribute("kontonummer", String::class, list = false, optional = true),
            FintAttribute("leverandornummer", Identifikator::class, list = false, optional = true),
            FintAttribute("systemId", Identifikator::class, list = false, optional = false),
        )
        override val relations = listOf(
            FintRelation(
                name = "person",
                target = Person::class,
                targetPath = "felles/person",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "leverandorgruppe",
                target = Leverandorgruppe::class,
                targetPath = "okonomi/regnskap/leverandorgruppe",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "virksomhet",
                target = Virksomhet::class,
                targetPath = "felles/virksomhet",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
        )
    }
}
