package no.novari.fint.core.model

interface FintResource : FintObject {
    val links: MutableMap<String, MutableList<Link>>
    override val metadata: FintResourceMetadata

    fun visitIdentifikators(visitor: IdentifikatorVisitor)

    fun identifikatorverdi(field: String): String?

    fun relationLinks(name: String): List<Link> = links[name].orEmpty()

    fun addLink(relation: String, link: Link) {
        links.getOrPut(relation) { mutableListOf() }.add(link)
    }
}
