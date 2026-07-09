package no.novari.fint.core.model.ressurs.eiendel

import no.novari.fint.core.model.Bidirectional
import no.novari.fint.core.model.FintAttribute
import no.novari.fint.core.model.FintMultiplicity
import no.novari.fint.core.model.FintRelation
import no.novari.fint.core.model.FintResource
import no.novari.fint.core.model.FintResourceMetadata
import no.novari.fint.core.model.IdentifikatorVisitor
import no.novari.fint.core.model.Link
import no.novari.fint.core.model.felles.kompleksedatatyper.Identifikator
import no.novari.fint.core.model.felles.kompleksedatatyper.Periode
import no.novari.fint.core.model.ressurs.kodeverk.Applikasjonskategori
import no.novari.fint.core.model.ressurs.kodeverk.Plattform

data class Applikasjon(
    val beskrivelse: String? = null,
    val gyldighetsperiode: Periode? = null,
    val navn: String? = null,
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
        override val type = Applikasjon::class
        override val ref = "ressurs-eiendel:Applikasjon"
        override val path = "ressurs/eiendel/applikasjon"
        override val idFields = listOf("systemId")
        override val attributes = listOf(
            FintAttribute("beskrivelse", String::class, list = false, optional = true),
            FintAttribute("gyldighetsperiode", Periode::class, list = false, optional = false),
            FintAttribute("navn", String::class, list = false, optional = false),
            FintAttribute("systemId", Identifikator::class, list = false, optional = false),
        )
        override val relations = listOf(
            FintRelation(
                name = "plattform",
                target = Plattform::class,
                targetPath = "ressurs/kodeverk/plattform",
                multiplicity = FintMultiplicity.ZERO_OR_MORE,
            ),
            FintRelation(
                name = "ressurs",
                target = Applikasjonsressurs::class,
                targetPath = "ressurs/eiendel/applikasjonsressurs",
                multiplicity = FintMultiplicity.ZERO_OR_MORE,
                bidirectional = Bidirectional(inverseName = "applikasjon", isSource = true, inverseMultiplicity = FintMultiplicity.EXACTLY_ONE),
            ),
            FintRelation(
                name = "applikasjonskategori",
                target = Applikasjonskategori::class,
                targetPath = "ressurs/kodeverk/applikasjonskategori",
                multiplicity = FintMultiplicity.ZERO_OR_MORE,
            ),
        )
    }
}
