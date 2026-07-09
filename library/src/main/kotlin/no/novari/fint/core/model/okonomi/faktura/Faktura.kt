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
import no.novari.fint.core.model.felles.kompleksedatatyper.Adresse
import no.novari.fint.core.model.felles.kompleksedatatyper.Identifikator

data class Faktura(
    var adresse: Adresse? = null,
    var belop: Long? = null,
    var betalt: Boolean? = null,
    var dato: LocalDate? = null,
    var fakturanummer: Identifikator? = null,
    var fakturert: Boolean? = null,
    var forfallsdato: LocalDate? = null,
    var kreditert: Boolean? = null,
    var mottaker: String? = null,
    var restbelop: Long? = null,
) : FintResource {
    override val links: MutableMap<String, MutableList<Link>> = mutableMapOf()

    override val metadata: FintResourceMetadata get() = Metadata

    override fun visitIdentifikators(visitor: IdentifikatorVisitor) {
        visitor.visit("fakturanummer", fakturanummer)
    }

    override fun identifikator(field: String): Identifikator? = when {
        field.equals("fakturanummer", ignoreCase = true) -> fakturanummer
        else -> null
    }

    companion object Metadata : FintResourceMetadata {
        override val type = Faktura::class
        override val ref = "okonomi-faktura:Faktura"
        override val path = "okonomi/faktura/faktura"
        override val idFields = listOf("fakturanummer")
        override val attributes = listOf(
            FintAttribute("adresse", Adresse::class, list = false, optional = true),
            FintAttribute("belop", Long::class, list = false, optional = false),
            FintAttribute("betalt", Boolean::class, list = false, optional = true),
            FintAttribute("dato", LocalDate::class, list = false, optional = false),
            FintAttribute("fakturanummer", Identifikator::class, list = false, optional = false),
            FintAttribute("fakturert", Boolean::class, list = false, optional = true),
            FintAttribute("forfallsdato", LocalDate::class, list = false, optional = false),
            FintAttribute("kreditert", Boolean::class, list = false, optional = true),
            FintAttribute("mottaker", String::class, list = false, optional = false),
            FintAttribute("restbelop", Long::class, list = false, optional = true),
        )
        override val relations = listOf(
            FintRelation(
                name = "fakturagrunnlag",
                target = Fakturagrunnlag::class,
                targetPath = "okonomi/faktura/fakturagrunnlag",
                multiplicity = FintMultiplicity.EXACTLY_ONE,
                bidirectional = Bidirectional(inverseName = "faktura", isSource = false, inverseMultiplicity = FintMultiplicity.ZERO_OR_MORE),
            ),
        )
    }
}
