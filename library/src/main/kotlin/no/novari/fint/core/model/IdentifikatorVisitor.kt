package no.novari.fint.core.model

fun interface IdentifikatorVisitor {
    fun visit(field: String, value: String)
}
