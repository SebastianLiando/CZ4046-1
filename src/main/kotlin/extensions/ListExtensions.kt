package extensions

inline fun <reified T> List<T>.contentEquals(otherList: List<T>) =
    this.toTypedArray().contentEquals(otherList.toTypedArray())
