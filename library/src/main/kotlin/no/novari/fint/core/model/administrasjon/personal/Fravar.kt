package no.novari.fint.core.model.administrasjon.personal

import java.time.LocalDateTime
import no.novari.fint.core.model.Bidirectional
import no.novari.fint.core.model.FintAttribute
import no.novari.fint.core.model.FintMultiplicity
import no.novari.fint.core.model.FintRelation
import no.novari.fint.core.model.FintResource
import no.novari.fint.core.model.FintResourceMetadata
import no.novari.fint.core.model.IdentifikatorVisitor
import no.novari.fint.core.model.Link
import no.novari.fint.core.model.administrasjon.kodeverk.Fravarsgrunn
import no.novari.fint.core.model.administrasjon.kodeverk.Fravarstype
import no.novari.fint.core.model.felles.kompleksedatatyper.Identifikator
import no.novari.fint.core.model.felles.kompleksedatatyper.Periode

data class Fravar(
    val godkjent: LocalDateTime? = null,
    val kildesystemId: Identifikator? = null,
    val periode: Periode? = null,
    val prosent: Long? = null,
    val systemId: Identifikator? = null,
) : FintResource {
    override val links: MutableMap<String, MutableList<Link>> = mutableMapOf()

    override val metadata: FintResourceMetadata get() = Metadata

    override fun visitIdentifikators(visitor: IdentifikatorVisitor) {
        kildesystemId?.identifikatorverdi?.let { visitor.visit("kildesystemId", it) }
        systemId?.identifikatorverdi?.let { visitor.visit("systemId", it) }
    }

    override fun identifikatorverdi(field: String): String? = when {
        field.equals("kildesystemId", ignoreCase = true) -> kildesystemId?.identifikatorverdi
        field.equals("systemId", ignoreCase = true) -> systemId?.identifikatorverdi
        else -> null
    }

    companion object Metadata : FintResourceMetadata {
        override val type = Fravar::class
        override val ref = "administrasjon-personal:Fravar"
        override val path = "administrasjon/personal/fravar"
        override val name = "fravar"
        override val isCommon = false
        override val idFields = listOf("kildesystemId", "systemId")
        override val attributes = listOf(
            FintAttribute("godkjent", LocalDateTime::class, list = false, optional = true),
            FintAttribute("kildesystemId", Identifikator::class, list = false, optional = true),
            FintAttribute("periode", Periode::class, list = false, optional = false),
            FintAttribute("prosent", Long::class, list = false, optional = false),
            FintAttribute("systemId", Identifikator::class, list = false, optional = true),
        )
        override val relations = listOf(
            FintRelation(
                name = "fravarsgrunn",
                target = Fravarsgrunn::class,
                targetPath = "administrasjon/kodeverk/fravarsgrunn",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "fravarstype",
                target = Fravarstype::class,
                targetPath = "administrasjon/kodeverk/fravarstype",
                multiplicity = FintMultiplicity.EXACTLY_ONE,
            ),
            FintRelation(
                name = "arbeidsforhold",
                target = Arbeidsforhold::class,
                targetPath = "administrasjon/personal/arbeidsforhold",
                multiplicity = FintMultiplicity.ONE_OR_MORE,
                bidirectional = Bidirectional(inverseName = "fravar", isSource = true, inverseMultiplicity = FintMultiplicity.ZERO_OR_MORE),
            ),
            FintRelation(
                name = "fortsettelse",
                target = Fravar::class,
                targetPath = "administrasjon/personal/fravar",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
                bidirectional = Bidirectional(inverseName = "fortsetter", isSource = true, inverseMultiplicity = FintMultiplicity.ZERO_OR_ONE),
            ),
            FintRelation(
                name = "godkjenner",
                target = Personalressurs::class,
                targetPath = "administrasjon/personal/personalressurs",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "fortsetter",
                target = Fravar::class,
                targetPath = "administrasjon/personal/fravar",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
                bidirectional = Bidirectional(inverseName = "fortsettelse", isSource = true, inverseMultiplicity = FintMultiplicity.ZERO_OR_ONE),
            ),
        )
    }
}
