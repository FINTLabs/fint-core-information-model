package no.novari.fint.core.model

/**
 * A resource at one of the places the model serves it.
 *
 * [path] is what a URL shows, [ref] is the identity the platform routes and
 * stores by, and [metadata] describes the type served there. The three agree:
 * [FintModel.refOf] of the path is the ref, [FintModel.byPath] of the ref is
 * the metadata, and [FintResourceMetadata.name] is the last segment of the path.
 *
 * A common resource appears once per place it is reached from, so felles:Person
 * appears with path "utdanning/elev/person" and again with
 * "administrasjon/personal/person". The felles/kodeverk/iso resources keep
 * their extra segment in [path] while [ref] drops it, so Landkode appears with
 * path "felles/kodeverk/iso/landkode" and ref ("felles", "kodeverk", "landkode").
 *
 * Get them with [FintModel.served] or [FintModel.resourcesIn].
 */
data class FintServedResource(
    val path: String,
    val ref: FintResourceRef,
    val metadata: FintResourceMetadata,
)
