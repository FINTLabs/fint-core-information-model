package no.novari.fint.core.model

import kotlin.reflect.KClass

interface FintTypeMetadata {
    val type: KClass<*>
    val ref: String
    val attributes: List<FintAttribute>
}
