package no.novari.fint.core.model.arkiv.noark

import java.time.LocalDateTime
import no.novari.fint.core.model.FintAttribute
import no.novari.fint.core.model.FintMultiplicity
import no.novari.fint.core.model.FintRelation
import no.novari.fint.core.model.FintResource
import no.novari.fint.core.model.FintResourceMetadata
import no.novari.fint.core.model.IdentifikatorVisitor
import no.novari.fint.core.model.Link
import no.novari.fint.core.model.arkiv.kodeverk.DokumentStatus
import no.novari.fint.core.model.arkiv.kodeverk.DokumentType
import no.novari.fint.core.model.arkiv.kodeverk.TilknyttetRegistreringSom

data class Dokumentbeskrivelse(
    val beskrivelse: String? = null,
    val dokumentnummer: Long? = null,
    val dokumentobjekt: List<Dokumentobjekt>? = null,
    val forfatter: List<String>? = null,
    val opprettetDato: LocalDateTime? = null,
    val part: List<Part>? = null,
    val referanseArkivdel: List<String>? = null,
    val skjerming: Skjerming? = null,
    val tilknyttetDato: LocalDateTime? = null,
    val tittel: String? = null,
) : FintResource {
    override val links: MutableMap<String, MutableList<Link>> = mutableMapOf()

    override val metadata: FintResourceMetadata get() = Metadata

    override fun visitIdentifikators(visitor: IdentifikatorVisitor) {}

    override fun identifikatorverdi(field: String): String? = null

    companion object Metadata : FintResourceMetadata {
        override val type = Dokumentbeskrivelse::class
        override val ref = "arkiv-noark:Dokumentbeskrivelse"
        override val path: String? = null
        override val name = "dokumentbeskrivelse"
        override val isCommon = false
        override val idFields = emptyList<String>()
        override val attributes = listOf(
            FintAttribute("beskrivelse", String::class, list = false, optional = true),
            FintAttribute("dokumentnummer", Long::class, list = false, optional = true),
            FintAttribute("dokumentobjekt", Dokumentobjekt::class, list = true, optional = true),
            FintAttribute("forfatter", String::class, list = true, optional = true),
            FintAttribute("opprettetDato", LocalDateTime::class, list = false, optional = true),
            FintAttribute("part", Part::class, list = true, optional = true),
            FintAttribute("referanseArkivdel", String::class, list = true, optional = true),
            FintAttribute("skjerming", Skjerming::class, list = false, optional = true),
            FintAttribute("tilknyttetDato", LocalDateTime::class, list = false, optional = true),
            FintAttribute("tittel", String::class, list = false, optional = false),
        )
        override val relations = listOf(
            FintRelation(
                name = "dokumentstatus",
                target = DokumentStatus::class,
                targetPath = "arkiv/kodeverk/dokumentstatus",
                multiplicity = FintMultiplicity.EXACTLY_ONE,
            ),
            FintRelation(
                name = "dokumentType",
                target = DokumentType::class,
                targetPath = "arkiv/kodeverk/dokumenttype",
                multiplicity = FintMultiplicity.EXACTLY_ONE,
            ),
            FintRelation(
                name = "tilknyttetRegistreringSom",
                target = TilknyttetRegistreringSom::class,
                targetPath = "arkiv/kodeverk/tilknyttetregistreringsom",
                multiplicity = FintMultiplicity.ONE_OR_MORE,
            ),
            FintRelation(
                name = "tilknyttetAv",
                target = Arkivressurs::class,
                targetPath = "arkiv/noark/arkivressurs",
                multiplicity = FintMultiplicity.EXACTLY_ONE,
            ),
            FintRelation(
                name = "opprettetAv",
                target = Arkivressurs::class,
                targetPath = "arkiv/noark/arkivressurs",
                multiplicity = FintMultiplicity.EXACTLY_ONE,
            ),
        )
    }
}
