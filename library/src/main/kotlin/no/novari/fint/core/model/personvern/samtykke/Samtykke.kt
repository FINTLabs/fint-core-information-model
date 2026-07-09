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
import no.novari.fint.core.model.administrasjon.organisasjon.Organisasjonselement
import no.novari.fint.core.model.felles.Person
import no.novari.fint.core.model.felles.kompleksedatatyper.Identifikator
import no.novari.fint.core.model.felles.kompleksedatatyper.Periode

data class Samtykke(
    var gyldighetsperiode: Periode? = null,
    var opprettet: LocalDateTime? = null,
    var systemId: Identifikator? = null,
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
        override val type = Samtykke::class
        override val ref = "personvern-samtykke:Samtykke"
        override val path = "personvern/samtykke/samtykke"
        override val idFields = listOf("systemId")
        override val attributes = listOf(
            FintAttribute("gyldighetsperiode", Periode::class, list = false, optional = false),
            FintAttribute("opprettet", LocalDateTime::class, list = false, optional = false),
            FintAttribute("systemId", Identifikator::class, list = false, optional = false),
        )
        override val relations = listOf(
            FintRelation(
                name = "behandling",
                target = Behandling::class,
                targetPath = "personvern/samtykke/behandling",
                multiplicity = FintMultiplicity.EXACTLY_ONE,
                bidirectional = Bidirectional(inverseName = "samtykke", isSource = true, inverseMultiplicity = FintMultiplicity.ZERO_OR_MORE),
            ),
            FintRelation(
                name = "person",
                target = Person::class,
                targetPath = "felles/person",
                multiplicity = FintMultiplicity.EXACTLY_ONE,
            ),
            FintRelation(
                name = "organisasjonselement",
                target = Organisasjonselement::class,
                targetPath = "administrasjon/organisasjon/organisasjonselement",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
        )
    }
}
