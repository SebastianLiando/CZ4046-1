package utils

import extensions.isNonNegative

/**
 * Manages conversion to and from a grid coordinate and list index. (0,0) is the top left of the grid.
 *
 * @property column The number of columns the grid has.
 * @property row The number of rows the grid has.
 */
class CoordinateManager(val column: Int, val row: Int) {
    /**
     * Converts coordinate to index.
     *
     * @param x The x coordinate.
     * @param y The y coordinate.
     */
    fun toIndex(x: Int, y: Int): Int {
        require(isCoordinateInBound(x, y)) { "x and y is out of bounds!" }

        return y * column + x
    }

    /**
     * Converts index to coordinate.
     *
     * @param index The index.
     */
    fun toCoordinate(index: Int): Pair<Int, Int> {
        require(index in 0 until column * row) { "Index is out of bounds!" }

        val x = index % column
        val y = index / column

        return x to y
    }

    /**
     * Returns the coordinate above the specified coordinate.
     *
     * @param x The x coordinate.
     * @param y The y coordinate.
     */
    fun getCoordinateAbove(x: Int, y: Int) = inBoundOrNull(x, y - 1)

    /**
     * Returns the coordinate below the specified coordinate.
     *
     * @param x The x coordinate.
     * @param y The y coordinate.
     */
    fun getCoordinateBelow(x: Int, y: Int) = inBoundOrNull(x, y + 1)

    /**
     * Returns the coordinate on the left of the specified coordinate.
     *
     * @param x The x coordinate.
     * @param y The y coordinate.
     */
    fun getCoordinateLeft(x: Int, y: Int) = inBoundOrNull(x - 1, y)

    /**
     * Returns the coordinate above on the right of specified coordinate.
     *
     * @param x The x coordinate.
     * @param y The y coordinate.
     */
    fun getCoordinateRight(x: Int, y: Int) = inBoundOrNull(x + 1, y)

    /**
     * Returns `null` if the coordinate is not valid in the maze.
     * Otherwise, return the current coordinate.
     *
     * @param x The x coordinate.
     * @param y The y coordinate.
     */
    private fun inBoundOrNull(x: Int, y: Int) = if (isCoordinateInBound(x, y)) {
        x to y
    } else {
        null
    }

    /**
     * Returns `true` if the coordinate is valid in the maze.
     *
     * @param x The x coordinate.
     * @param y The y coordinate.
     */
    private fun isCoordinateInBound(x: Int, y: Int) =
        x.isNonNegative && y.isNonNegative && x < column && y < row
}