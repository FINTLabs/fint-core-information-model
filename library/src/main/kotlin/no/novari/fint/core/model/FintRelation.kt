package no.novari.fint.core.model

import kotlin.reflect.KClass

/**
 * A relation from one model type to another.
 *
 * @property name the relation name, as used in links
 * @property target the class the relation points to
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

/**
 * Reads [href] into a [Link] using the id fields of this relation's target.
 *
 * The id field is found by name, not by position, so an id value that itself
 * contains slashes survives whole: ".../person/fodselsnummer/ABC/DEF" keeps
 * "ABC/DEF". An href that names none of the target's id fields is kept verbatim
 * in [Link.unresolved] rather than having an id invented for it, which is what
 * happens to a reference like "https://data.udir.no/kl06/v201906/fagkoder/FSP01-01" —
 * Grepreferanse and Vigoreferanse have no id fields at all. Absolute and
 * relative hrefs are read the same way.
 *
 * [href] is taken exactly as it arrives: nothing is decoded. That asymmetry
 * with [Link.href], which encodes, is deliberate. Inbound we hold the model, so
 * we know where the id begins and can split a raw href safely. Outbound the
 * county client reading the href has no model to split on, so the id value is
 * percent-encoded to keep it one segment. Adapters send raw hrefs; an adapter
 * that percent-encodes instead will have its escapes stored literally and
 * encoded again on the way out.
 */
fun FintRelation.resolveLink(href: String): Link {
    val idFields = (targetMetadata as? FintResourceMetadata)?.idFields.orEmpty()
    if (idFields.isEmpty()) return Link(unresolved = href)

    val parts = href.substringAfter("://").split('/')
    val index = parts.indexOfFirst { part -> idFields.any { it.equals(part, ignoreCase = true) } }
    if (index < 0) return Link(unresolved = href)

    val idValue = parts.subList(index + 1, parts.size).joinToString("/")
    if (idValue.isEmpty()) return Link(unresolved = href)

    return Link(idField = parts[index].lowercase(), idValue = idValue)
}
