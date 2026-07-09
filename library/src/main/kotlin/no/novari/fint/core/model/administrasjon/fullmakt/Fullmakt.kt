package no.novari.fint.core.model.administrasjon.fullmakt

import no.novari.fint.core.model.Bidirectional
import no.novari.fint.core.model.FintAttribute
import no.novari.fint.core.model.FintMultiplicity
import no.novari.fint.core.model.FintRelation
import no.novari.fint.core.model.FintResource
import no.novari.fint.core.model.FintResourceMetadata
import no.novari.fint.core.model.IdentifikatorVisitor
import no.novari.fint.core.model.Link
import no.novari.fint.core.model.administrasjon.kodeverk.Aktivitet
import no.novari.fint.core.model.administrasjon.kodeverk.Anlegg
import no.novari.fint.core.model.administrasjon.kodeverk.Ansvar
import no.novari.fint.core.model.administrasjon.kodeverk.Art
import no.novari.fint.core.model.administrasjon.kodeverk.Diverse
import no.novari.fint.core.model.administrasjon.kodeverk.Formal
import no.novari.fint.core.model.administrasjon.kodeverk.Funksjon
import no.novari.fint.core.model.administrasjon.kodeverk.Kontrakt
import no.novari.fint.core.model.administrasjon.kodeverk.Lopenummer
import no.novari.fint.core.model.administrasjon.kodeverk.Objekt
import no.novari.fint.core.model.administrasjon.kodeverk.Prosjekt
import no.novari.fint.core.model.administrasjon.kodeverk.Ramme
import no.novari.fint.core.model.administrasjon.organisasjon.Organisasjonselement
import no.novari.fint.core.model.administrasjon.personal.Personalressurs
import no.novari.fint.core.model.felles.kompleksedatatyper.Identifikator
import no.novari.fint.core.model.felles.kompleksedatatyper.Periode

data class Fullmakt(
    val gyldighetsperiode: Periode? = null,
    val systemId: Identifikator? = null,
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
        override val type = Fullmakt::class
        override val ref = "administrasjon-fullmakt:Fullmakt"
        override val path = "administrasjon/fullmakt/fullmakt"
        override val idFields = listOf("systemId")
        override val attributes = listOf(
            FintAttribute("gyldighetsperiode", Periode::class, list = false, optional = false),
            FintAttribute("systemId", Identifikator::class, list = false, optional = false),
        )
        override val relations = listOf(
            FintRelation(
                name = "ramme",
                target = Ramme::class,
                targetPath = "administrasjon/kodeverk/ramme",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "funksjon",
                target = Funksjon::class,
                targetPath = "administrasjon/kodeverk/funksjon",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "objekt",
                target = Objekt::class,
                targetPath = "administrasjon/kodeverk/objekt",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "organisasjonselement",
                target = Organisasjonselement::class,
                targetPath = "administrasjon/organisasjon/organisasjonselement",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "art",
                target = Art::class,
                targetPath = "administrasjon/kodeverk/art",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "anlegg",
                target = Anlegg::class,
                targetPath = "administrasjon/kodeverk/anlegg",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "diverse",
                target = Diverse::class,
                targetPath = "administrasjon/kodeverk/diverse",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "aktivitet",
                target = Aktivitet::class,
                targetPath = "administrasjon/kodeverk/aktivitet",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "ansvar",
                target = Ansvar::class,
                targetPath = "administrasjon/kodeverk/ansvar",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "stedfortreder",
                target = Personalressurs::class,
                targetPath = "administrasjon/personal/personalressurs",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
                bidirectional = Bidirectional(inverseName = "stedfortreder", isSource = true, inverseMultiplicity = FintMultiplicity.ZERO_OR_MORE),
            ),
            FintRelation(
                name = "kontrakt",
                target = Kontrakt::class,
                targetPath = "administrasjon/kodeverk/kontrakt",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "fullmektig",
                target = Personalressurs::class,
                targetPath = "administrasjon/personal/personalressurs",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
                bidirectional = Bidirectional(inverseName = "fullmakt", isSource = true, inverseMultiplicity = FintMultiplicity.ZERO_OR_MORE),
            ),
            FintRelation(
                name = "prosjekt",
                target = Prosjekt::class,
                targetPath = "administrasjon/kodeverk/prosjekt",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "formal",
                target = Formal::class,
                targetPath = "administrasjon/kodeverk/formal",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "rolle",
                target = Rolle::class,
                targetPath = "administrasjon/fullmakt/rolle",
                multiplicity = FintMultiplicity.EXACTLY_ONE,
                bidirectional = Bidirectional(inverseName = "fullmakt", isSource = true, inverseMultiplicity = FintMultiplicity.ONE_OR_MORE),
            ),
            FintRelation(
                name = "lopenummer",
                target = Lopenummer::class,
                targetPath = "administrasjon/kodeverk/lopenummer",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
        )
    }
}
