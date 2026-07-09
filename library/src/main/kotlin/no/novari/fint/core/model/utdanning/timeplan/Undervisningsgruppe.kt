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
import no.novari.fint.core.model.utdanning.elev.Undervisningsforhold
import no.novari.fint.core.model.utdanning.kodeverk.Skolear
import no.novari.fint.core.model.utdanning.kodeverk.Termin
import no.novari.fint.core.model.utdanning.utdanningsprogram.Skole

data class Undervisningsgruppe(
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
        override val type = Undervisningsgruppe::class
        override val ref = "utdanning-timeplan:Undervisningsgruppe"
        override val path = "utdanning/timeplan/undervisningsgruppe"
        override val idFields = listOf("systemId")
        override val attributes = listOf(
            FintAttribute("beskrivelse", String::class, list = false, optional = false),
            FintAttribute("navn", String::class, list = false, optional = false),
            FintAttribute("systemId", Identifikator::class, list = false, optional = false),
        )
        override val relations = listOf(
            FintRelation(
                name = "undervisningsforhold",
                target = Undervisningsforhold::class,
                targetPath = "utdanning/elev/undervisningsforhold",
                multiplicity = FintMultiplicity.ZERO_OR_MORE,
                bidirectional = Bidirectional(inverseName = "undervisningsgruppe", isSource = true, inverseMultiplicity = FintMultiplicity.ZERO_OR_MORE),
            ),
            FintRelation(
                name = "fag",
                target = Fag::class,
                targetPath = "utdanning/timeplan/fag",
                multiplicity = FintMultiplicity.ONE_OR_MORE,
                bidirectional = Bidirectional(inverseName = "undervisningsgruppe", isSource = true, inverseMultiplicity = FintMultiplicity.ZERO_OR_MORE),
            ),
            FintRelation(
                name = "termin",
                target = Termin::class,
                targetPath = "utdanning/kodeverk/termin",
                multiplicity = FintMultiplicity.ZERO_OR_MORE,
            ),
            FintRelation(
                name = "skole",
                target = Skole::class,
                targetPath = "utdanning/utdanningsprogram/skole",
                multiplicity = FintMultiplicity.EXACTLY_ONE,
                bidirectional = Bidirectional(inverseName = "undervisningsgruppe", isSource = true, inverseMultiplicity = FintMultiplicity.ZERO_OR_MORE),
            ),
            FintRelation(
                name = "skolear",
                target = Skolear::class,
                targetPath = "utdanning/kodeverk/skolear",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "time",
                target = Time::class,
                targetPath = "utdanning/timeplan/time",
                multiplicity = FintMultiplicity.ZERO_OR_MORE,
                bidirectional = Bidirectional(inverseName = "undervisningsgruppe", isSource = false, inverseMultiplicity = FintMultiplicity.ONE_OR_MORE),
            ),
            FintRelation(
                name = "gruppemedlemskap",
                target = Undervisningsgruppemedlemskap::class,
                targetPath = "utdanning/timeplan/undervisningsgruppemedlemskap",
                multiplicity = FintMultiplicity.ZERO_OR_MORE,
                bidirectional = Bidirectional(inverseName = "undervisningsgruppe", isSource = false, inverseMultiplicity = FintMultiplicity.EXACTLY_ONE),
            ),
        )
    }
}
