package no.novari.fint.core.model.utdanning.vurdering

import java.time.LocalDateTime
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
import no.novari.fint.core.model.utdanning.kodeverk.Eksamensform
import no.novari.fint.core.model.utdanning.kodeverk.Skolear
import no.novari.fint.core.model.utdanning.kodeverk.Termin
import no.novari.fint.core.model.utdanning.timeplan.Eksamen
import no.novari.fint.core.model.utdanning.timeplan.Fag
import no.novari.fint.core.model.utdanning.utdanningsprogram.Skole

data class Eksamensgruppe(
    var eksamensdato: LocalDateTime? = null,
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
        override val type = Eksamensgruppe::class
        override val ref = "utdanning-vurdering:Eksamensgruppe"
        override val path = "utdanning/vurdering/eksamensgruppe"
        override val idFields = listOf("systemId")
        override val attributes = listOf(
            FintAttribute("eksamensdato", LocalDateTime::class, list = false, optional = true),
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
                bidirectional = Bidirectional(inverseName = "eksamensgruppe", isSource = true, inverseMultiplicity = FintMultiplicity.ZERO_OR_MORE),
            ),
            FintRelation(
                name = "eksamen",
                target = Eksamen::class,
                targetPath = "utdanning/timeplan/eksamen",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
                bidirectional = Bidirectional(inverseName = "eksamensgruppe", isSource = true, inverseMultiplicity = FintMultiplicity.ZERO_OR_MORE),
            ),
            FintRelation(
                name = "fag",
                target = Fag::class,
                targetPath = "utdanning/timeplan/fag",
                multiplicity = FintMultiplicity.EXACTLY_ONE,
                bidirectional = Bidirectional(inverseName = "eksamensgruppe", isSource = true, inverseMultiplicity = FintMultiplicity.ZERO_OR_MORE),
            ),
            FintRelation(
                name = "skole",
                target = Skole::class,
                targetPath = "utdanning/utdanningsprogram/skole",
                multiplicity = FintMultiplicity.EXACTLY_ONE,
                bidirectional = Bidirectional(inverseName = "eksamensgruppe", isSource = true, inverseMultiplicity = FintMultiplicity.ZERO_OR_MORE),
            ),
            FintRelation(
                name = "termin",
                target = Termin::class,
                targetPath = "utdanning/kodeverk/termin",
                multiplicity = FintMultiplicity.EXACTLY_ONE,
            ),
            FintRelation(
                name = "eksamensform",
                target = Eksamensform::class,
                targetPath = "utdanning/kodeverk/eksamensform",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "skolear",
                target = Skolear::class,
                targetPath = "utdanning/kodeverk/skolear",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "gruppemedlemskap",
                target = Eksamensgruppemedlemskap::class,
                targetPath = "utdanning/vurdering/eksamensgruppemedlemskap",
                multiplicity = FintMultiplicity.ZERO_OR_MORE,
                bidirectional = Bidirectional(inverseName = "eksamensgruppe", isSource = false, inverseMultiplicity = FintMultiplicity.EXACTLY_ONE),
            ),
            FintRelation(
                name = "sensor",
                target = Sensor::class,
                targetPath = "utdanning/vurdering/sensor",
                multiplicity = FintMultiplicity.ZERO_OR_MORE,
                bidirectional = Bidirectional(inverseName = "eksamensgruppe", isSource = false, inverseMultiplicity = FintMultiplicity.EXACTLY_ONE),
            ),
        )
    }
}
