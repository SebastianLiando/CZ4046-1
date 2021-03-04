package core

import core.algorithm.MarkovAlgorithm
import core.algorithm.PolicyIteration
import core.algorithm.ValueIteration
import utils.CoordinateManager
import utils.CsvWriter
import utils.MazeManager
import utils.loadMaze
import java.io.File
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

                runAlgorithmThenReportResult(valueIterator, Config.valueIterationFile) {
                    val requiredRuns = valueIterator.getRequiredIterationForError(maxError)
                    valueIterator.runAlgorithm(requiredRuns)
                }
            }

            2 -> {
                // Policy iteration
                val k = readUntilValid("Input parameter k: ") {
                    it.toIntOrNull() != null
                }.toInt()

                val maxIteration = readUntilValid("Input max iteration (-1 for infinite): ") {
                    it.toIntOrNull() != null
                }.toInt()

                println("${titleDecorator}Policy Iteration${titleDecorator}")

                val policyIterator = PolicyIteration(mazeManager, Config.GAMMA, k)

                runAlgorithmThenReportResult(policyIterator, Config.policyIterationFile) {
                    if (maxIteration == -1) {
                        policyIterator.runAlgorithm()
                    } else {
                        policyIterator.runAlgorithm(maxIteration)
                    }
                }
            }

            3 -> {
                println("${titleDecorator}Maze${titleDecorator}")
                println(mazeManager.getPrintableMaze())
            }
        }
    }
}

/**
 * Runs MDP algorithm in the [run] block and report the result afterwards.
 *
 * @param algorithm The algorithm to run.
 * @param reportFile The file to write the report to.
 * @param run The block that runs the algorithm.
 */
private fun runAlgorithmThenReportResult(
    algorithm: MarkovAlgorithm,
    reportFile: File,
    run: () -> Unit
) {
    run()

    println("* State Utilities *")
    println(algorithm.getPrintableUtilities())
    println("* Optimal policy *")
    println(algorithm.getPrintablePolicy())
    println("There are ${algorithm.historyUtilities.size} history utilities")

    val saveToFile = readUntilValid("Save state utilities history to file? (Y / N)") {
        it.toLowerCase() in listOf("y", "n")
    } == "y"

    if (saveToFile) {
        val writer = CsvWriter<Double>(reportFile)
        writer.write(algorithm.historyUtilities)
        println("Written state utilities history to ${reportFile.path}")
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

