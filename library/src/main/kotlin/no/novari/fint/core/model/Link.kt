package no.novari.fint.core.model

import java.net.URLDecoder
import java.net.URLEncoder

data class Link(
    var idField: String? = null,
    var idValue: String? = null,
    var unresolved: String? = null,
) {
    fun href(baseUrl: String, path: String): String =
        unresolved ?: baseUrl.trimEnd('/') + "/" + path + "/" + idField + "/" + encode(idValue.orEmpty())

    companion object {
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
