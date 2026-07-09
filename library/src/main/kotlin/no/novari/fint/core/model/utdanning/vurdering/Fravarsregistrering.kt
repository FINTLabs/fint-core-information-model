package no.novari.fint.core.model.utdanning.vurdering

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
import no.novari.fint.core.model.utdanning.elev.Skoleressurs
import no.novari.fint.core.model.utdanning.kodeverk.Fravarstype
import no.novari.fint.core.model.utdanning.timeplan.Faggruppe
import no.novari.fint.core.model.utdanning.timeplan.Undervisningsgruppe

data class Fravarsregistrering(
    var foresPaVitnemal: Boolean? = null,
    var kommentar: String? = null,
    var periode: Periode? = null,
    var systemId: Identifikator? = null,
) : FintResource {
    override val links: MutableMap<String, MutableList<Link>> = mutableMapOf()

    override val metadata: FintResourceMetadata get() = Metadata

    override fun visitIdentifikators(visitor: IdentifikatorVisitor) {
        visitor.visit("systemId", systemId)
    }

    override fun identifikator(field: String): Identifikator? = when {
        field.equals("systemId", ignoreCase = true) -> systemId
        else -> null
    }

    companion object Metadata : FintResourceMetadata {
        override val type = Fravarsregistrering::class
        override val ref = "utdanning-vurdering:Fravarsregistrering"
        override val path = "utdanning/vurdering/fravarsregistrering"
        override val idFields = listOf("systemId")
        override val attributes = listOf(
            FintAttribute("foresPaVitnemal", Boolean::class, list = false, optional = false),
            FintAttribute("kommentar", String::class, list = false, optional = true),
            FintAttribute("periode", Periode::class, list = false, optional = false),
            FintAttribute("systemId", Identifikator::class, list = false, optional = false),
        )
        override val relations = listOf(
            FintRelation(
                name = "registrertAv",
                target = Skoleressurs::class,
                targetPath = "utdanning/elev/skoleressurs",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "faggruppe",
                target = Faggruppe::class,
                targetPath = "utdanning/timeplan/faggruppe",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "undervisningsgruppe",
                target = Undervisningsgruppe::class,
                targetPath = "utdanning/timeplan/undervisningsgruppe",
                multiplicity = FintMultiplicity.EXACTLY_ONE,
            ),
            FintRelation(
                name = "fravarstype",
                target = Fravarstype::class,
                targetPath = "utdanning/kodeverk/fravarstype",
                multiplicity = FintMultiplicity.EXACTLY_ONE,
            ),
            FintRelation(
                name = "elevfravar",
                target = Elevfravar::class,
                targetPath = "utdanning/vurdering/elevfravar",
                multiplicity = FintMultiplicity.EXACTLY_ONE,
                bidirectional = Bidirectional(inverseName = "fravarsregistrering", isSource = false, inverseMultiplicity = FintMultiplicity.ZERO_OR_MORE),
            ),
        )
    }
}
