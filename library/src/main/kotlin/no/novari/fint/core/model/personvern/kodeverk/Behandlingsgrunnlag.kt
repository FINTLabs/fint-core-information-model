package no.novari.fint.core.model.personvern.kodeverk

import no.novari.fint.core.model.FintAttribute
import no.novari.fint.core.model.FintRelation
import no.novari.fint.core.model.FintResource
import no.novari.fint.core.model.FintResourceMetadata
import no.novari.fint.core.model.IdentifikatorVisitor
import no.novari.fint.core.model.Link
import no.novari.fint.core.model.felles.basisklasser.Begrep
import no.novari.fint.core.model.felles.kompleksedatatyper.Identifikator
import no.novari.fint.core.model.felles.kompleksedatatyper.Periode

data class Behandlingsgrunnlag(
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
        override val type = Behandlingsgrunnlag::class
        override val ref = "personvern-kodeverk:Behandlingsgrunnlag"
        override val path = "personvern/kodeverk/behandlingsgrunnlag"
        override val idFields = listOf("systemId")
        override val attributes = listOf(
            FintAttribute("gyldighetsperiode", Periode::class, list = false, optional = true),
            FintAttribute("kode", String::class, list = false, optional = false),
            FintAttribute("navn", String::class, list = false, optional = false),
            FintAttribute("passiv", Boolean::class, list = false, optional = true),
            FintAttribute("systemId", Identifikator::class, list = false, optional = false),
        )
        override val relations = emptyList<FintRelation>()
    }
}
