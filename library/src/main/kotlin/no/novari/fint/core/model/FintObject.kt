package no.novari.fint.core.model

/**
 * Base type for everything generated from the FINT model.
 */
interface FintObject {

    /** Metadata describing this type. */
    val metadata: FintTypeMetadata
}
