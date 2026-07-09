package no.novari.fint.core.model.utdanning.utdanningsprogram

import no.novari.fint.core.model.Bidirectional
import no.novari.fint.core.model.FintAttribute
import no.novari.fint.core.model.FintMultiplicity
import no.novari.fint.core.model.FintRelation
import no.novari.fint.core.model.FintResource
import no.novari.fint.core.model.FintResourceMetadata
import no.novari.fint.core.model.IdentifikatorVisitor
import no.novari.fint.core.model.Link
import no.novari.fint.core.model.administrasjon.organisasjon.Organisasjonselement
import no.novari.fint.core.model.felles.basisklasser.Enhet
import no.novari.fint.core.model.felles.kompleksedatatyper.Adresse
import no.novari.fint.core.model.felles.kompleksedatatyper.Identifikator
import no.novari.fint.core.model.felles.kompleksedatatyper.Kontaktinformasjon
import no.novari.fint.core.model.utdanning.elev.Elevforhold
import no.novari.fint.core.model.utdanning.elev.Klasse
import no.novari.fint.core.model.utdanning.elev.Kontaktlarergruppe
import no.novari.fint.core.model.utdanning.elev.Skoleressurs
import no.novari.fint.core.model.utdanning.elev.Undervisningsforhold
import no.novari.fint.core.model.utdanning.kodeverk.Skoleeiertype
import no.novari.fint.core.model.utdanning.kodeverk.Vigoreferanse
import no.novari.fint.core.model.utdanning.timeplan.Fag
import no.novari.fint.core.model.utdanning.timeplan.Faggruppe
import no.novari.fint.core.model.utdanning.timeplan.Undervisningsgruppe
import no.novari.fint.core.model.utdanning.vurdering.Eksamensgruppe

data class Skole(
    val domenenavn: String? = null,
    val juridiskNavn: String? = null,
    val navn: String? = null,
    val skolenummer: Identifikator? = null,
    val systemId: Identifikator? = null,
    override val forretningsadresse: Adresse? = null,
    override val organisasjonsnavn: String? = null,
    override val organisasjonsnummer: Identifikator? = null,
    override val kontaktinformasjon: Kontaktinformasjon? = null,
    override val postadresse: Adresse? = null,
) : Enhet, FintResource {
    override val links: MutableMap<String, MutableList<Link>> = mutableMapOf()

    override val metadata: FintResourceMetadata get() = Metadata

    override fun visitIdentifikators(visitor: IdentifikatorVisitor) {
        organisasjonsnummer?.identifikatorverdi?.let { visitor.visit("organisasjonsnummer", it) }
        skolenummer?.identifikatorverdi?.let { visitor.visit("skolenummer", it) }
        systemId?.identifikatorverdi?.let { visitor.visit("systemId", it) }
    }

    override fun identifikatorverdi(field: String): String? = when {
        field.equals("organisasjonsnummer", ignoreCase = true) -> organisasjonsnummer?.identifikatorverdi
        field.equals("skolenummer", ignoreCase = true) -> skolenummer?.identifikatorverdi
        field.equals("systemId", ignoreCase = true) -> systemId?.identifikatorverdi
        else -> null
    }

    companion object Metadata : FintResourceMetadata {
        override val type = Skole::class
        override val ref = "utdanning-utdanningsprogram:Skole"
        override val path = "utdanning/utdanningsprogram/skole"
        override val idFields = listOf("organisasjonsnummer", "skolenummer", "systemId")
        override val attributes = listOf(
            FintAttribute("domenenavn", String::class, list = false, optional = true),
            FintAttribute("juridiskNavn", String::class, list = false, optional = true),
            FintAttribute("navn", String::class, list = false, optional = false),
            FintAttribute("skolenummer", Identifikator::class, list = false, optional = false),
            FintAttribute("systemId", Identifikator::class, list = false, optional = false),
            FintAttribute("forretningsadresse", Adresse::class, list = false, optional = true),
            FintAttribute("organisasjonsnavn", String::class, list = false, optional = true),
            FintAttribute("organisasjonsnummer", Identifikator::class, list = false, optional = true),
            FintAttribute("kontaktinformasjon", Kontaktinformasjon::class, list = false, optional = true),
            FintAttribute("postadresse", Adresse::class, list = false, optional = true),
        )
        override val relations = listOf(
            FintRelation(
                name = "organisasjon",
                target = Organisasjonselement::class,
                targetPath = "administrasjon/organisasjon/organisasjonselement",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
                bidirectional = Bidirectional(inverseName = "skole", isSource = true, inverseMultiplicity = FintMultiplicity.ZERO_OR_ONE),
            ),
            FintRelation(
                name = "skoleeierType",
                target = Skoleeiertype::class,
                targetPath = "utdanning/kodeverk/skoleeiertype",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "vigoreferanse",
                target = Vigoreferanse::class,
                targetPath = null,
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "elevforhold",
                target = Elevforhold::class,
                targetPath = "utdanning/elev/elevforhold",
                multiplicity = FintMultiplicity.ZERO_OR_MORE,
                bidirectional = Bidirectional(inverseName = "skole", isSource = false, inverseMultiplicity = FintMultiplicity.EXACTLY_ONE),
            ),
            FintRelation(
                name = "klasse",
                target = Klasse::class,
                targetPath = "utdanning/elev/klasse",
                multiplicity = FintMultiplicity.ZERO_OR_MORE,
                bidirectional = Bidirectional(inverseName = "skole", isSource = false, inverseMultiplicity = FintMultiplicity.EXACTLY_ONE),
            ),
            FintRelation(
                name = "kontaktlarergruppe",
                target = Kontaktlarergruppe::class,
                targetPath = "utdanning/elev/kontaktlarergruppe",
                multiplicity = FintMultiplicity.ZERO_OR_MORE,
                bidirectional = Bidirectional(inverseName = "skole", isSource = false, inverseMultiplicity = FintMultiplicity.EXACTLY_ONE),
            ),
            FintRelation(
                name = "skoleressurs",
                target = Skoleressurs::class,
                targetPath = "utdanning/elev/skoleressurs",
                multiplicity = FintMultiplicity.ZERO_OR_MORE,
                bidirectional = Bidirectional(inverseName = "skole", isSource = false, inverseMultiplicity = FintMultiplicity.ZERO_OR_MORE),
            ),
            FintRelation(
                name = "undervisningsforhold",
                target = Undervisningsforhold::class,
                targetPath = "utdanning/elev/undervisningsforhold",
                multiplicity = FintMultiplicity.ZERO_OR_MORE,
                bidirectional = Bidirectional(inverseName = "skole", isSource = false, inverseMultiplicity = FintMultiplicity.EXACTLY_ONE),
            ),
            FintRelation(
                name = "fag",
                target = Fag::class,
                targetPath = "utdanning/timeplan/fag",
                multiplicity = FintMultiplicity.ZERO_OR_MORE,
                bidirectional = Bidirectional(inverseName = "skole", isSource = false, inverseMultiplicity = FintMultiplicity.ZERO_OR_MORE),
            ),
            FintRelation(
                name = "faggruppe",
                target = Faggruppe::class,
                targetPath = "utdanning/timeplan/faggruppe",
                multiplicity = FintMultiplicity.ZERO_OR_MORE,
                bidirectional = Bidirectional(inverseName = "skole", isSource = false, inverseMultiplicity = FintMultiplicity.ZERO_OR_ONE),
            ),
            FintRelation(
                name = "undervisningsgruppe",
                target = Undervisningsgruppe::class,
                targetPath = "utdanning/timeplan/undervisningsgruppe",
                multiplicity = FintMultiplicity.ZERO_OR_MORE,
                bidirectional = Bidirectional(inverseName = "skole", isSource = false, inverseMultiplicity = FintMultiplicity.EXACTLY_ONE),
            ),
            FintRelation(
                name = "eksamensgruppe",
                target = Eksamensgruppe::class,
                targetPath = "utdanning/vurdering/eksamensgruppe",
                multiplicity = FintMultiplicity.ZERO_OR_MORE,
                bidirectional = Bidirectional(inverseName = "skole", isSource = false, inverseMultiplicity = FintMultiplicity.EXACTLY_ONE),
            ),
            FintRelation(
                name = "utdanningsprogram",
                target = Utdanningsprogram::class,
                targetPath = "utdanning/utdanningsprogram/utdanningsprogram",
                multiplicity = FintMultiplicity.ZERO_OR_MORE,
                bidirectional = Bidirectional(inverseName = "skole", isSource = false, inverseMultiplicity = FintMultiplicity.ZERO_OR_MORE),
            ),
        )
    }
}
