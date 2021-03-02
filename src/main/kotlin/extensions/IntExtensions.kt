package extensions

/** `true` if this number is larger or equal to zero. */
val Int.isNonNegative
    get() = this >= 0