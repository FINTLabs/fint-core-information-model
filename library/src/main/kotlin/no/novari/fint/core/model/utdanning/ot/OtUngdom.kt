package no.novari.fint.core.model.utdanning.ot

import no.novari.fint.core.model.Bidirectional
import no.novari.fint.core.model.FintAttribute
import no.novari.fint.core.model.FintMultiplicity
import no.novari.fint.core.model.FintRelation
import no.novari.fint.core.model.FintResource
import no.novari.fint.core.model.FintResourceMetadata
import no.novari.fint.core.model.IdentifikatorVisitor
import no.novari.fint.core.model.Link
import no.novari.fint.core.model.felles.Person
import no.novari.fint.core.model.felles.kompleksedatatyper.Identifikator
import no.novari.fint.core.model.utdanning.kodeverk.OtEnhet
import no.novari.fint.core.model.utdanning.kodeverk.OtStatus
import no.novari.fint.core.model.utdanning.utdanningsprogram.Programomrade

data class OtUngdom(
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
        override val type = OtUngdom::class
        override val ref = "utdanning-ot:OtUngdom"
        override val path = "utdanning/ot/otungdom"
        override val idFields = listOf("systemId")
        override val attributes = listOf(
            FintAttribute("systemId", Identifikator::class, list = false, optional = false),
        )
        override val relations = listOf(
            FintRelation(
                name = "person",
                target = Person::class,
                targetPath = "felles/person",
                multiplicity = FintMultiplicity.EXACTLY_ONE,
                bidirectional = Bidirectional(inverseName = "otungdom", isSource = true, inverseMultiplicity = FintMultiplicity.ZERO_OR_ONE),
            ),
            FintRelation(
                name = "status",
                target = OtStatus::class,
                targetPath = "utdanning/kodeverk/otstatus",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "enhet",
                target = OtEnhet::class,
                targetPath = "utdanning/kodeverk/otenhet",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "programomrade",
                target = Programomrade::class,
                targetPath = "utdanning/utdanningsprogram/programomrade",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
        )
    }
}
