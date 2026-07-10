package no.novari.fint.core.model

import java.net.URLDecoder
import java.net.URLEncoder

/**
 * A link to a resource, stored as the id that points it out instead of the
 * full href.
 *
 * @property idField the id field name from the href, e.g. "systemid"
 * @property idValue the id value from the href
 * @property unresolved the original href, kept as-is when it does not follow the FINT id pattern
 */
data class Link(
    val idField: String? = null,
    val idValue: String? = null,
    val unresolved: String? = null,
) {
    /** Builds the full href from [baseUrl], the target's [path] and the stored id. */
    fun href(baseUrl: String, path: String): String =
        unresolved ?: baseUrl.trimEnd('/') + "/" + path + "/" + idField + "/" + encode(idValue.orEmpty())

    companion object {
        /** Parses [href] into an id field and value, using the last two path segments. */
        fun parse(href: String): Link {
            val segments = href.substringAfter("://").split('/').filter { it.isNotEmpty() }
            if (segments.size < 4) return Link(unresolved = href)
            return Link(
                idField = segments[segments.size - 2].lowercase(),
                idValue = decode(segments.last()),
            )
        }

        private fun decode(value: String): String = URLDecoder.decode(value, Charsets.UTF_8)

        private fun encode(value: String): String =
            URLEncoder.encode(value, Charsets.UTF_8).replace("+", "%20")
    }
}
