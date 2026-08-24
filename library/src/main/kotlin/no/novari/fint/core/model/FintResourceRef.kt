package no.novari.fint.core.model

/**
 * Where a resource is served, as the three segments of a REST path: the
 * "utdanning", "elev" and "elev" of "utdanning/elev/elev".
 *
 * Not the same thing as [FintTypeMetadata.ref], which is the model reference
 * string ("utdanning-elev:Elev") naming a type inside the model. The two share
 * a word and nothing else: this one says where a resource is reached, that one
 * says which type it is.
 *
 * Build one for a relation's target with [FintRelation.targetIn], or for a
 * resource reached through a known context with [FintResourceMetadata.refIn].
 *
 * @property domainName the first segment, "utdanning" in "utdanning/elev/elev"
 * @property packageName the second segment, "elev"
 * @property resourceName the third segment, "elev"
 */
data class FintResourceRef(
    val domainName: String,
    val packageName: String,
    val resourceName: String,
)
