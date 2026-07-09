package no.novari.fint.core.model

import kotlin.reflect.KClass

data class FintRelation(
    val name: String,
    val target: KClass<out FintObject>,
    val targetPath: String?,
    val multiplicity: FintMultiplicity,
    val bidirectional: Bidirectional? = null,
) {
    val isBidirectional: Boolean get() = bidirectional != null
}
