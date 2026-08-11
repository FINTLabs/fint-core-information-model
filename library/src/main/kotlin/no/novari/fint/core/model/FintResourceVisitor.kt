package no.novari.fint.core.model

/**
 * Receives nested resources from [FintResource.visitNested], one at a time.
 */
fun interface FintResourceVisitor {

    /** Called with the field name and the resource held in it. */
    fun visit(field: String, resource: FintResource)
}
