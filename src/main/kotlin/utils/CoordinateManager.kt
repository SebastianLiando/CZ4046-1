package utils

/**
 * Manages conversion to and from a grid coordinate and list index. (0,0) is the top left of the grid.
 *
 * @property column The number of columns the grid has.
 * @property row The number of rows the grid has.
 */
class CoordinateManager(val column: Int, val row: Int) {
    fun toIndex(x: Int, y: Int): Int {
        require(x >= 0 && y >= 0) { "x and y must be positive!" }
        require(x < column && y < row) { "x and y is out of bounds!" }

        return y * column + x
    }

    fun toCoordinate(index: Int): Pair<Int, Int> {
        require(index in 0 until column * row) { "Index is out of bounds!" }

        val x = index % column
        val y = index / column

        return x to y
    }
}