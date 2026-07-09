package no.novari.fint.core.model

import no.novari.fint.core.model.felles.kompleksedatatyper.Identifikator

fun interface IdentifikatorVisitor {
    fun visit(name: String, value: Identifikator)
}
