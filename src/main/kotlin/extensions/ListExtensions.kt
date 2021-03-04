package extensions

inline fun <reified T> List<T>.contentEquals(otherList: List<T>) =
    this.toTypedArray().contentEquals(otherList.toTypedArray())

/**
 * Returns a shallow copy of the list.
 *
 */
fun <T> List<T>.copy() = this.map { it }
