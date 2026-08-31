package no.novari.fint.core.model

/**
 * Where a resource is served: the domain, package and resource name the
 * platform routes and stores by, the "utdanning", "elev" and "elev" of
 * "utdanning/elev/elev".
 *
 * Usually these are exactly the three segments of the REST path. The
 * felles/kodeverk/iso resources are the exception: their path has a fourth
 * segment, and the identity keeps the first two segments and the last, so
 * Landkode is served at "felles/kodeverk/iso/landkode" and identified as
 * ("felles", "kodeverk", "landkode").
 *
 * Not the same thing as [FintTypeMetadata.ref], which is the model reference
 * string ("utdanning-elev:Elev") naming a type inside the model. The two share
 * a word and nothing else: this one says where a resource is reached, that one
 * says which type it is.
 *
 * Build one for a relation's target with [FintRelation.targetIn], for a
 * resource reached through a known context with [FintResourceMetadata.refIn],
 * or from a served path with [FintModel.refOf].
 *
 * @property domainName the first segment, "utdanning" in "utdanning/elev/elev"
 * @property packageName the second segment, "elev"
 * @property resourceName the last segment, "elev"
 */
data class FintResourceRef(
    val domainName: String,
    val packageName: String,
    val resourceName: String,
)
