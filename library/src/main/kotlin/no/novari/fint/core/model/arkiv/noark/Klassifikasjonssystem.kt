package no.novari.fint.core.model.arkiv.noark

import java.time.LocalDateTime
import no.novari.fint.core.model.Bidirectional
import no.novari.fint.core.model.FintAttribute
import no.novari.fint.core.model.FintMultiplicity
import no.novari.fint.core.model.FintRelation
import no.novari.fint.core.model.FintResource
import no.novari.fint.core.model.FintResourceMetadata
import no.novari.fint.core.model.IdentifikatorVisitor
import no.novari.fint.core.model.Link
import no.novari.fint.core.model.arkiv.kodeverk.Klassifikasjonstype
import no.novari.fint.core.model.felles.kompleksedatatyper.Identifikator

data class Klassifikasjonssystem(
    val avsluttetAv: String? = null,
    val avsluttetDato: LocalDateTime? = null,
    val beskrivelse: String? = null,
    val klasse: List<Klasse>? = null,
    val opprettetAv: String? = null,
    val opprettetDato: LocalDateTime? = null,
    val systemId: Identifikator? = null,
    val tittel: String? = null,
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
        override val type = Klassifikasjonssystem::class
        override val ref = "arkiv-noark:Klassifikasjonssystem"
        override val path = "arkiv/noark/klassifikasjonssystem"
        override val name = "klassifikasjonssystem"
        override val isCommon = false
        override val idFields = listOf("systemId")
        override val attributes = listOf(
            FintAttribute("avsluttetAv", String::class, list = false, optional = true),
            FintAttribute("avsluttetDato", LocalDateTime::class, list = false, optional = true),
            FintAttribute("beskrivelse", String::class, list = false, optional = true),
            FintAttribute("klasse", Klasse::class, list = true, optional = false),
            FintAttribute("opprettetAv", String::class, list = false, optional = false),
            FintAttribute("opprettetDato", LocalDateTime::class, list = false, optional = false),
            FintAttribute("systemId", Identifikator::class, list = false, optional = false),
            FintAttribute("tittel", String::class, list = false, optional = false),
        )
        override val relations = listOf(
            FintRelation(
                name = "klassifikasjonstype",
                target = Klassifikasjonstype::class,
                targetPath = "arkiv/kodeverk/klassifikasjonstype",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "arkivdel",
                target = Arkivdel::class,
                targetPath = "arkiv/noark/arkivdel",
                multiplicity = FintMultiplicity.ONE_OR_MORE,
                bidirectional = Bidirectional(inverseName = "klassifikasjonssystem", isSource = false, inverseMultiplicity = FintMultiplicity.ZERO_OR_MORE),
            ),
        )
    }
}
