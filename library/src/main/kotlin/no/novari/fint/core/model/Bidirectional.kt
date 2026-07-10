package no.novari.fint.core.model

/**
 * Extra information for a relation that goes both ways.
 *
 * @property inverseName the relation name seen from the other side
 * @property isSource true when this side owns the relation in the model
 * @property inverseMultiplicity how many links the other side expects
 */
data class Bidirectional(
    val inverseName: String,
    val isSource: Boolean,
    val inverseMultiplicity: FintMultiplicity,
)
