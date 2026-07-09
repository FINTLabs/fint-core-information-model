package no.novari.fint.core.model.okonomi.kodeverk

import no.novari.fint.core.model.Bidirectional
import no.novari.fint.core.model.FintAttribute
import no.novari.fint.core.model.FintMultiplicity
import no.novari.fint.core.model.FintRelation
import no.novari.fint.core.model.FintResource
import no.novari.fint.core.model.FintResourceMetadata
import no.novari.fint.core.model.IdentifikatorVisitor
import no.novari.fint.core.model.Link
import no.novari.fint.core.model.administrasjon.kompleksedatatyper.Kontostreng
import no.novari.fint.core.model.felles.basisklasser.Begrep
import no.novari.fint.core.model.felles.kompleksedatatyper.Identifikator
import no.novari.fint.core.model.felles.kompleksedatatyper.Periode
import no.novari.fint.core.model.okonomi.faktura.Fakturautsteder

data class Vare(
    var enhet: String? = null,
    var kontering: Kontostreng? = null,
    var pris: Long? = null,
    override var gyldighetsperiode: Periode? = null,
    override var kode: String? = null,
    override var navn: String? = null,
    override var passiv: Boolean? = null,
    override var systemId: Identifikator? = null,
) : Begrep, FintResource {
    override val links: MutableMap<String, MutableList<Link>> = mutableMapOf()

    override val metadata: FintResourceMetadata get() = Metadata

    override fun visitIdentifikators(visitor: IdentifikatorVisitor) {
        systemId?.let { visitor.visit("systemId", it) }
    }

    override fun identifikator(field: String): Identifikator? = when {
        field.equals("systemId", ignoreCase = true) -> systemId
        else -> null
    }

    companion object Metadata : FintResourceMetadata {
        override val type = Vare::class
        override val ref = "okonomi-kodeverk:Vare"
        override val path = "okonomi/kodeverk/vare"
        override val idFields = listOf("systemId")
        override val attributes = listOf(
            FintAttribute("enhet", String::class, list = false, optional = false),
            FintAttribute("kontering", Kontostreng::class, list = false, optional = true),
            FintAttribute("pris", Long::class, list = false, optional = false),
            FintAttribute("gyldighetsperiode", Periode::class, list = false, optional = true),
            FintAttribute("kode", String::class, list = false, optional = false),
            FintAttribute("navn", String::class, list = false, optional = false),
            FintAttribute("passiv", Boolean::class, list = false, optional = true),
            FintAttribute("systemId", Identifikator::class, list = false, optional = false),
        )
        override val relations = listOf(
            FintRelation(
                name = "fakturautsteder",
                target = Fakturautsteder::class,
                targetPath = "okonomi/faktura/fakturautsteder",
                multiplicity = FintMultiplicity.EXACTLY_ONE,
                bidirectional = Bidirectional(inverseName = "vare", isSource = true, inverseMultiplicity = FintMultiplicity.ZERO_OR_MORE),
            ),
            FintRelation(
                name = "merverdiavgift",
                target = Merverdiavgift::class,
                targetPath = "okonomi/kodeverk/merverdiavgift",
                multiplicity = FintMultiplicity.EXACTLY_ONE,
            ),
        )
    }
}
