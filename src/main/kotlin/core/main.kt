package core

import core.algorithm.PolicyIteration
import core.algorithm.ValueIteration
import utils.CoordinateManager
import utils.MazeManager
import utils.loadMaze

fun main() {
    // Load maze file
    val maze = loadMaze("maze.txt")
    println("Maze file successfully loaded!")

    // Initialize dependencies
    val coordinateManager = CoordinateManager(Config.COLUMN_COUNT, Config.ROW_COUNT)

    val mazeManager = MazeManager(maze, coordinateManager)
    println("* Maze *")
    println(mazeManager.getPrintableMaze())

    val titleDecorator = "=".repeat(30)

    // Value iteration
    println("${titleDecorator}Value Iteration${titleDecorator}")
    val valueIterator = ValueIteration(Config.GAMMA, mazeManager)
    valueIterator.runAlgorithm(100)

    println("* State Utilities *")
    println(valueIterator.getPrintableUtilities())
    println("* Optimal policy *")
    println(valueIterator.getPrintablePolicy())
    println("There are ${valueIterator.historyUtilities.size} history utilities\n")

    // Policy iteration
    println("${titleDecorator}Policy Iteration${titleDecorator}")
    val policyIterator = PolicyIteration(mazeManager, Config.GAMMA, 10)
    policyIterator.runAlgorithm()

    println()
    println("* State Utilities *")
    println(policyIterator.getPrintableEstimateUtilities())
    println("* Optimal policy *")
    println(policyIterator.getPrintablePolicy())
    println("There are ${policyIterator.historyUtilities.size} history utilities")
}

