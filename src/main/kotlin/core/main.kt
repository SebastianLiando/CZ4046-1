package core

import core.algorithm.PolicyIteration
import core.algorithm.ValueIteration
import utils.CoordinateManager
import utils.MazeManager
import utils.loadMaze
import java.util.*

fun main() {
    // Load maze file
    val maze = loadMaze("maze.txt")
    println("Maze file successfully loaded!")

    // Initialize dependencies
    val coordinateManager = CoordinateManager(Config.COLUMN_COUNT, Config.ROW_COUNT)

    val mazeManager = MazeManager(maze, coordinateManager)

    val titleDecorator = "=".repeat(30)

    while (true) {
        val actionToChoose = readUntilValid(
            "Choose option\n1. Value iteration\n2. Policy iteration\n3. Display maze\n0. Quit"
        ) {
            it == "1" || it == "2" || it == "3" || it == "0"
        }.toInt()

        when (actionToChoose) {
            0 -> {
                println("Quitting...")
                break
            }

            1 -> {
                // Value iteration
                val maxError = readUntilValid("Enter max error") {
                    it.toDoubleOrNull() != null
                }.toDouble()

                println("${titleDecorator}Value Iteration${titleDecorator}")
                val valueIterator = ValueIteration(Config.GAMMA, mazeManager)
                val requiredRuns = valueIterator.getRequiredIterationForError(maxError)

                valueIterator.runAlgorithm(requiredRuns)

                println("* State Utilities *")
                println(valueIterator.getPrintableUtilities())
                println("* Optimal policy *")
                println(valueIterator.getPrintablePolicy())
                println(
                    "There are ${valueIterator.historyUtilities.size} history utilities " +
                            "for $requiredRuns runs\n"
                )
            }

            2 -> {
                // Policy iteration
                val k = readUntilValid("Input parameter k: ") {
                    it.toIntOrNull() != null
                }.toInt()
                
                println("${titleDecorator}Policy Iteration${titleDecorator}")
                val policyIterator = PolicyIteration(mazeManager, Config.GAMMA, k)
                policyIterator.runAlgorithm()

                println()
                println("* State Utilities *")
                println(policyIterator.getPrintableEstimateUtilities())
                println("* Optimal policy *")
                println(policyIterator.getPrintablePolicy())
                println("There are ${policyIterator.historyUtilities.size} history utilities")
            }

            3 -> {
                println("${titleDecorator}Maze${titleDecorator}")
                println(mazeManager.getPrintableMaze())
            }
        }
    }
}

/**
 * Keep trying to read input from the user until the user inputs the valid input.
 *
 * @param message The message to tell the user what to input.
 * @param onInvalid The action to do when the user key in invalid input.
 * @param isValid The function that validates the user input.
 *
 * @return The valid input.
 */
private fun readUntilValid(
    message: String,
    onInvalid: (String) -> Unit = { println("Invalid input!") },
    isValid: (String) -> Boolean
): String {
    var userInput: String
    val scanner = Scanner(System.`in`)

    while (true) {
        println(message)

        userInput = scanner.nextLine()

        if (!isValid(userInput)) {
            onInvalid(userInput)
        } else {
            break
        }
    }

    return userInput
}

