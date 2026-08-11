package no.novari.fint.core.model.administrasjon.kodeverk

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

data class Funksjon(
    override val gyldighetsperiode: Periode? = null,
    override val kode: String? = null,
    override val navn: String? = null,
    override val passiv: Boolean? = null,
    override val systemId: Identifikator? = null,
) : Kontodimensjon, FintResource {
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
        override val type = Funksjon::class
        override val ref = "administrasjon-kodeverk:Funksjon"
        override val path = "administrasjon/kodeverk/funksjon"
        override val name = "funksjon"
        override val isCommon = false
        override val idFields = listOf("systemId")
        override val attributes = listOf(
            FintAttribute("gyldighetsperiode", Periode::class, list = false, optional = true),
            FintAttribute("kode", String::class, list = false, optional = false),
            FintAttribute("navn", String::class, list = false, optional = false),
            FintAttribute("passiv", Boolean::class, list = false, optional = true),
            FintAttribute("systemId", Identifikator::class, list = false, optional = false),
        )
        override val relations = listOf(
            FintRelation(
                name = "overordnet",
                target = Funksjon::class,
                targetPath = "administrasjon/kodeverk/funksjon",
                multiplicity = FintMultiplicity.ZERO_OR_ONE,
                bidirectional = Bidirectional(inverseName = "underordnet", isSource = true, inverseMultiplicity = FintMultiplicity.ZERO_OR_MORE),
            ),
            FintRelation(
                name = "underordnet",
                target = Funksjon::class,
                targetPath = "administrasjon/kodeverk/funksjon",
                multiplicity = FintMultiplicity.ZERO_OR_MORE,
                bidirectional = Bidirectional(inverseName = "overordnet", isSource = true, inverseMultiplicity = FintMultiplicity.ZERO_OR_ONE),
            ),
        )
    }
}
