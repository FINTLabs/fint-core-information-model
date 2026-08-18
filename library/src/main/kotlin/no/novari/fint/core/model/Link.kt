package no.novari.fint.core.model

/**
 * A link to a resource, stored as the id that points it out instead of the
 * full href.
 *
 * Read an href with [FintRelation.resolveLink]. It takes the relation because
 * an id field is only an id field if the target declares it, and nothing in an
 * href alone can say that.
 *
 * This type holds no codec. Hrefs cross the wire percent-encoded, but which
 * codec produced them, and whether an ingress or gateway already decoded, is
 * knowledge the caller has and this library does not — so [FintRelation.resolveLink]
 * never decodes and [href] never encodes. Everything here is the value exactly
 * as it arrived.
 *
 * @property idField the id field name from the href, lowercased
 * @property idValue the id value from the href, exactly as it arrived — still encoded
 * @property unresolved the original href, kept as-is when it names no id field of the target
 */
data class Link(
    val idField: String? = null,
    val idValue: String? = null,
    val unresolved: String? = null,
) {
    /**
     * The href a reader outside the platform follows: [baseUrl], the target's
     * [path], the id field, and [encodedIdValue] as the final segment.
     *
     * The caller encodes, for the reason [Link] gives. Use a path-segment
     * encoder — Spring's UriUtils.encodePathSegment — and not URLEncoder,
     * which writes application/x-www-form-urlencoded: there a space becomes
     * "+", and in a path "+" is a literal plus that decodes back as a space.
     * Only the id value is encoded; [path] and the id field name are
     * structural.
     *
     * Pass [baseUrl] empty for a root-relative href. Unresolved links are
     * emitted verbatim and ignore [encodedIdValue], so null is the right thing
     * to pass for them — it mirrors [idValue].
     */
    fun href(baseUrl: String, path: String, encodedIdValue: String?): String =
        unresolved ?: baseUrl.trimEnd('/') + "/" + path + "/" + idField + "/" + encodedIdValue.orEmpty()
}
