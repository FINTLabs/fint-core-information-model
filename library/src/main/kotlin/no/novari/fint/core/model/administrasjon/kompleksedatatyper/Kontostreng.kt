package no.novari.fint.core.model.administrasjon.kompleksedatatyper

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
import no.novari.fint.core.model.administrasjon.kodeverk.Prosjektart
import no.novari.fint.core.model.administrasjon.kodeverk.Ramme

class Kontostreng : FintResource {
    override val links: MutableMap<String, MutableList<Link>> = mutableMapOf()

    override val metadata: FintResourceMetadata get() = Metadata

    override fun visitIdentifikators(visitor: IdentifikatorVisitor) {}

    override fun identifikatorverdi(field: String): String? = null

    companion object Metadata : FintResourceMetadata {
        override val type = Kontostreng::class
        override val ref = "administrasjon-kompleksedatatyper:Kontostreng"
        override val path: String? = null
        override val name = "kontostreng"
        override val isCommon = false
        override val idFields = emptyList<String>()
        override val attributes = emptyList<FintAttribute>()
        override val relations = listOf(
            FintRelation(
                name = "aktivitet",
                target = Aktivitet::class,
                targetPath = "administrasjon/kodeverk/aktivitet",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "anlegg",
                target = Anlegg::class,
                targetPath = "administrasjon/kodeverk/anlegg",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "ansvar",
                target = Ansvar::class,
                targetPath = "administrasjon/kodeverk/ansvar",
                multiplicity = FintMultiplicity.EXACTLY_ONE,
            ),
            FintRelation(
                name = "art",
                target = Art::class,
                targetPath = "administrasjon/kodeverk/art",
                multiplicity = FintMultiplicity.EXACTLY_ONE,
            ),
            FintRelation(
                name = "diverse",
                target = Diverse::class,
                targetPath = "administrasjon/kodeverk/diverse",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "formal",
                target = Formal::class,
                targetPath = "administrasjon/kodeverk/formal",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "funksjon",
                target = Funksjon::class,
                targetPath = "administrasjon/kodeverk/funksjon",
                multiplicity = FintMultiplicity.EXACTLY_ONE,
            ),
            FintRelation(
                name = "kontrakt",
                target = Kontrakt::class,
                targetPath = "administrasjon/kodeverk/kontrakt",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "lopenummer",
                target = Lopenummer::class,
                targetPath = "administrasjon/kodeverk/lopenummer",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "objekt",
                target = Objekt::class,
                targetPath = "administrasjon/kodeverk/objekt",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "prosjekt",
                target = Prosjekt::class,
                targetPath = "administrasjon/kodeverk/prosjekt",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "prosjektart",
                target = Prosjektart::class,
                targetPath = "administrasjon/kodeverk/prosjektart",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "ramme",
                target = Ramme::class,
                targetPath = "administrasjon/kodeverk/ramme",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
        )
    }
}
