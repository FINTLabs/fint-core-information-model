package no.novari.fint.core.model

/**
 * Receives id fields from [FintResource.visitIdentifikators], one at a time.
 */
fun interface IdentifikatorVisitor {

    /** Called with the field name and its value. */
    fun visit(field: String, value: String)
}
