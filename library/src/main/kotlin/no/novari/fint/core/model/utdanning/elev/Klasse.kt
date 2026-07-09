package no.novari.fint.core.model.utdanning.elev

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
import no.novari.fint.core.model.utdanning.kodeverk.Termin
import no.novari.fint.core.model.utdanning.utdanningsprogram.Arstrinn
import no.novari.fint.core.model.utdanning.utdanningsprogram.Skole

data class Klasse(
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
        override val type = Klasse::class
        override val ref = "utdanning-elev:Klasse"
        override val path = "utdanning/elev/klasse"
        override val idFields = listOf("systemId")
        override val attributes = listOf(
            FintAttribute("beskrivelse", String::class, list = false, optional = false),
            FintAttribute("navn", String::class, list = false, optional = false),
            FintAttribute("systemId", Identifikator::class, list = false, optional = false),
        )
        override val relations = listOf(
            FintRelation(
                name = "skolear",
                target = Skolear::class,
                targetPath = "utdanning/kodeverk/skolear",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "termin",
                target = Termin::class,
                targetPath = "utdanning/kodeverk/termin",
                multiplicity = FintMultiplicity.ZERO_OR_MORE,
            ),
            FintRelation(
                name = "trinn",
                target = Arstrinn::class,
                targetPath = "utdanning/utdanningsprogram/arstrinn",
                multiplicity = FintMultiplicity.EXACTLY_ONE,
                bidirectional = Bidirectional(inverseName = "klasse", isSource = true, inverseMultiplicity = FintMultiplicity.ZERO_OR_MORE),
            ),
            FintRelation(
                name = "undervisningsforhold",
                target = Undervisningsforhold::class,
                targetPath = "utdanning/elev/undervisningsforhold",
                multiplicity = FintMultiplicity.ZERO_OR_MORE,
                bidirectional = Bidirectional(inverseName = "klasse", isSource = true, inverseMultiplicity = FintMultiplicity.ZERO_OR_MORE),
            ),
            FintRelation(
                name = "skole",
                target = Skole::class,
                targetPath = "utdanning/utdanningsprogram/skole",
                multiplicity = FintMultiplicity.EXACTLY_ONE,
                bidirectional = Bidirectional(inverseName = "klasse", isSource = true, inverseMultiplicity = FintMultiplicity.ZERO_OR_MORE),
            ),
            FintRelation(
                name = "klassemedlemskap",
                target = Klassemedlemskap::class,
                targetPath = "utdanning/elev/klassemedlemskap",
                multiplicity = FintMultiplicity.ZERO_OR_MORE,
                bidirectional = Bidirectional(inverseName = "klasse", isSource = false, inverseMultiplicity = FintMultiplicity.EXACTLY_ONE),
            ),
            FintRelation(
                name = "kontaktlarergruppe",
                target = Kontaktlarergruppe::class,
                targetPath = "utdanning/elev/kontaktlarergruppe",
                multiplicity = FintMultiplicity.ZERO_OR_MORE,
                bidirectional = Bidirectional(inverseName = "klasse", isSource = false, inverseMultiplicity = FintMultiplicity.ONE_OR_MORE),
            ),
        )
    }
}
