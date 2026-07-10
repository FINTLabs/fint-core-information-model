package no.novari.fint.core.model

/**
 * How many of something the model expects, given as a range.
 */
enum class FintMultiplicity(val lower: Int, val upper: Int?) {
    EXACTLY_ONE(1, 1),
    ZERO_OR_ONE(0, 1),
    ONE_OR_MORE(1, null),
    ZERO_OR_MORE(0, null);

    /** True when at least one is required. */
    val required: Boolean get() = lower > 0

    /** True when there can be more than one. */
    val many: Boolean get() = upper == null
}
