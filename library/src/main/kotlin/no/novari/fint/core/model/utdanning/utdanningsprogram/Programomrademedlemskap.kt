package no.novari.fint.core.model.utdanning.utdanningsprogram

import no.novari.fint.core.model.Bidirectional
import no.novari.fint.core.model.FintAttribute
import no.novari.fint.core.model.FintMultiplicity
import no.novari.fint.core.model.FintRelation
import no.novari.fint.core.model.FintResource
import no.novari.fint.core.model.FintResourceMetadata
import no.novari.fint.core.model.IdentifikatorVisitor
import no.novari.fint.core.model.Link
import no.novari.fint.core.model.felles.kompleksedatatyper.Identifikator
import no.novari.fint.core.model.felles.kompleksedatatyper.Periode
import no.novari.fint.core.model.utdanning.basisklasser.Gruppemedlemskap
import no.novari.fint.core.model.utdanning.elev.Elevforhold

data class Programomrademedlemskap(
    override var gyldighetsperiode: Periode? = null,
    override var systemId: Identifikator? = null,
) : Gruppemedlemskap, FintResource {
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
        override val type = Programomrademedlemskap::class
        override val ref = "utdanning-utdanningsprogram:Programomrademedlemskap"
        override val path = "utdanning/utdanningsprogram/programomrademedlemskap"
        override val idFields = listOf("systemId")
        override val attributes = listOf(
            FintAttribute("gyldighetsperiode", Periode::class, list = false, optional = true),
            FintAttribute("systemId", Identifikator::class, list = false, optional = false),
        )
        override val relations = listOf(
            FintRelation(
                name = "elevforhold",
                target = Elevforhold::class,
                targetPath = "utdanning/elev/elevforhold",
                multiplicity = FintMultiplicity.EXACTLY_ONE,
                bidirectional = Bidirectional(inverseName = "programomrademedlemskap", isSource = true, inverseMultiplicity = FintMultiplicity.ZERO_OR_MORE),
            ),
            FintRelation(
                name = "programomrade",
                target = Programomrade::class,
                targetPath = "utdanning/utdanningsprogram/programomrade",
                multiplicity = FintMultiplicity.EXACTLY_ONE,
                bidirectional = Bidirectional(inverseName = "gruppemedlemskap", isSource = true, inverseMultiplicity = FintMultiplicity.ZERO_OR_MORE),
            ),
        )
    }
}
