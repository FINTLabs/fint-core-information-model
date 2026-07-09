package no.novari.fint.core.model.utdanning.elev

import no.novari.fint.core.model.Bidirectional
import no.novari.fint.core.model.FintAttribute
import no.novari.fint.core.model.FintMultiplicity
import no.novari.fint.core.model.FintRelation
import no.novari.fint.core.model.FintResource
import no.novari.fint.core.model.FintResourceMetadata
import no.novari.fint.core.model.IdentifikatorVisitor
import no.novari.fint.core.model.Link
import no.novari.fint.core.model.administrasjon.personal.Arbeidsforhold
import no.novari.fint.core.model.felles.kompleksedatatyper.Identifikator
import no.novari.fint.core.model.utdanning.basisklasser.Utdanningsforhold
import no.novari.fint.core.model.utdanning.timeplan.Time
import no.novari.fint.core.model.utdanning.timeplan.Undervisningsgruppe
import no.novari.fint.core.model.utdanning.utdanningsprogram.Skole
import no.novari.fint.core.model.utdanning.vurdering.Eksamensgruppe

data class Undervisningsforhold(
    var hovedskole: Boolean? = null,
    override var beskrivelse: String? = null,
    override var systemId: Identifikator? = null,
) : Utdanningsforhold, FintResource {
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
        override val type = Undervisningsforhold::class
        override val ref = "utdanning-elev:Undervisningsforhold"
        override val path = "utdanning/elev/undervisningsforhold"
        override val idFields = listOf("systemId")
        override val attributes = listOf(
            FintAttribute("hovedskole", Boolean::class, list = false, optional = true),
            FintAttribute("beskrivelse", String::class, list = false, optional = false),
            FintAttribute("systemId", Identifikator::class, list = false, optional = false),
        )
        override val relations = listOf(
            FintRelation(
                name = "arbeidsforhold",
                target = Arbeidsforhold::class,
                targetPath = "administrasjon/personal/arbeidsforhold",
                multiplicity = FintMultiplicity.EXACTLY_ONE,
                bidirectional = Bidirectional(inverseName = "undervisningsforhold", isSource = true, inverseMultiplicity = FintMultiplicity.ZERO_OR_ONE),
            ),
            FintRelation(
                name = "time",
                target = Time::class,
                targetPath = "utdanning/timeplan/time",
                multiplicity = FintMultiplicity.ZERO_OR_MORE,
                bidirectional = Bidirectional(inverseName = "undervisningsforhold", isSource = true, inverseMultiplicity = FintMultiplicity.ONE_OR_MORE),
            ),
            FintRelation(
                name = "skole",
                target = Skole::class,
                targetPath = "utdanning/utdanningsprogram/skole",
                multiplicity = FintMultiplicity.EXACTLY_ONE,
                bidirectional = Bidirectional(inverseName = "undervisningsforhold", isSource = true, inverseMultiplicity = FintMultiplicity.ZERO_OR_MORE),
            ),
            FintRelation(
                name = "klasse",
                target = Klasse::class,
                targetPath = "utdanning/elev/klasse",
                multiplicity = FintMultiplicity.ZERO_OR_MORE,
                bidirectional = Bidirectional(inverseName = "undervisningsforhold", isSource = false, inverseMultiplicity = FintMultiplicity.ZERO_OR_MORE),
            ),
            FintRelation(
                name = "kontaktlarergruppe",
                target = Kontaktlarergruppe::class,
                targetPath = "utdanning/elev/kontaktlarergruppe",
                multiplicity = FintMultiplicity.ZERO_OR_MORE,
                bidirectional = Bidirectional(inverseName = "undervisningsforhold", isSource = false, inverseMultiplicity = FintMultiplicity.ZERO_OR_MORE),
            ),
            FintRelation(
                name = "skoleressurs",
                target = Skoleressurs::class,
                targetPath = "utdanning/elev/skoleressurs",
                multiplicity = FintMultiplicity.EXACTLY_ONE,
                bidirectional = Bidirectional(inverseName = "undervisningsforhold", isSource = false, inverseMultiplicity = FintMultiplicity.ZERO_OR_MORE),
            ),
            FintRelation(
                name = "undervisningsgruppe",
                target = Undervisningsgruppe::class,
                targetPath = "utdanning/timeplan/undervisningsgruppe",
                multiplicity = FintMultiplicity.ZERO_OR_MORE,
                bidirectional = Bidirectional(inverseName = "undervisningsforhold", isSource = false, inverseMultiplicity = FintMultiplicity.ZERO_OR_MORE),
            ),
            FintRelation(
                name = "eksamensgruppe",
                target = Eksamensgruppe::class,
                targetPath = "utdanning/vurdering/eksamensgruppe",
                multiplicity = FintMultiplicity.ZERO_OR_MORE,
                bidirectional = Bidirectional(inverseName = "undervisningsforhold", isSource = false, inverseMultiplicity = FintMultiplicity.ZERO_OR_MORE),
            ),
        )
    }
}
