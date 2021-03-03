package core

import core.algorithm.PolicyIteration
import core.algorithm.ValueIteration
import extensions.contentEquals
import utils.*

fun main(args: Array<String>) {
    val maze = loadMaze("maze.txt")
    val coordinateManager = CoordinateManager(Config.COLUMN_COUNT, Config.ROW_COUNT)

    val mazeManager = MazeManager(maze, coordinateManager)
    println(mazeManager.getPrintableMaze())

    val valueIterator = ValueIteration(Config.GAMMA, mazeManager)
    valueIterator.runAlgorithm(100)

    println(valueIterator.getPrintableUtilities())
    println(valueIterator.getPrintablePolicy())

    val policyIterator = PolicyIteration(mazeManager, Config.GAMMA, 10)
    policyIterator.runAlgorithm()

    println(policyIterator.getPrintableEstimateUtilities())
    println(policyIterator.getPrintablePolicy())
}

