package no.novari.fint.core.model.ressurs.datautstyr

import no.novari.fint.core.model.Bidirectional
import no.novari.fint.core.model.FintAttribute
import no.novari.fint.core.model.FintMultiplicity
import no.novari.fint.core.model.FintRelation
import no.novari.fint.core.model.FintResource
import no.novari.fint.core.model.FintResourceMetadata
import no.novari.fint.core.model.IdentifikatorVisitor
import no.novari.fint.core.model.Link
import no.novari.fint.core.model.administrasjon.organisasjon.Organisasjonselement
import no.novari.fint.core.model.administrasjon.personal.Personalressurs
import no.novari.fint.core.model.felles.kompleksedatatyper.Identifikator
import no.novari.fint.core.model.ressurs.kodeverk.Enhetstype
import no.novari.fint.core.model.ressurs.kodeverk.Plattform
import no.novari.fint.core.model.ressurs.kodeverk.Produsent
import no.novari.fint.core.model.ressurs.kodeverk.Status
import no.novari.fint.core.model.utdanning.elev.Elev

data class DigitalEnhet(
    var dataobjektId: Identifikator? = null,
    var flerbrukerenhet: Boolean? = null,
    var navn: String? = null,
    var privateid: Boolean? = null,
    var serienummer: String? = null,
    var systemId: Identifikator? = null,
) : FintResource {
    override val links: MutableMap<String, MutableList<Link>> = mutableMapOf()

    override val metadata: FintResourceMetadata get() = Metadata

    override fun visitIdentifikators(visitor: IdentifikatorVisitor) {
        dataobjektId?.let { visitor.visit("dataobjektId", it) }
        systemId?.let { visitor.visit("systemId", it) }
    }

    override fun identifikator(field: String): Identifikator? = when {
        field.equals("dataobjektId", ignoreCase = true) -> dataobjektId
        field.equals("systemId", ignoreCase = true) -> systemId
        else -> null
    }

    companion object Metadata : FintResourceMetadata {
        override val type = DigitalEnhet::class
        override val ref = "ressurs-datautstyr:DigitalEnhet"
        override val path = "ressurs/datautstyr/digitalenhet"
        override val idFields = listOf("dataobjektId", "systemId")
        override val attributes = listOf(
            FintAttribute("dataobjektId", Identifikator::class, list = false, optional = true),
            FintAttribute("flerbrukerenhet", Boolean::class, list = false, optional = true),
            FintAttribute("navn", String::class, list = false, optional = true),
            FintAttribute("privateid", Boolean::class, list = false, optional = true),
            FintAttribute("serienummer", String::class, list = false, optional = false),
            FintAttribute("systemId", Identifikator::class, list = false, optional = false),
        )
        override val relations = listOf(
            FintRelation(
                name = "administrator",
                target = Organisasjonselement::class,
                targetPath = "administrasjon/organisasjon/organisasjonselement",
                multiplicity = FintMultiplicity.EXACTLY_ONE,
            ),
            FintRelation(
                name = "eier",
                target = Organisasjonselement::class,
                targetPath = "administrasjon/organisasjon/organisasjonselement",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "personalressurs",
                target = Personalressurs::class,
                targetPath = "administrasjon/personal/personalressurs",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "elev",
                target = Elev::class,
                targetPath = "utdanning/elev/elev",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "status",
                target = Status::class,
                targetPath = "ressurs/kodeverk/status",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "enhetstype",
                target = Enhetstype::class,
                targetPath = "ressurs/kodeverk/enhetstype",
                multiplicity = FintMultiplicity.EXACTLY_ONE,
            ),
            FintRelation(
                name = "plattform",
                target = Plattform::class,
                targetPath = "ressurs/kodeverk/plattform",
                multiplicity = FintMultiplicity.EXACTLY_ONE,
            ),
            FintRelation(
                name = "produsent",
                target = Produsent::class,
                targetPath = "ressurs/kodeverk/produsent",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "enhetsgruppemedlemskap",
                target = Enhetsgruppemedlemskap::class,
                targetPath = "ressurs/datautstyr/enhetsgruppemedlemskap",
                multiplicity = FintMultiplicity.ZERO_OR_MORE,
                bidirectional = Bidirectional(inverseName = "digitalEnhet", isSource = false, inverseMultiplicity = FintMultiplicity.EXACTLY_ONE),
            ),
        )
    }
}
