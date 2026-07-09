package no.novari.fint.core.model.ressurs.eiendel

import no.novari.fint.core.model.Bidirectional
import no.novari.fint.core.model.FintAttribute
import no.novari.fint.core.model.FintMultiplicity
import no.novari.fint.core.model.FintRelation
import no.novari.fint.core.model.FintResource
import no.novari.fint.core.model.FintResourceMetadata
import no.novari.fint.core.model.IdentifikatorVisitor
import no.novari.fint.core.model.Link
import no.novari.fint.core.model.administrasjon.organisasjon.Organisasjonselement
import no.novari.fint.core.model.felles.kompleksedatatyper.Identifikator
import no.novari.fint.core.model.felles.kompleksedatatyper.Periode
import no.novari.fint.core.model.ressurs.kodeverk.Brukertype
import no.novari.fint.core.model.ressurs.kodeverk.Handhevingstype
import no.novari.fint.core.model.ressurs.kodeverk.Lisensmodell

data class Applikasjonsressurs(
    var beskrivelse: String? = null,
    var enhetskostnad: Long? = null,
    var gyldighetsperiode: Periode? = null,
    var kreverGodkjenning: Boolean? = null,
    var lisensantall: Long? = null,
    var navn: String? = null,
    var systemId: Identifikator? = null,
) : FintResource {
    override val links: MutableMap<String, MutableList<Link>> = mutableMapOf()

    override val metadata: FintResourceMetadata get() = Metadata

    override fun visitIdentifikators(visitor: IdentifikatorVisitor) {
        visitor.visit("systemId", systemId)
    }

    override fun identifikator(field: String): Identifikator? = when {
        field.equals("systemId", ignoreCase = true) -> systemId
        else -> null
    }

    companion object Metadata : FintResourceMetadata {
        override val type = Applikasjonsressurs::class
        override val ref = "ressurs-eiendel:Applikasjonsressurs"
        override val path = "ressurs/eiendel/applikasjonsressurs"
        override val idFields = listOf("systemId")
        override val attributes = listOf(
            FintAttribute("beskrivelse", String::class, list = false, optional = true),
            FintAttribute("enhetskostnad", Long::class, list = false, optional = true),
            FintAttribute("gyldighetsperiode", Periode::class, list = false, optional = false),
            FintAttribute("kreverGodkjenning", Boolean::class, list = false, optional = true),
            FintAttribute("lisensantall", Long::class, list = false, optional = true),
            FintAttribute("navn", String::class, list = false, optional = false),
            FintAttribute("systemId", Identifikator::class, list = false, optional = false),
        )
        override val relations = listOf(
            FintRelation(
                name = "brukertype",
                target = Brukertype::class,
                targetPath = "ressurs/kodeverk/brukertype",
                multiplicity = FintMultiplicity.ONE_OR_MORE,
            ),
            FintRelation(
                name = "handhevingstype",
                target = Handhevingstype::class,
                targetPath = "ressurs/kodeverk/handhevingstype",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "lisensmodell",
                target = Lisensmodell::class,
                targetPath = "ressurs/kodeverk/lisensmodell",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "ressurstilgjengelighet",
                target = Applikasjonsressurstilgjengelighet::class,
                targetPath = "ressurs/eiendel/applikasjonsressurstilgjengelighet",
                multiplicity = FintMultiplicity.ZERO_OR_MORE,
                bidirectional = Bidirectional(inverseName = "ressurs", isSource = true, inverseMultiplicity = FintMultiplicity.EXACTLY_ONE),
            ),
            FintRelation(
                name = "eier",
                target = Organisasjonselement::class,
                targetPath = "administrasjon/organisasjon/organisasjonselement",
                multiplicity = FintMultiplicity.EXACTLY_ONE,
            ),
            FintRelation(
                name = "applikasjon",
                target = Applikasjon::class,
                targetPath = "ressurs/eiendel/applikasjon",
                multiplicity = FintMultiplicity.EXACTLY_ONE,
                bidirectional = Bidirectional(inverseName = "ressurs", isSource = false, inverseMultiplicity = FintMultiplicity.ZERO_OR_MORE),
            ),
        )
    }
}
