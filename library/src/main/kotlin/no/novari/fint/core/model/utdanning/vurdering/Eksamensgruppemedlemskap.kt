package no.novari.fint.core.model.utdanning.vurdering

import no.novari.fint.core.model.Bidirectional
import no.novari.fint.core.model.FintAttribute
import no.novari.fint.core.model.FintMultiplicity
import no.novari.fint.core.model.FintRelation
import no.novari.fint.core.model.FintResource
import no.novari.fint.core.model.FintResourceMetadata
import no.novari.fint.core.model.IdentifikatorVisitor
import no.novari.fint.core.model.Link
import no.novari.fint.core.model.felles.kodeverk.Fylke
import no.novari.fint.core.model.felles.kompleksedatatyper.Identifikator
import no.novari.fint.core.model.felles.kompleksedatatyper.Periode
import no.novari.fint.core.model.utdanning.basisklasser.Gruppemedlemskap
import no.novari.fint.core.model.utdanning.elev.Elevforhold
import no.novari.fint.core.model.utdanning.kodeverk.Betalingsstatus
import no.novari.fint.core.model.utdanning.kodeverk.Karakterstatus
import no.novari.fint.core.model.utdanning.utdanningsprogram.Skole

data class Eksamensgruppemedlemskap(
    var delegert: Boolean? = null,
    var kandidatnummer: String? = null,
    override var gyldighetsperiode: Periode? = null,
    override var systemId: Identifikator? = null,
) : Gruppemedlemskap, FintResource {
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
        override val type = Eksamensgruppemedlemskap::class
        override val ref = "utdanning-vurdering:Eksamensgruppemedlemskap"
        override val path = "utdanning/vurdering/eksamensgruppemedlemskap"
        override val idFields = listOf("systemId")
        override val attributes = listOf(
            FintAttribute("delegert", Boolean::class, list = false, optional = true),
            FintAttribute("kandidatnummer", String::class, list = false, optional = true),
            FintAttribute("gyldighetsperiode", Periode::class, list = false, optional = true),
            FintAttribute("systemId", Identifikator::class, list = false, optional = false),
        )
        override val relations = listOf(
            FintRelation(
                name = "delegertTil",
                target = Fylke::class,
                targetPath = "felles/kodeverk/fylke",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "elevforhold",
                target = Elevforhold::class,
                targetPath = "utdanning/elev/elevforhold",
                multiplicity = FintMultiplicity.EXACTLY_ONE,
                bidirectional = Bidirectional(inverseName = "eksamensgruppemedlemskap", isSource = true, inverseMultiplicity = FintMultiplicity.ZERO_OR_MORE),
            ),
            FintRelation(
                name = "foretrukketSkole",
                target = Skole::class,
                targetPath = "utdanning/utdanningsprogram/skole",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "eksamensgruppe",
                target = Eksamensgruppe::class,
                targetPath = "utdanning/vurdering/eksamensgruppe",
                multiplicity = FintMultiplicity.EXACTLY_ONE,
                bidirectional = Bidirectional(inverseName = "gruppemedlemskap", isSource = true, inverseMultiplicity = FintMultiplicity.ZERO_OR_MORE),
            ),
            FintRelation(
                name = "nus",
                target = Karakterstatus::class,
                targetPath = "utdanning/kodeverk/karakterstatus",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "betalingsstatus",
                target = Betalingsstatus::class,
                targetPath = "utdanning/kodeverk/betalingsstatus",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "foretrukketSensor",
                target = Sensor::class,
                targetPath = "utdanning/vurdering/sensor",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
        )
    }
}
