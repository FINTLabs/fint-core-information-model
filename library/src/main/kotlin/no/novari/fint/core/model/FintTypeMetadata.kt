package no.novari.fint.core.model

import kotlin.reflect.KClass

/**
 * Describes one type from the FINT model.
 */
interface FintTypeMetadata {

    /** The Kotlin class this metadata belongs to. */
    val type: KClass<*>

    /** The model reference, e.g. "utdanning-elev:Elev". */
    val ref: String

    /** Every field on the type, including inherited ones. */
    val attributes: List<FintAttribute>
}
