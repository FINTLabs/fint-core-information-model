package no.novari.fint.core.model.utdanning.timeplan

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
import no.novari.fint.core.model.utdanning.kodeverk.Skolear
import no.novari.fint.core.model.utdanning.utdanningsprogram.Skole

data class Faggruppe(
    override var beskrivelse: String? = null,
    override var navn: String? = null,
    override var systemId: Identifikator? = null,
) : Gruppe, FintResource {
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
        override val type = Faggruppe::class
        override val ref = "utdanning-timeplan:Faggruppe"
        override val path = "utdanning/timeplan/faggruppe"
        override val idFields = listOf("systemId")
        override val attributes = listOf(
            FintAttribute("beskrivelse", String::class, list = false, optional = false),
            FintAttribute("navn", String::class, list = false, optional = false),
            FintAttribute("systemId", Identifikator::class, list = false, optional = false),
        )
        override val relations = listOf(
            FintRelation(
                name = "fag",
                target = Fag::class,
                targetPath = "utdanning/timeplan/fag",
                multiplicity = FintMultiplicity.EXACTLY_ONE,
                bidirectional = Bidirectional(inverseName = "faggruppe", isSource = true, inverseMultiplicity = FintMultiplicity.ZERO_OR_MORE),
            ),
            FintRelation(
                name = "skole",
                target = Skole::class,
                targetPath = "utdanning/utdanningsprogram/skole",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
                bidirectional = Bidirectional(inverseName = "faggruppe", isSource = true, inverseMultiplicity = FintMultiplicity.ZERO_OR_MORE),
            ),
            FintRelation(
                name = "skolear",
                target = Skolear::class,
                targetPath = "utdanning/kodeverk/skolear",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "faggruppemedlemskap",
                target = Faggruppemedlemskap::class,
                targetPath = "utdanning/timeplan/faggruppemedlemskap",
                multiplicity = FintMultiplicity.ZERO_OR_MORE,
                bidirectional = Bidirectional(inverseName = "faggruppe", isSource = true, inverseMultiplicity = FintMultiplicity.EXACTLY_ONE),
            ),
        )
    }
}
