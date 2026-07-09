package no.novari.fint.core.model

import kotlin.reflect.KClass

data class FintAttribute(
    val name: String,
    val type: KClass<*>,
    val list: Boolean,
    val optional: Boolean,
)
