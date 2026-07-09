package no.novari.fint.core.model.utdanning.vurdering

import java.time.LocalDateTime
import no.novari.fint.core.model.FintAttribute
import no.novari.fint.core.model.FintMultiplicity
import no.novari.fint.core.model.FintRelation
import no.novari.fint.core.model.FintResource
import no.novari.fint.core.model.FintResourceMetadata
import no.novari.fint.core.model.IdentifikatorVisitor
import no.novari.fint.core.model.Link
import no.novari.fint.core.model.felles.kompleksedatatyper.Identifikator
import no.novari.fint.core.model.utdanning.elev.Skoleressurs
import no.novari.fint.core.model.utdanning.kodeverk.Karakterstatus

data class Karakterhistorie(
    var endretDato: LocalDateTime? = null,
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
        override val type = Karakterhistorie::class
        override val ref = "utdanning-vurdering:Karakterhistorie"
        override val path = "utdanning/vurdering/karakterhistorie"
        override val idFields = listOf("systemId")
        override val attributes = listOf(
            FintAttribute("endretDato", LocalDateTime::class, list = false, optional = false),
            FintAttribute("systemId", Identifikator::class, list = false, optional = false),
        )
        override val relations = listOf(
            FintRelation(
                name = "oppdatertAv",
                target = Skoleressurs::class,
                targetPath = "utdanning/elev/skoleressurs",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "opprinneligKarakterverdi",
                target = Karakterverdi::class,
                targetPath = "utdanning/vurdering/karakterverdi",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "opprinneligKarakterstatus",
                target = Karakterstatus::class,
                targetPath = "utdanning/kodeverk/karakterstatus",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "karakterverdi",
                target = Karakterverdi::class,
                targetPath = "utdanning/vurdering/karakterverdi",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "karakterstatus",
                target = Karakterstatus::class,
                targetPath = "utdanning/kodeverk/karakterstatus",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
        )
    }
}
