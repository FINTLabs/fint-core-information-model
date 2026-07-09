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
import no.novari.fint.core.model.felles.kompleksedatatyper.Periode
import no.novari.fint.core.model.utdanning.basisklasser.Gruppemedlemskap
import no.novari.fint.core.model.utdanning.elev.Elevforhold
import no.novari.fint.core.model.utdanning.elev.Varsel
import no.novari.fint.core.model.utdanning.kodeverk.Fagmerknad
import no.novari.fint.core.model.utdanning.kodeverk.Fagstatus

data class Faggruppemedlemskap(
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
        override val type = Faggruppemedlemskap::class
        override val ref = "utdanning-timeplan:Faggruppemedlemskap"
        override val path = "utdanning/timeplan/faggruppemedlemskap"
        override val idFields = listOf("systemId")
        override val attributes = listOf(
            FintAttribute("gyldighetsperiode", Periode::class, list = false, optional = true),
            FintAttribute("systemId", Identifikator::class, list = false, optional = false),
        )
        override val relations = listOf(
            FintRelation(
                name = "fagmerknad",
                target = Fagmerknad::class,
                targetPath = "utdanning/kodeverk/fagmerknad",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "fagstatus",
                target = Fagstatus::class,
                targetPath = "utdanning/kodeverk/fagstatus",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "elevforhold",
                target = Elevforhold::class,
                targetPath = "utdanning/elev/elevforhold",
                multiplicity = FintMultiplicity.EXACTLY_ONE,
            ),
            FintRelation(
                name = "varsel",
                target = Varsel::class,
                targetPath = "utdanning/elev/varsel",
                multiplicity = FintMultiplicity.ZERO_OR_MORE,
                bidirectional = Bidirectional(inverseName = "faggruppemedlemskap", isSource = false, inverseMultiplicity = FintMultiplicity.EXACTLY_ONE),
            ),
            FintRelation(
                name = "faggruppe",
                target = Faggruppe::class,
                targetPath = "utdanning/timeplan/faggruppe",
                multiplicity = FintMultiplicity.EXACTLY_ONE,
                bidirectional = Bidirectional(inverseName = "faggruppemedlemskap", isSource = false, inverseMultiplicity = FintMultiplicity.ZERO_OR_MORE),
            ),
        )
    }
}
