package no.novari.fint.core.model

/**
 * A resource from the FINT model: a type that can carry links to other
 * resources.
 *
 * Fields are immutable — the [links] map is the only thing that can change.
 * Note that equals, hashCode and copy() ignore links on purpose.
 */
interface FintResource : FintObject {

    /** Links to related resources, grouped by relation name. */
    val links: MutableMap<String, MutableList<Link>>

    /** Metadata for this resource: its path, id fields and relations. */
    override val metadata: FintResourceMetadata

    /** Calls [visitor] once for every id field that has a value. */
    fun visitIdentifikators(visitor: IdentifikatorVisitor)

    /** Returns the id value for [field], or null when it is not set. Case does not matter. */
    fun identifikatorverdi(field: String): String?

    /** Returns the links stored under [name], or an empty list. */
    fun relationLinks(name: String): List<Link> = links[name].orEmpty()

    /** Adds [link] under [relation]. */
    fun addLink(relation: String, link: Link) {
        links.getOrPut(relation) { mutableListOf() }.add(link)
    }
}
