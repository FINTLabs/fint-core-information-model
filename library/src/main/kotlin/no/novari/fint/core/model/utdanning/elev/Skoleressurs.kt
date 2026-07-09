package no.novari.fint.core.model.utdanning.elev

import no.novari.fint.core.model.Bidirectional
import no.novari.fint.core.model.FintAttribute
import no.novari.fint.core.model.FintMultiplicity
import no.novari.fint.core.model.FintRelation
import no.novari.fint.core.model.FintResource
import no.novari.fint.core.model.FintResourceMetadata
import no.novari.fint.core.model.IdentifikatorVisitor
import no.novari.fint.core.model.Link
import no.novari.fint.core.model.administrasjon.personal.Personalressurs
import no.novari.fint.core.model.felles.Person
import no.novari.fint.core.model.felles.kompleksedatatyper.Identifikator
import no.novari.fint.core.model.utdanning.utdanningsprogram.Skole
import no.novari.fint.core.model.utdanning.vurdering.Sensor

data class Skoleressurs(
    var feidenavn: Identifikator? = null,
    var systemId: Identifikator? = null,
) : FintResource {
    override val links: MutableMap<String, MutableList<Link>> = mutableMapOf()

    override val metadata: FintResourceMetadata get() = Metadata

    override fun visitIdentifikators(visitor: IdentifikatorVisitor) {
        feidenavn?.let { visitor.visit("feidenavn", it) }
        systemId?.let { visitor.visit("systemId", it) }
    }

    override fun identifikator(field: String): Identifikator? = when {
        field.equals("feidenavn", ignoreCase = true) -> feidenavn
        field.equals("systemId", ignoreCase = true) -> systemId
        else -> null
    }

    companion object Metadata : FintResourceMetadata {
        override val type = Skoleressurs::class
        override val ref = "utdanning-elev:Skoleressurs"
        override val path = "utdanning/elev/skoleressurs"
        override val idFields = listOf("feidenavn", "systemId")
        override val attributes = listOf(
            FintAttribute("feidenavn", Identifikator::class, list = false, optional = true),
            FintAttribute("systemId", Identifikator::class, list = false, optional = false),
        )
        override val relations = listOf(
            FintRelation(
                name = "person",
                target = Person::class,
                targetPath = "felles/person",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
            ),
            FintRelation(
                name = "personalressurs",
                target = Personalressurs::class,
                targetPath = "administrasjon/personal/personalressurs",
                multiplicity = FintMultiplicity.EXACTLY_ONE,
                bidirectional = Bidirectional(inverseName = "skoleressurs", isSource = true, inverseMultiplicity = FintMultiplicity.ZERO_OR_ONE),
            ),
            FintRelation(
                name = "undervisningsforhold",
                target = Undervisningsforhold::class,
                targetPath = "utdanning/elev/undervisningsforhold",
                multiplicity = FintMultiplicity.ZERO_OR_MORE,
                bidirectional = Bidirectional(inverseName = "skoleressurs", isSource = true, inverseMultiplicity = FintMultiplicity.EXACTLY_ONE),
            ),
            FintRelation(
                name = "skole",
                target = Skole::class,
                targetPath = "utdanning/utdanningsprogram/skole",
                multiplicity = FintMultiplicity.ZERO_OR_MORE,
                bidirectional = Bidirectional(inverseName = "skoleressurs", isSource = true, inverseMultiplicity = FintMultiplicity.ZERO_OR_MORE),
            ),
            FintRelation(
                name = "sensor",
                target = Sensor::class,
                targetPath = "utdanning/vurdering/sensor",
                multiplicity = FintMultiplicity.ZERO_OR_MORE,
                bidirectional = Bidirectional(inverseName = "skoleressurs", isSource = false, inverseMultiplicity = FintMultiplicity.EXACTLY_ONE),
            ),
        )
    }
}
