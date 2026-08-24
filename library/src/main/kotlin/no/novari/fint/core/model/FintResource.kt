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

    /**
     * The id field and value of this resource whose value is [value], or null
     * when no id field of it holds that value.
     *
     * Values are matched exactly. An id value is opaque here, so S-1 and s-1
     * are different ids, unlike the field names everywhere else in this
     * interface. When more than one id field holds [value] the first in
     * declared order wins, and only this resource is looked at, never the
     * resources nested below it.
     *
     * The field comes back spelled as the model declares it, the same spelling
     * [visitIdentifikators] hands out. Hrefs carry it lowercased, so lowercase
     * it on the way to the wire as [FintRelation.resolveLink] does on the way
     * in.
     */
    fun idFor(value: String): Pair<String, String>? {
        var found: Pair<String, String>? = null
        visitIdentifikators { field, held -> if (found == null && held == value) found = field to held }
        return found
    }

    /**
     * Calls [visitor] once for every resource held in a field of this one —
     * Personalmappe.journalpost, .part, .skjerming and so on — skipping the
     * fields that are not set. Lists are visited element by element under the
     * field's own name.
     *
     * One level deep: the resources handed to [visitor] are not themselves
     * walked, so call [visitNested] again on each to reach the whole tree.
     * Resources with no such fields never call [visitor].
     */
    fun visitNested(visitor: FintResourceVisitor) {}

    /**
     * The resources held in the fields of this one, in field order. Builds a
     * new list per call — use [visitNested] to walk them without one.
     */
    val nestedResources: List<FintResource>
        get() = buildList { visitNested { _, resource -> add(resource) } }

    /** Returns the links stored under [name], or an empty list. */
    fun relationLinks(name: String): List<Link> = links[name].orEmpty()

    /** Adds [link] under [relation]. */
    fun addLink(relation: String, link: Link) {
        links.getOrPut(relation) { mutableListOf() }.add(link)
    }

    /**
     * Removes the self links from this resource and from every resource held
     * below it, however deep. Nothing else in [links] is touched.
     *
     * A self link says what [metadata] and the id fields already say, so
     * storing one duplicates what is stored anyway — once per id field, and
     * again for every node of a nested tree. Strip them on the way into
     * storage and build them again on the way out.
     */
    fun removeSelfLinks() {
        links.remove(SELF)
        visitNested { _, nested -> nested.removeSelfLinks() }
    }

    companion object {

        /**
         * The relation name a resource's own href travels under. Not a model
         * relation — the wire's word for it, and no model type declares a
         * relation by this name.
         */
        const val SELF: String = "self"
    }
}
