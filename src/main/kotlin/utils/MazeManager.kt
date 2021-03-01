package utils

import core.Config

class MazeManager(val maze: List<String>, private val coordinateManager: CoordinateManager) {
    val columnCount
        get() = coordinateManager.column

    val rowCount
        get() = coordinateManager.row

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

    fun getPrintableMaze(pad: Int = 3): String {
        val longestString = maze.maxByOrNull { it.length }!!

        var result = ""

        (0 until coordinateManager.row).forEach { y ->
            (0 until coordinateManager.column).forEach { x ->
                withCoordinate(x, y) { index ->
                    result += maze[index].padStart(longestString.length + pad)
                }
            }
            result += "\n"
        }

        return result
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

    private fun <T> withCoordinate(x: Int, y: Int, block: (index: Int) -> T): T {
        val correspondingIndex = coordinateManager.toIndex(x, y)
        return block(correspondingIndex)
    }
}