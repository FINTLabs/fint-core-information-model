package no.novari.fint.core.model.okonomi.regnskap

import java.time.LocalDate
import java.time.LocalDateTime
import no.novari.fint.core.model.Bidirectional
import no.novari.fint.core.model.FintAttribute
import no.novari.fint.core.model.FintMultiplicity
import no.novari.fint.core.model.FintRelation
import no.novari.fint.core.model.FintResource
import no.novari.fint.core.model.FintResourceMetadata
import no.novari.fint.core.model.IdentifikatorVisitor
import no.novari.fint.core.model.Link
import no.novari.fint.core.model.administrasjon.personal.Personalressurs
import no.novari.fint.core.model.felles.kodeverk.Valuta
import no.novari.fint.core.model.felles.kompleksedatatyper.Identifikator

data class Transaksjon(
    val belop: Long? = null,
    val beskrivelse: String? = null,
    val bilag: List<Bilag>? = null,
    val forfallsdato: LocalDate? = null,
    val oppdateringstidspunkt: LocalDateTime? = null,
    val transaksjonsId: Identifikator? = null,
    val transaksjonstidspunkt: LocalDateTime? = null,
) : FintResource {
    override val links: MutableMap<String, MutableList<Link>> = mutableMapOf()

    override val metadata: FintResourceMetadata get() = Metadata

    override fun visitIdentifikators(visitor: IdentifikatorVisitor) {
        transaksjonsId?.identifikatorverdi?.let { visitor.visit("transaksjonsId", it) }
    }

    override fun identifikatorverdi(field: String): String? = when {
        field.equals("transaksjonsId", ignoreCase = true) -> transaksjonsId?.identifikatorverdi
        else -> null
    }

    companion object Metadata : FintResourceMetadata {
        override val type = Transaksjon::class
        override val ref = "okonomi-regnskap:Transaksjon"
        override val path = "okonomi/regnskap/transaksjon"
        override val name = "transaksjon"
        override val isCommon = false
        override val idFields = listOf("transaksjonsId")
        override val attributes = listOf(
            FintAttribute("belop", Long::class, list = false, optional = false),
            FintAttribute("beskrivelse", String::class, list = false, optional = true),
            FintAttribute("bilag", Bilag::class, list = true, optional = true),
            FintAttribute("forfallsdato", LocalDate::class, list = false, optional = false),
            FintAttribute("oppdateringstidspunkt", LocalDateTime::class, list = false, optional = true),
            FintAttribute("transaksjonsId", Identifikator::class, list = false, optional = false),
            FintAttribute("transaksjonstidspunkt", LocalDateTime::class, list = false, optional = true),
        )
        override val relations = listOf(
            FintRelation(
                name = "leverandor",
                target = Leverandor::class,
                targetPath = "okonomi/regnskap/leverandor",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "ansvarlig",
                target = Personalressurs::class,
                targetPath = "administrasjon/personal/personalressurs",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "valuta",
                target = Valuta::class,
                targetPath = "felles/kodeverk/valuta",
                multiplicity = FintMultiplicity.EXACTLY_ONE,
            ),
            FintRelation(
                name = "postering",
                target = Postering::class,
                targetPath = "okonomi/regnskap/postering",
                multiplicity = FintMultiplicity.ONE_OR_MORE,
                bidirectional = Bidirectional(inverseName = "transaksjon", isSource = false, inverseMultiplicity = FintMultiplicity.ZERO_OR_ONE),
            ),
        )
    }
}
