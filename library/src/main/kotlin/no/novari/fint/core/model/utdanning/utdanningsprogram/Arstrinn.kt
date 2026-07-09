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
import no.novari.fint.core.model.utdanning.basisklasser.Gruppe
import no.novari.fint.core.model.utdanning.elev.Klasse
import no.novari.fint.core.model.utdanning.kodeverk.Grepreferanse
import no.novari.fint.core.model.utdanning.kodeverk.Vigoreferanse

data class Arstrinn(
    override var beskrivelse: String? = null,
    override var navn: String? = null,
    override var systemId: Identifikator? = null,
) : Gruppe, FintResource {
    override val links: MutableMap<String, MutableList<Link>> = mutableMapOf()

    override val metadata: FintResourceMetadata get() = Metadata

    override fun visitIdentifikators(visitor: IdentifikatorVisitor) {
        systemId?.let { visitor.visit("systemId", it) }
    }

    override fun identifikator(field: String): Identifikator? = when {
        field.equals("systemId", ignoreCase = true) -> systemId
        else -> null
    }

    companion object Metadata : FintResourceMetadata {
        override val type = Arstrinn::class
        override val ref = "utdanning-utdanningsprogram:Arstrinn"
        override val path = "utdanning/utdanningsprogram/arstrinn"
        override val idFields = listOf("systemId")
        override val attributes = listOf(
            FintAttribute("beskrivelse", String::class, list = false, optional = false),
            FintAttribute("navn", String::class, list = false, optional = false),
            FintAttribute("systemId", Identifikator::class, list = false, optional = false),
        )
        override val relations = listOf(
            FintRelation(
                name = "vigoreferanse",
                target = Vigoreferanse::class,
                targetPath = null,
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "grepreferanse",
                target = Grepreferanse::class,
                targetPath = null,
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "klasse",
                target = Klasse::class,
                targetPath = "utdanning/elev/klasse",
                multiplicity = FintMultiplicity.ZERO_OR_MORE,
                bidirectional = Bidirectional(inverseName = "trinn", isSource = false, inverseMultiplicity = FintMultiplicity.EXACTLY_ONE),
            ),
            FintRelation(
                name = "programomrade",
                target = Programomrade::class,
                targetPath = "utdanning/utdanningsprogram/programomrade",
                multiplicity = FintMultiplicity.ZERO_OR_MORE,
                bidirectional = Bidirectional(inverseName = "trinn", isSource = false, inverseMultiplicity = FintMultiplicity.ZERO_OR_MORE),
            ),
        )
    }
}
