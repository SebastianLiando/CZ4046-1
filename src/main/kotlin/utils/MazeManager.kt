package utils

import core.Config

/**
 * Manages interaction with the mze.
 *
 * @property maze The maze states.
 * @property coordinateManager The coordinate manager for the maze.
 */
class MazeManager(private val maze: List<String>, private val coordinateManager: CoordinateManager) {
    /** The number of columns in the grid. */
    val columnCount
        get() = coordinateManager.column

    /** The number of rows in the grid. */
    val rowCount
        get() = coordinateManager.row

    /** The number of cells in the grid. */
    val totalCell
        get() = coordinateManager.column * coordinateManager.row

    /** The maximum reward possible for the maze. */
    val maxReward
        get() = maze.mapNotNull { it.toDoubleOrNull() }.maxByOrNull { it } ?: 0.0

    /**
     * Returns `true` if the cell from the given coordinate is a wall state.
     *
     * @param x The x coordinate.
     * @param y The y coordinate.
     */
    fun isWall(x: Int, y: Int) = withCoordinate(x, y) { index ->
        maze[index].toLowerCase() == Config.WALL_CHAR.toString()
    }

    /**
     * Returns the reward of the state from the given coordinate.
     *
     * @param x The x coordinate.
     * @param y The y coordinate.
     */
    fun getReward(x: Int, y: Int) = withCoordinate(x, y) { index ->
        when (val char = maze[index].toLowerCase()) {
            Config.START_CHAR.toString() -> -0.04
            Config.WALL_CHAR.toString() -> throw IllegalArgumentException("Unable to get reward from a wall!")
            else -> char.toDouble()
        }
    }

    /**
     * Returns a table-like formatted String of the maze.
     *
     * @param pad The padding size between maze cells.
     */
    fun getPrintableMaze(pad: Int = 3) =
        getTableLikeString(columnCount, rowCount, maze, pad) { cell, _ ->
            if (cell == "W") "WALL" else cell
        }

    /**
     * Returns all possible actions of the state from the given coordinate.
     *
     * @param x The x coordinate.
     * @param y The y coordinate.
     *
     * @return List of possible actions.
     */
    fun getPossibleActions(x: Int, y: Int): List<Action> {
        return mutableListOf<Action>().apply {
            if (!isWall(x, y)) {
                if (y - 1 >= 0 && !isWall(x, y - 1)) add(Action.UP)
                if (y + 1 < rowCount && !isWall(x, y + 1)) add(Action.DOWN)
                if (x - 1 >= 0 && !isWall(x - 1, y)) add(Action.LEFT)
                if (x + 1 < columnCount && !isWall(x + 1, y)) add(Action.RIGHT)
            }
        }
    }

    /**
     * Converts coordinate to index.
     *
     * @param x The x coordinate.
     * @param y The y coordinate.
     */
    fun toIndex(x: Int, y: Int) = coordinateManager.toIndex(x, y)

    /**
     * Converts index to coordinate.
     *
     * @param index The index.
     */
    fun toCoordinate(index: Int) = coordinateManager.toCoordinate(index)

    /**
     * Gets the resulting coordinate if the action [action] is taken. If
     *
     * @param x The current x position.
     * @param y The current y position.
     * @param action The action to take.
     *
     * @return The resulting coordinate if the action is successful, otherwise stay in the same position.
     */
    fun getCoordinateForAction(x: Int, y: Int, action: Action): Pair<Int, Int> {
        val nextCoordinate = when (action) {
            Action.UP -> coordinateManager.getCoordinateAbove(x, y)
            Action.DOWN -> coordinateManager.getCoordinateBelow(x, y)
            Action.LEFT -> coordinateManager.getCoordinateLeft(x, y)
            Action.RIGHT -> coordinateManager.getCoordinateRight(x, y)
        } ?: x to y

        return if (isWall(nextCoordinate.first, nextCoordinate.second)) {
            x to y
        } else {
            nextCoordinate
        }
    }

    /**
     * The [block] will have the corresponding index from the given coordinate.
     *
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @param block The code that requires index.
     *
     * @return Whatever is returned from [block].
     */
    private fun <T> withCoordinate(x: Int, y: Int, block: (index: Int) -> T): T {
        val correspondingIndex = coordinateManager.toIndex(x, y)
        return block(correspondingIndex)
    }
}