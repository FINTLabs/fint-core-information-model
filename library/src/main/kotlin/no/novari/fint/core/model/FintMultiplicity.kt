package no.novari.fint.core.model

enum class FintMultiplicity(val lower: Int, val upper: Int?) {
    EXACTLY_ONE(1, 1),
    ZERO_OR_ONE(0, 1),
    ONE_OR_MORE(1, null),
    ZERO_OR_MORE(0, null);

    val required: Boolean get() = lower > 0
    val many: Boolean get() = upper == null
}
