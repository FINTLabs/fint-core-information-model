package no.novari.fint.core.model

import kotlin.reflect.KClass

/**
 * One field on a model type.
 *
 * @property name the field name
 * @property type the Kotlin class of the field's value
 * @property list true when the field holds a list of values
 * @property optional true when the model allows the field to be empty
 */
data class FintAttribute(
    val name: String,
    val type: KClass<*>,
    val list: Boolean,
    val optional: Boolean,
)
