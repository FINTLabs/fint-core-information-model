package no.novari.fint.core.model

/**
 * Describes a resource: a type with a REST path, id fields and relations.
 */
interface FintResourceMetadata : FintTypeMetadata {

    /** The REST path, e.g. "utdanning/elev/elev", or null when the resource has no own endpoint. */
    val path: String?

    /** The names of the fields that can identify this resource. */
    val idFields: List<String>

    /** Every relation from this resource, including inherited ones. */
    val relations: List<FintRelation>

    /** True when [name] is one of this resource's id fields. Case does not matter. */
    fun isIdField(name: String): Boolean = idFields.any { it.equals(name, ignoreCase = true) }

    /** The REST path of the resource [relationName] points to, or null. Case does not matter. */
    fun relationPath(relationName: String): String? =
        relations.firstOrNull { it.name.equals(relationName, ignoreCase = true) }?.targetPath
}
