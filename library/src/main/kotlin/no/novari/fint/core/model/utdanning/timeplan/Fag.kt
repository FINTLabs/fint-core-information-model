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
import no.novari.fint.core.model.utdanning.elev.Elevtilrettelegging
import no.novari.fint.core.model.utdanning.kodeverk.Grepreferanse
import no.novari.fint.core.model.utdanning.kodeverk.Vigoreferanse
import no.novari.fint.core.model.utdanning.utdanningsprogram.Programomrade
import no.novari.fint.core.model.utdanning.utdanningsprogram.Skole
import no.novari.fint.core.model.utdanning.vurdering.Eksamensgruppe

data class Fag(
    override val beskrivelse: String? = null,
    override val navn: String? = null,
    override val systemId: Identifikator? = null,
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
        override val type = Fag::class
        override val ref = "utdanning-timeplan:Fag"
        override val path = "utdanning/timeplan/fag"
        override val name = "fag"
        override val isCommon = false
        override val idFields = listOf("systemId")
        override val attributes = listOf(
            FintAttribute("beskrivelse", String::class, list = false, optional = false),
            FintAttribute("navn", String::class, list = false, optional = false),
            FintAttribute("systemId", Identifikator::class, list = false, optional = false),
        )
        override val relations = listOf(
            FintRelation(
                name = "grepreferanse",
                target = Grepreferanse::class,
                targetPath = null,
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "skole",
                target = Skole::class,
                targetPath = "utdanning/utdanningsprogram/skole",
                multiplicity = FintMultiplicity.ZERO_OR_MORE,
                bidirectional = Bidirectional(inverseName = "fag", isSource = true, inverseMultiplicity = FintMultiplicity.ZERO_OR_MORE),
            ),
            FintRelation(
                name = "vigoreferanse",
                target = Vigoreferanse::class,
                targetPath = null,
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "tilrettelegging",
                target = Elevtilrettelegging::class,
                targetPath = "utdanning/elev/elevtilrettelegging",
                multiplicity = FintMultiplicity.ZERO_OR_MORE,
                bidirectional = Bidirectional(inverseName = "fag", isSource = false, inverseMultiplicity = FintMultiplicity.ZERO_OR_ONE),
            ),
            FintRelation(
                name = "programomrade",
                target = Programomrade::class,
                targetPath = "utdanning/utdanningsprogram/programomrade",
                multiplicity = FintMultiplicity.ZERO_OR_MORE,
                bidirectional = Bidirectional(inverseName = "fag", isSource = false, inverseMultiplicity = FintMultiplicity.ZERO_OR_MORE),
            ),
            FintRelation(
                name = "faggruppe",
                target = Faggruppe::class,
                targetPath = "utdanning/timeplan/faggruppe",
                multiplicity = FintMultiplicity.ZERO_OR_MORE,
                bidirectional = Bidirectional(inverseName = "fag", isSource = false, inverseMultiplicity = FintMultiplicity.EXACTLY_ONE),
            ),
            FintRelation(
                name = "undervisningsgruppe",
                target = Undervisningsgruppe::class,
                targetPath = "utdanning/timeplan/undervisningsgruppe",
                multiplicity = FintMultiplicity.ZERO_OR_MORE,
                bidirectional = Bidirectional(inverseName = "fag", isSource = false, inverseMultiplicity = FintMultiplicity.ONE_OR_MORE),
            ),
            FintRelation(
                name = "eksamensgruppe",
                target = Eksamensgruppe::class,
                targetPath = "utdanning/vurdering/eksamensgruppe",
                multiplicity = FintMultiplicity.ZERO_OR_MORE,
                bidirectional = Bidirectional(inverseName = "fag", isSource = false, inverseMultiplicity = FintMultiplicity.EXACTLY_ONE),
            ),
        )
    }
}
