package no.novari.fint.core.model.administrasjon.personal

import java.time.LocalDateTime
import no.novari.fint.core.model.Bidirectional
import no.novari.fint.core.model.FintAttribute
import no.novari.fint.core.model.FintMultiplicity
import no.novari.fint.core.model.FintRelation
import no.novari.fint.core.model.FintResourceMetadata
import no.novari.fint.core.model.IdentifikatorVisitor
import no.novari.fint.core.model.Link
import no.novari.fint.core.model.administrasjon.kodeverk.Lonnsart
import no.novari.fint.core.model.administrasjon.kompleksedatatyper.Kontostreng
import no.novari.fint.core.model.felles.kompleksedatatyper.Identifikator
import no.novari.fint.core.model.felles.kompleksedatatyper.Periode

data class Fastlonn(
    var prosent: Long? = null,
    override var anvist: LocalDateTime? = null,
    override var attestert: LocalDateTime? = null,
    override var beskrivelse: String? = null,
    override var kildesystemId: Identifikator? = null,
    override var kontert: LocalDateTime? = null,
    override var kontostreng: Kontostreng? = null,
    override var opptjent: Periode? = null,
    override var periode: Periode? = null,
    override var systemId: Identifikator? = null,
) : Lonn {
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
        override val type = Fastlonn::class
        override val ref = "administrasjon-personal:Fastlonn"
        override val path = "administrasjon/personal/fastlonn"
        override val idFields = listOf("kildesystemId", "systemId")
        override val attributes = listOf(
            FintAttribute("prosent", Long::class, list = false, optional = false),
            FintAttribute("anvist", LocalDateTime::class, list = false, optional = true),
            FintAttribute("attestert", LocalDateTime::class, list = false, optional = true),
            FintAttribute("beskrivelse", String::class, list = false, optional = false),
            FintAttribute("kildesystemId", Identifikator::class, list = false, optional = true),
            FintAttribute("kontert", LocalDateTime::class, list = false, optional = true),
            FintAttribute("kontostreng", Kontostreng::class, list = false, optional = false),
            FintAttribute("opptjent", Periode::class, list = false, optional = true),
            FintAttribute("periode", Periode::class, list = false, optional = false),
            FintAttribute("systemId", Identifikator::class, list = false, optional = true),
        )
        override val relations = listOf(
            FintRelation(
                name = "lonnsart",
                target = Lonnsart::class,
                targetPath = "administrasjon/kodeverk/lonnsart",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "arbeidsforhold",
                target = Arbeidsforhold::class,
                targetPath = "administrasjon/personal/arbeidsforhold",
                multiplicity = FintMultiplicity.EXACTLY_ONE,
                bidirectional = Bidirectional(inverseName = "fastlonn", isSource = true, inverseMultiplicity = FintMultiplicity.ZERO_OR_MORE),
            ),
            FintRelation(
                name = "anviser",
                target = Personalressurs::class,
                targetPath = "administrasjon/personal/personalressurs",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "konterer",
                target = Personalressurs::class,
                targetPath = "administrasjon/personal/personalressurs",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "attestant",
                target = Personalressurs::class,
                targetPath = "administrasjon/personal/personalressurs",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
        )
    }
}
