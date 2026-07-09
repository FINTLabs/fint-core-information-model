package no.novari.fint.core.model.okonomi.faktura

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

data class Fakturagrunnlag(
    var avgiftsbelop: Long? = null,
    var fakturalinjer: List<Fakturalinje>? = null,
    var leveringsdato: LocalDate? = null,
    var mottaker: Fakturamottaker? = null,
    var nettobelop: Long? = null,
    var ordrenummer: Identifikator? = null,
    var totalbelop: Long? = null,
) : FintResource {
    override val links: MutableMap<String, MutableList<Link>> = mutableMapOf()

    override val metadata: FintResourceMetadata get() = Metadata

    override fun visitIdentifikators(visitor: IdentifikatorVisitor) {
        visitor.visit("ordrenummer", ordrenummer)
    }

    override fun identifikator(field: String): Identifikator? = when {
        field.equals("ordrenummer", ignoreCase = true) -> ordrenummer
        else -> null
    }

    companion object Metadata : FintResourceMetadata {
        override val type = Fakturagrunnlag::class
        override val ref = "okonomi-faktura:Fakturagrunnlag"
        override val path = "okonomi/faktura/fakturagrunnlag"
        override val idFields = listOf("ordrenummer")
        override val attributes = listOf(
            FintAttribute("avgiftsbelop", Long::class, list = false, optional = true),
            FintAttribute("fakturalinjer", Fakturalinje::class, list = true, optional = false),
            FintAttribute("leveringsdato", LocalDate::class, list = false, optional = true),
            FintAttribute("mottaker", Fakturamottaker::class, list = false, optional = false),
            FintAttribute("nettobelop", Long::class, list = false, optional = true),
            FintAttribute("ordrenummer", Identifikator::class, list = false, optional = false),
            FintAttribute("totalbelop", Long::class, list = false, optional = true),
        )
        override val relations = listOf(
            FintRelation(
                name = "faktura",
                target = Faktura::class,
                targetPath = "okonomi/faktura/faktura",
                multiplicity = FintMultiplicity.ZERO_OR_MORE,
                bidirectional = Bidirectional(inverseName = "fakturagrunnlag", isSource = true, inverseMultiplicity = FintMultiplicity.EXACTLY_ONE),
            ),
            FintRelation(
                name = "fakturautsteder",
                target = Fakturautsteder::class,
                targetPath = "okonomi/faktura/fakturautsteder",
                multiplicity = FintMultiplicity.EXACTLY_ONE,
                bidirectional = Bidirectional(inverseName = "fakturagrunnlag", isSource = true, inverseMultiplicity = FintMultiplicity.ZERO_OR_MORE),
            ),
        )
    }
}
