package extensions

import kotlin.math.round

/**
 * Rounds to [decimals] decimal places.
 *
 * @param decimals Decimal numbers to round to.
 * @return Rounded decimal number.
 */
fun Double.round(decimals: Int): Double {
    var multiplier = 1.0
    repeat(decimals) { multiplier *= 10 }
    return round(this * multiplier) / multiplier
}