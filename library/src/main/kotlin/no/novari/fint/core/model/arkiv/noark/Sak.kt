package no.novari.fint.core.model.arkiv.noark

import java.time.LocalDateTime
import no.novari.fint.core.model.FintAttribute
import no.novari.fint.core.model.FintMultiplicity
import no.novari.fint.core.model.FintRelation
import no.novari.fint.core.model.FintResourceMetadata
import no.novari.fint.core.model.IdentifikatorVisitor
import no.novari.fint.core.model.Link
import no.novari.fint.core.model.arkiv.kodeverk.Saksmappetype
import no.novari.fint.core.model.arkiv.kodeverk.Saksstatus
import no.novari.fint.core.model.arkiv.kodeverk.Tilgangsgruppe
import no.novari.fint.core.model.felles.kompleksedatatyper.Identifikator

data class Sak(
    override val journalpost: List<Journalpost>? = null,
    override val saksaar: String? = null,
    override val saksdato: LocalDateTime? = null,
    override val sakssekvensnummer: String? = null,
    override val utlaantDato: LocalDateTime? = null,
    override val avsluttetDato: LocalDateTime? = null,
    override val beskrivelse: String? = null,
    override val klasse: List<Klasse>? = null,
    override val mappeId: Identifikator? = null,
    override val merknad: List<Merknad>? = null,
    override val noekkelord: List<String>? = null,
    override val offentligTittel: String? = null,
    override val opprettetDato: LocalDateTime? = null,
    override val part: List<Part>? = null,
    override val skjerming: Skjerming? = null,
    override val systemId: Identifikator? = null,
    override val tittel: String? = null,
) : Saksmappe {
    override val links: MutableMap<String, MutableList<Link>> = mutableMapOf()

    override val metadata: FintResourceMetadata get() = Metadata

    override fun visitIdentifikators(visitor: IdentifikatorVisitor) {
        mappeId?.identifikatorverdi?.let { visitor.visit("mappeId", it) }
        systemId?.identifikatorverdi?.let { visitor.visit("systemId", it) }
    }

    override fun identifikatorverdi(field: String): String? = when {
        field.equals("mappeId", ignoreCase = true) -> mappeId?.identifikatorverdi
        field.equals("systemId", ignoreCase = true) -> systemId?.identifikatorverdi
        else -> null
    }

    companion object Metadata : FintResourceMetadata {
        override val type = Sak::class
        override val ref = "arkiv-noark:Sak"
        override val path = "arkiv/noark/sak"
        override val name = "sak"
        override val isCommon = false
        override val idFields = listOf("mappeId", "systemId")
        override val attributes = listOf(
            FintAttribute("journalpost", Journalpost::class, list = true, optional = true),
            FintAttribute("saksaar", String::class, list = false, optional = true),
            FintAttribute("saksdato", LocalDateTime::class, list = false, optional = true),
            FintAttribute("sakssekvensnummer", String::class, list = false, optional = true),
            FintAttribute("utlaantDato", LocalDateTime::class, list = false, optional = true),
            FintAttribute("avsluttetDato", LocalDateTime::class, list = false, optional = true),
            FintAttribute("beskrivelse", String::class, list = false, optional = true),
            FintAttribute("klasse", Klasse::class, list = true, optional = true),
            FintAttribute("mappeId", Identifikator::class, list = false, optional = true),
            FintAttribute("merknad", Merknad::class, list = true, optional = true),
            FintAttribute("noekkelord", String::class, list = true, optional = true),
            FintAttribute("offentligTittel", String::class, list = false, optional = true),
            FintAttribute("opprettetDato", LocalDateTime::class, list = false, optional = true),
            FintAttribute("part", Part::class, list = true, optional = true),
            FintAttribute("skjerming", Skjerming::class, list = false, optional = true),
            FintAttribute("systemId", Identifikator::class, list = false, optional = true),
            FintAttribute("tittel", String::class, list = false, optional = true),
        )
        override val relations = listOf(
            FintRelation(
                name = "saksmappetype",
                target = Saksmappetype::class,
                targetPath = "arkiv/kodeverk/saksmappetype",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "saksstatus",
                target = Saksstatus::class,
                targetPath = "arkiv/kodeverk/saksstatus",
                multiplicity = FintMultiplicity.EXACTLY_ONE,
            ),
            FintRelation(
                name = "tilgangsgruppe",
                target = Tilgangsgruppe::class,
                targetPath = "arkiv/kodeverk/tilgangsgruppe",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "journalenhet",
                target = AdministrativEnhet::class,
                targetPath = "arkiv/noark/administrativenhet",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "administrativEnhet",
                target = AdministrativEnhet::class,
                targetPath = "arkiv/noark/administrativenhet",
                multiplicity = FintMultiplicity.EXACTLY_ONE,
            ),
            FintRelation(
                name = "saksansvarlig",
                target = Arkivressurs::class,
                targetPath = "arkiv/noark/arkivressurs",
                multiplicity = FintMultiplicity.EXACTLY_ONE,
            ),
            FintRelation(
                name = "arkivdel",
                target = Arkivdel::class,
                targetPath = "arkiv/noark/arkivdel",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "avsluttetAv",
                target = Arkivressurs::class,
                targetPath = "arkiv/noark/arkivressurs",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
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
