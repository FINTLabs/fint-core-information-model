package no.novari.fint.core.model.utdanning.larling

import java.time.LocalDate
import no.novari.fint.core.model.Bidirectional
import no.novari.fint.core.model.FintAttribute
import no.novari.fint.core.model.FintMultiplicity
import no.novari.fint.core.model.FintRelation
import no.novari.fint.core.model.FintResource
import no.novari.fint.core.model.FintResourceMetadata
import no.novari.fint.core.model.IdentifikatorVisitor
import no.novari.fint.core.model.Link
import no.novari.fint.core.model.felles.kompleksedatatyper.Identifikator
import no.novari.fint.core.model.utdanning.kodeverk.Bevistype
import no.novari.fint.core.model.utdanning.kodeverk.Brevtype
import no.novari.fint.core.model.utdanning.kodeverk.Fullfortkode
import no.novari.fint.core.model.utdanning.kodeverk.Provestatus

data class AvlagtProve(
    var provedato: LocalDate? = null,
    var systemId: Identifikator? = null,
) : FintResource {
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
        override val type = AvlagtProve::class
        override val ref = "utdanning-larling:AvlagtProve"
        override val path = "utdanning/larling/avlagtprove"
        override val idFields = listOf("systemId")
        override val attributes = listOf(
            FintAttribute("provedato", LocalDate::class, list = false, optional = true),
            FintAttribute("systemId", Identifikator::class, list = false, optional = false),
        )
        override val relations = listOf(
            FintRelation(
                name = "provestatus",
                target = Provestatus::class,
                targetPath = "utdanning/kodeverk/provestatus",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "larling",
                target = Larling::class,
                targetPath = "utdanning/larling/larling",
                multiplicity = FintMultiplicity.EXACTLY_ONE,
                bidirectional = Bidirectional(inverseName = "avlagtprove", isSource = true, inverseMultiplicity = FintMultiplicity.ZERO_OR_MORE),
            ),
            FintRelation(
                name = "fullfortkode",
                target = Fullfortkode::class,
                targetPath = "utdanning/kodeverk/fullfortkode",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "brevtype",
                target = Brevtype::class,
                targetPath = "utdanning/kodeverk/brevtype",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "bevistype",
                target = Bevistype::class,
                targetPath = "utdanning/kodeverk/bevistype",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
        )
    }
}
