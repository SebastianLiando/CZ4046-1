package utils

import core.Config

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

    fun isWall(x: Int, y: Int) = withCoordinate(x, y) { index ->
        maze[index].toLowerCase() == Config.WALL_CHAR.toString()
    }

    fun getReward(x: Int, y: Int) = withCoordinate(x, y) { index ->
        when (val char = maze[index].toLowerCase()) {
            Config.START_CHAR.toString() -> -0.04
            Config.WALL_CHAR.toString() -> throw IllegalArgumentException("Unable to get reward from a wall!")
            else -> char.toDouble()
        }
    }

    fun getPrintableMaze(pad: Int = 3) =
        getTableLikeString(columnCount, rowCount, maze, pad) { cell, _ ->
            if (cell == "W") "WALL" else cell
        }

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

    fun toIndex(x: Int, y: Int) = coordinateManager.toIndex(x, y)

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


    private fun <T> withCoordinate(x: Int, y: Int, block: (index: Int) -> T): T {
        val correspondingIndex = coordinateManager.toIndex(x, y)
        return block(correspondingIndex)
    }
}