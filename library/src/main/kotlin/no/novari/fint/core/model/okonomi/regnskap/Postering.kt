package no.novari.fint.core.model.okonomi.regnskap

import no.novari.fint.core.model.Bidirectional
import no.novari.fint.core.model.FintAttribute
import no.novari.fint.core.model.FintMultiplicity
import no.novari.fint.core.model.FintRelation
import no.novari.fint.core.model.FintResource
import no.novari.fint.core.model.FintResourceMetadata
import no.novari.fint.core.model.IdentifikatorVisitor
import no.novari.fint.core.model.Link
import no.novari.fint.core.model.administrasjon.kompleksedatatyper.Kontostreng
import no.novari.fint.core.model.felles.kompleksedatatyper.Identifikator

data class Postering(
    var belop: Long? = null,
    var debet: Boolean? = null,
    var kontering: Kontostreng? = null,
    var posteringsId: Identifikator? = null,
) : FintResource {
    override val links: MutableMap<String, MutableList<Link>> = mutableMapOf()

    override val metadata: FintResourceMetadata get() = Metadata

    override fun visitIdentifikators(visitor: IdentifikatorVisitor) {
        posteringsId?.identifikatorverdi?.let { visitor.visit("posteringsId", it) }
    }

    override fun identifikatorverdi(field: String): String? = when {
        field.equals("posteringsId", ignoreCase = true) -> posteringsId?.identifikatorverdi
        else -> null
    }

    companion object Metadata : FintResourceMetadata {
        override val type = Postering::class
        override val ref = "okonomi-regnskap:Postering"
        override val path = "okonomi/regnskap/postering"
        override val idFields = listOf("posteringsId")
        override val attributes = listOf(
            FintAttribute("belop", Long::class, list = false, optional = false),
            FintAttribute("debet", Boolean::class, list = false, optional = false),
            FintAttribute("kontering", Kontostreng::class, list = false, optional = false),
            FintAttribute("posteringsId", Identifikator::class, list = false, optional = false),
        )
        override val relations = listOf(
            FintRelation(
                name = "transaksjon",
                target = Transaksjon::class,
                targetPath = "okonomi/regnskap/transaksjon",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
                bidirectional = Bidirectional(inverseName = "postering", isSource = true, inverseMultiplicity = FintMultiplicity.ONE_OR_MORE),
            ),
        )
    }
}
