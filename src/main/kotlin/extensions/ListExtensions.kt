package extensions

/**
 * Checks if the content of the list is equal to the content of [otherList].
 *
 * @param otherList The other list to compare to.
 */
inline fun <reified T> List<T>.contentEquals(otherList: List<T>) =
    this.toTypedArray().contentEquals(otherList.toTypedArray())

/**
 * Returns a shallow copy of the list.
 *
 */
fun <T> List<T>.copy() = this.map { it }
