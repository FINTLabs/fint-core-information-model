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
     * The county-facing href: [baseUrl], the target's [path] and the stored id,
     * with the id value percent-encoded so that it stays a single segment for a
     * reader who has no model to split on. Unresolved links are emitted verbatim.
     */
    fun href(baseUrl: String, path: String): String =
        unresolved ?: baseUrl.trimEnd('/') + "/" + path + "/" + idField + "/" + encode(idValue.orEmpty())

    /**
     * The adapter-facing href: "idfield/idvalue", raw, with nothing encoded —
     * the same form adapters send. Unresolved links are emitted verbatim. Null
     * only for a link that holds neither an id nor an href.
     *
     * Raw is safe here and not toward a county because an adapter knows the id
     * fields of its own resources, so it can find where the id begins in
     * "fodselsnummer/ABC/DEF" exactly as [FintRelation.resolveLink] does. A
     * county client has no model, which is why [href] escapes the id into one
     * segment instead. It also makes this direction lossless: an id read with
     * [FintRelation.resolveLink] and written back out with [idHref] survives
     * the round trip unchanged.
     */
    val idHref: String?
        get() = unresolved ?: idField?.let { field -> idValue?.let { value -> "$field/$value" } }
}

/**
 * Percent-encodes [value] for use as a single path segment.
 *
 * URLEncoder writes application/x-www-form-urlencoded — the HTML form format,
 * where a space is "+". In a path segment "+" is a literal plus, so it has to
 * be rewritten to %20; dropping that rewrite corrupts every id holding a space.
 * The rewrite cannot touch a literal plus, since URLEncoder has already escaped
 * that to %2B. The JDK has no path-segment encoder to use instead: URI leaves
 * "/" unescaped, which is the one character an id value most needs escaped.
 */
private fun encode(value: String): String =
    URLEncoder.encode(value, Charsets.UTF_8).replace("+", "%20")
