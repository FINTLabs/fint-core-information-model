package no.novari.fint.core.model

import kotlin.reflect.KClass

/**
 * Describes a resource: a type with id fields, relations and — unless it is
 * common — a REST path of its own.
 */
interface FintResourceMetadata : FintTypeMetadata {

    /** The Kotlin class this metadata belongs to. */
    override val type: KClass<out FintResource>

    /** The REST path, e.g. "utdanning/elev/elev", or null when the resource has no endpoint of its own. */
    val path: String?

    /** This resource's own segment of a path, e.g. "elev". */
    val name: String

    /**
     * True when the resource is served under the domain and package of whoever
     * links to it. felles:Person is reached at "utdanning/elev/person" from
     * utdanning/elev/elev and at "administrasjon/personal/person" from
     * administrasjon/personal/personalressurs, so it has no [path] of its own —
     * build one with [pathIn] or [relationPath].
     */
    val isCommon: Boolean

    /** The names of the fields that can identify this resource. */
    val idFields: List<String>

    /** Every relation from this resource, including inherited ones. */
    val relations: List<FintRelation>

    /** True when [name] is one of this resource's id fields. Case does not matter. */
    fun isIdField(name: String): Boolean = idFields.any { it.equals(name, ignoreCase = true) }

    /** The relation called [relationName], or null. Case does not matter. */
    fun relation(relationName: String): FintRelation? =
        relations.firstOrNull { it.name.equals(relationName, ignoreCase = true) }

    /**
     * The REST path this resource is served at when it is reached through
     * [contextPath]. A common resource takes the domain and package from
     * [contextPath]; every other resource ignores it and answers [path].
     */
    fun pathIn(contextPath: String): String? =
        if (isCommon) domainAndPackageOf(contextPath)?.let { "$it/$name" } else path

    /**
     * The REST path of the resource [relationName] points to, or null when
     * there is no such relation or its target has no path. Common targets are
     * resolved against this resource's own [path]: Elev.Metadata.relationPath("person")
     * is "utdanning/elev/person". Case does not matter.
     */
    fun relationPath(relationName: String): String? = relationPath(relationName, path.orEmpty())

    /**
     * The REST path of the resource [relationName] points to, given that this
     * resource was reached through [contextPath]. Needed when this resource is
     * itself common, and so has no path of its own to resolve the target against.
     */
    fun relationPath(relationName: String, contextPath: String): String? {
        val relation = relation(relationName) ?: return null
        return relation.targetPath ?: (relation.targetMetadata as? FintResourceMetadata)?.pathIn(contextPath)
    }
}

private fun domainAndPackageOf(path: String): String? {
    val segments = path.split('/').filter { it.isNotEmpty() }
    return if (segments.size < 2) null else segments[0] + "/" + segments[1]
}
