package no.novari.fint.core.model.utdanning.elev

import java.time.LocalDate
import no.novari.fint.core.model.Bidirectional
import no.novari.fint.core.model.FintAttribute
import no.novari.fint.core.model.FintMultiplicity
import no.novari.fint.core.model.FintRelation
import no.novari.fint.core.model.FintResource
import no.novari.fint.core.model.FintResourceMetadata
import no.novari.fint.core.model.FintResourceVisitor
import no.novari.fint.core.model.IdentifikatorVisitor
import no.novari.fint.core.model.Link
import no.novari.fint.core.model.felles.kompleksedatatyper.Identifikator
import no.novari.fint.core.model.felles.kompleksedatatyper.Periode
import no.novari.fint.core.model.utdanning.basisklasser.Utdanningsforhold
import no.novari.fint.core.model.utdanning.kodeverk.Avbruddsarsak
import no.novari.fint.core.model.utdanning.kodeverk.Elevkategori
import no.novari.fint.core.model.utdanning.kodeverk.Skolear
import no.novari.fint.core.model.utdanning.timeplan.Faggruppemedlemskap
import no.novari.fint.core.model.utdanning.timeplan.Undervisningsgruppemedlemskap
import no.novari.fint.core.model.utdanning.utdanningsprogram.Programomrademedlemskap
import no.novari.fint.core.model.utdanning.utdanningsprogram.Skole
import no.novari.fint.core.model.utdanning.vurdering.Anmerkninger
import no.novari.fint.core.model.utdanning.vurdering.Eksamensgruppemedlemskap
import no.novari.fint.core.model.utdanning.vurdering.Elevfravar
import no.novari.fint.core.model.utdanning.vurdering.Elevvurdering
import no.novari.fint.core.model.utdanning.vurdering.Fravarsoversikt

data class Elevforhold(
    val anmerkninger: List<Anmerkninger>? = null,
    val avbruddsdato: LocalDate? = null,
    val gyldighetsperiode: Periode? = null,
    val hovedskole: Boolean? = null,
    val tosprakligFagopplaring: Boolean? = null,
    override val beskrivelse: String? = null,
    override val systemId: Identifikator? = null,
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

    override fun visitNested(visitor: FintResourceVisitor) {
        anmerkninger?.forEach { visitor.visit("anmerkninger", it) }
    }

    companion object Metadata : FintResourceMetadata {
        override val type = Elevforhold::class
        override val ref = "utdanning-elev:Elevforhold"
        override val path = "utdanning/elev/elevforhold"
        override val name = "elevforhold"
        override val isCommon = false
        override val idFields = listOf("systemId")
        override val attributes = listOf(
            FintAttribute("anmerkninger", Anmerkninger::class, list = true, optional = true),
            FintAttribute("avbruddsdato", LocalDate::class, list = false, optional = true),
            FintAttribute("gyldighetsperiode", Periode::class, list = false, optional = true),
            FintAttribute("hovedskole", Boolean::class, list = false, optional = true),
            FintAttribute("tosprakligFagopplaring", Boolean::class, list = false, optional = true),
            FintAttribute("beskrivelse", String::class, list = false, optional = false),
            FintAttribute("systemId", Identifikator::class, list = false, optional = false),
        )
        override val relations = listOf(
            FintRelation(
                name = "elev",
                target = Elev::class,
                targetPath = "utdanning/elev/elev",
                multiplicity = FintMultiplicity.EXACTLY_ONE,
                bidirectional = Bidirectional(inverseName = "elevforhold", isSource = true, inverseMultiplicity = FintMultiplicity.ZERO_OR_MORE),
            ),
            FintRelation(
                name = "kategori",
                target = Elevkategori::class,
                targetPath = "utdanning/kodeverk/elevkategori",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "skole",
                target = Skole::class,
                targetPath = "utdanning/utdanningsprogram/skole",
                multiplicity = FintMultiplicity.EXACTLY_ONE,
                bidirectional = Bidirectional(inverseName = "elevforhold", isSource = true, inverseMultiplicity = FintMultiplicity.ZERO_OR_MORE),
            ),
            FintRelation(
                name = "avbruddsarsak",
                target = Avbruddsarsak::class,
                targetPath = "utdanning/kodeverk/avbruddsarsak",
                multiplicity = FintMultiplicity.ZERO_OR_MORE,
            ),
            FintRelation(
                name = "fravarsregistreringer",
                target = Elevfravar::class,
                targetPath = "utdanning/vurdering/elevfravar",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
                bidirectional = Bidirectional(inverseName = "elevforhold", isSource = true, inverseMultiplicity = FintMultiplicity.EXACTLY_ONE),
            ),
            FintRelation(
                name = "faggruppemedlemskap",
                target = Faggruppemedlemskap::class,
                targetPath = "utdanning/timeplan/faggruppemedlemskap",
                multiplicity = FintMultiplicity.ZERO_OR_MORE,
                bidirectional = Bidirectional(inverseName = "elevforhold", isSource = true, inverseMultiplicity = FintMultiplicity.EXACTLY_ONE),
            ),
            FintRelation(
                name = "skolear",
                target = Skolear::class,
                targetPath = "utdanning/kodeverk/skolear",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "undervisningsgruppemedlemskap",
                target = Undervisningsgruppemedlemskap::class,
                targetPath = "utdanning/timeplan/undervisningsgruppemedlemskap",
                multiplicity = FintMultiplicity.ZERO_OR_MORE,
                bidirectional = Bidirectional(inverseName = "elevforhold", isSource = false, inverseMultiplicity = FintMultiplicity.EXACTLY_ONE),
            ),
            FintRelation(
                name = "persongruppemedlemskap",
                target = Persongruppemedlemskap::class,
                targetPath = "utdanning/elev/persongruppemedlemskap",
                multiplicity = FintMultiplicity.ZERO_OR_MORE,
                bidirectional = Bidirectional(inverseName = "elevforhold", isSource = false, inverseMultiplicity = FintMultiplicity.ZERO_OR_ONE),
            ),
            FintRelation(
                name = "eksamensgruppemedlemskap",
                target = Eksamensgruppemedlemskap::class,
                targetPath = "utdanning/vurdering/eksamensgruppemedlemskap",
                multiplicity = FintMultiplicity.ZERO_OR_MORE,
                bidirectional = Bidirectional(inverseName = "elevforhold", isSource = false, inverseMultiplicity = FintMultiplicity.EXACTLY_ONE),
            ),
            FintRelation(
                name = "kontaktlarergruppemedlemskap",
                target = Kontaktlarergruppemedlemskap::class,
                targetPath = "utdanning/elev/kontaktlarergruppemedlemskap",
                multiplicity = FintMultiplicity.ZERO_OR_MORE,
                bidirectional = Bidirectional(inverseName = "elevforhold", isSource = false, inverseMultiplicity = FintMultiplicity.EXACTLY_ONE),
            ),
            FintRelation(
                name = "elevfravar",
                target = Fravarsoversikt::class,
                targetPath = "utdanning/vurdering/fravarsoversikt",
                multiplicity = FintMultiplicity.ZERO_OR_MORE,
                bidirectional = Bidirectional(inverseName = "elevforhold", isSource = false, inverseMultiplicity = FintMultiplicity.EXACTLY_ONE),
            ),
            FintRelation(
                name = "tilrettelegging",
                target = Elevtilrettelegging::class,
                targetPath = "utdanning/elev/elevtilrettelegging",
                multiplicity = FintMultiplicity.ZERO_OR_MORE,
                bidirectional = Bidirectional(inverseName = "elev", isSource = false, inverseMultiplicity = FintMultiplicity.EXACTLY_ONE),
            ),
            FintRelation(
                name = "elevvurdering",
                target = Elevvurdering::class,
                targetPath = "utdanning/vurdering/elevvurdering",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
                bidirectional = Bidirectional(inverseName = "elevforhold", isSource = false, inverseMultiplicity = FintMultiplicity.EXACTLY_ONE),
            ),
            FintRelation(
                name = "programomrademedlemskap",
                target = Programomrademedlemskap::class,
                targetPath = "utdanning/utdanningsprogram/programomrademedlemskap",
                multiplicity = FintMultiplicity.ZERO_OR_MORE,
                bidirectional = Bidirectional(inverseName = "elevforhold", isSource = false, inverseMultiplicity = FintMultiplicity.EXACTLY_ONE),
            ),
            FintRelation(
                name = "klassemedlemskap",
                target = Klassemedlemskap::class,
                targetPath = "utdanning/elev/klassemedlemskap",
                multiplicity = FintMultiplicity.ZERO_OR_MORE,
                bidirectional = Bidirectional(inverseName = "elevforhold", isSource = false, inverseMultiplicity = FintMultiplicity.EXACTLY_ONE),
            ),
        )
    }
}
