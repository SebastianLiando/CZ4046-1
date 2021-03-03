package utils

/**
 * Creates a list with the given value and size.
 *
 * @param value The values of the list.
 * @param amount The number of elements in the list.
 */
fun doubleListOf(value: Double, amount: Int) = (1..amount).map { value }

/**
 * Loops through every combination of sequence of 2 values.
 *
 * @param maxX The maximum first value.
 * @param maxY The maximum second value.
 * @param onNextY What to do before going to the next second value.
 * @param block The action to do.
 */
fun pairCombination(maxX: Int, maxY: Int, onNextY: () -> Unit = {}, block: (Int, Int) -> Unit) {
    (0 until maxY).forEach { y ->
        (0 until maxX).forEach { x ->
            block(x, y)
        }
        onNextY()
    }
}

fun <T> getTableLikeString(
    column: Int, row: Int,
    content: List<T>, pad: Int = 3,
    toString: (T, coordinate: Pair<Int, Int>) -> String
): String {
    var result = ""

    val coordinateManager = CoordinateManager(column, row)

    val listOfString = content.mapIndexed { index, t ->
        toString(t, coordinateManager.toCoordinate(index))
    }

    val longestStringLength = listOfString.maxByOrNull { it.length }?.length
        ?: throw IllegalArgumentException("List must not be empty!")

    pairCombination(row, column, onNextY = { result += "\n" }) { x, y ->
        val index = coordinateManager.toIndex(x, y)
        val item = listOfString[index]

        result += item.padEnd(longestStringLength + pad)
    }

    return result
}