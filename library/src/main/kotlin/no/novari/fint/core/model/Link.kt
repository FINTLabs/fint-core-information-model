package no.novari.fint.core.model

import java.net.URLEncoder

/**
 * A link to a resource, stored as the id that points it out instead of the
 * full href.
 *
 * Read an href with [FintRelation.resolveLink]. It takes the relation because
 * only the target's declared id fields say where the id begins — there is no
 * way to tell from an href alone, and guessing by position truncates id values
 * containing "/" and invents id fields for hrefs that carry none.
 *
 * @property idField the id field name from the href, e.g. "systemid"
 * @property idValue the id value from the href, exactly as it arrived
 * @property unresolved the original href, kept as-is when it names no id field of the target
 */
data class Link(
    val idField: String? = null,
    val idValue: String? = null,
    val unresolved: String? = null,
) {
    /**
     * Builds the full href from [baseUrl], the target's [path] and the stored
     * id, percent-encoding the id value so that it stays a single segment for
     * a reader who has no model to split on. Unresolved links are emitted
     * verbatim.
     */
    fun href(baseUrl: String, path: String): String =
        unresolved ?: baseUrl.trimEnd('/') + "/" + path + "/" + idField + "/" + encode(idValue.orEmpty())
}

private fun encode(value: String): String =
    URLEncoder.encode(value, Charsets.UTF_8).replace("+", "%20")
