package no.novari.fint.core.model

interface FintResourceMetadata : FintTypeMetadata {
    val path: String?
    val idFields: List<String>
    val relations: List<FintRelation>

    fun isIdField(name: String): Boolean = idFields.any { it.equals(name, ignoreCase = true) }

    fun relationPath(relationName: String): String? =
        relations.firstOrNull { it.name.equals(relationName, ignoreCase = true) }?.targetPath
}
