package no.novari.fint.core.model

import no.novari.fint.core.model.felles.kompleksedatatyper.Identifikator

interface FintResource : FintObject {
    val links: MutableMap<String, MutableList<Link>>
    override val metadata: FintResourceMetadata

    fun visitIdentifikators(visitor: IdentifikatorVisitor)

    fun identifikator(field: String): Identifikator?

    fun relationLinks(name: String): List<Link> = links[name].orEmpty()

    fun addLink(relation: String, link: Link) {
        links.getOrPut(relation) { mutableListOf() }.add(link)
    }
}
