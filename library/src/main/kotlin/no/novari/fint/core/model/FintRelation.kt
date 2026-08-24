package no.novari.fint.core.model

import kotlin.reflect.KClass

/**
 * A relation from one model type to another.
 *
 * @property name the relation name, as used in links
 * @property target the resource on the other end of the relation, held as a class. A relation
 * points at a resource, never at another relation, so [name] is what the relation itself is
 * called and [targetName] is what the resource it points at is called. Those two names differ
 * for a quarter of the model's relations. [targetPath], [targetIdFields] and [targetMetadata]
 * all describe this same target.
 * @property targetPath the REST path of the target, or null when the target has none of its own —
 * a common resource, a resource served inside another one, or a type outside the model. Build the
 * path for those with [FintResourceMetadata.relationPath].
 * @property multiplicity how many links the model expects on this side
 * @property bidirectional set when the relation goes both ways, null when it only goes one way
 */
data class FintRelation(
    val name: String,
    val target: KClass<out FintObject>,
    val targetPath: String?,
    val multiplicity: FintMultiplicity,
    val bidirectional: Bidirectional? = null,
) {
    /** True when the relation goes both ways. */
    val isBidirectional: Boolean get() = bidirectional != null
}

/** The declared id fields of this relation's target, empty when it has none. */
val FintRelation.targetIdFields: List<String>
    get() = (targetMetadata as? FintResourceMetadata)?.idFields.orEmpty()

/**
 * The name of this relation's target resource, or null when the target is not a
 * resource of its own -- Grepreferanse and Vigoreferanse, the same two whose
 * [targetIdFields] is empty.
 *
 * Not the same as [name]. A relation is named for the role it plays and the
 * target for what it is, so the two part company often: a relation named "elev"
 * has Elevforhold for its target, and "gruppemedlemskap" has four different
 * targets depending on which resource declares it. Read links off a payload
 * with [name]; say which resource is on the other end with this.
 */
val FintRelation.targetName: String?
    get() = (targetMetadata as? FintResourceMetadata)?.name

/**
 * Where this relation's target is served, given that the resource declaring the
 * relation is served at [context]. Null when the target is no resource of its
 * own (Grepreferanse and Vigoreferanse, the same two [targetName] is null for),
 * and null when the target has no serving location of its own because it is
 * served inside another resource.
 *
 * [context] is what a common target is resolved against. felles:Person has no
 * path: it is served under the domain and package of whoever links to it, so
 * Elev's "person" relation answers "utdanning/elev/person", while the same
 * target reached from an administrasjon resource answers under that domain
 * instead. The typed counterpart of [FintResourceMetadata.relationPath].
 */
fun FintRelation.targetIn(context: FintResourceRef): FintResourceRef? =
    (targetMetadata as? FintResourceMetadata)?.refIn(context)

/**
 * Reads [href] into a [Link]: the id value is the last segment, the id field
 * the one before it, validated against [targetIdFields].
 *
 * Hrefs arrive percent-encoded, so an id value is exactly one segment and the
 * id is positional, as in any other URL. The model is not what finds the id —
 * it is what confirms it. An href whose second-to-last segment names none of
 * the target's id fields is kept verbatim in [Link.unresolved] rather than
 * having an id invented for it, and so is one whose target declares no id
 * fields at all, like "https://data.udir.no/kl06/v201906/fagkoder/FSP01-01" —
 * Grepreferanse and Vigoreferanse have none. Absolute and relative hrefs are
 * read the same way.
 *
 * That makes an unencoded href visible rather than repaired. A sender that
 * still writes ".../fodselsnummer/ABC/DEF" resolves to nothing instead of
 * silently yielding "DEF", so the fault surfaces at the sender. To tell a
 * malformed href from one that was never resolvable, check whether the target
 * declares id fields at all:
 *
 *     if (link.unresolved != null && relation.targetIdFields.isNotEmpty()) …
 *
 * [href] is taken exactly as it arrives and [Link.idValue] is returned still
 * encoded: this splits, it never decodes. The split has to happen while the
 * value is encoded, or a "%2F" inside it would already have become a
 * structural "/". Decoding the value segment afterwards is the caller's, since
 * only the caller knows the wire contract — which codec, whether an ingress
 * already decoded, whether a gateway double-encoded. [Link.href] is the
 * mirror: the caller encodes, this never does.
 */
fun FintRelation.resolveLink(href: String): Link {
    val idFields = targetIdFields
    if (idFields.isEmpty()) return Link(unresolved = href)

    val parts = href.substringAfter("://").split('/')
    if (parts.size < 2) return Link(unresolved = href)

    val idField = parts[parts.size - 2]
    val idValue = parts.last()
    if (idValue.isEmpty()) return Link(unresolved = href)
    if (idFields.none { it.equals(idField, ignoreCase = true) }) return Link(unresolved = href)

    return Link(idField = idField.lowercase(), idValue = idValue)
}
