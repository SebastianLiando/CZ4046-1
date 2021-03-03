package core

import core.algorithm.ValueIteration
import utils.CoordinateManager
import utils.MazeManager
import utils.loadMaze

fun main(args: Array<String>) {
    val maze = loadMaze("maze.txt")
    val coordinateManager = CoordinateManager(Config.COLUMN_COUNT, Config.ROW_COUNT)

    val mazeManager = MazeManager(maze, coordinateManager)
    println(mazeManager.getPrintableMaze())

    val valueIterator = ValueIteration(Config.GAMMA, mazeManager)
    valueIterator.runAlgorithm(3)

    println(valueIterator.getPrintableUtilities())
    println(valueIterator.getPrintablePolicy())
}

