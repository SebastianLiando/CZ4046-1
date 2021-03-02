package utils

import extensions.isNonNegative

/**
 * Manages conversion to and from a grid coordinate and list index. (0,0) is the top left of the grid.
 *
 * @property column The number of columns the grid has.
 * @property row The number of rows the grid has.
 */
class CoordinateManager(val column: Int, val row: Int) {
    fun toIndex(x: Int, y: Int): Int {
        require(isCoordinateInBound(x, y)) { "x and y is out of bounds!" }

        return y * column + x
    }

    fun toCoordinate(index: Int): Pair<Int, Int> {
        require(index in 0 until column * row) { "Index is out of bounds!" }

        val x = index % column
        val y = index / column

        return x to y
    }

    fun getCoordinateAbove(x: Int, y: Int) = inBoundOrNull(x, y - 1)

    fun getCoordinateBelow(x: Int, y: Int) = inBoundOrNull(x, y + 1)

    fun getCoordinateLeft(x: Int, y: Int) = inBoundOrNull(x - 1, y)

    fun getCoordinateRight(x: Int, y: Int) = inBoundOrNull(x + 1, y)

    private fun inBoundOrNull(x: Int, y: Int) = if (isCoordinateInBound(x, y)) {
        x to y
    } else {
        null
    }

    private fun isCoordinateInBound(x: Int, y: Int) =
        x.isNonNegative && y.isNonNegative && x < column && y < row
}