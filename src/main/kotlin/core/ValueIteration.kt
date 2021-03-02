package core

import extensions.round
import utils.Action
import utils.MazeManager
import utils.doubleListOf
import utils.pairCombination
import kotlin.math.max

class ValueIteration(private val gamma: Double, private val manager: MazeManager) {
    /** Current utilities of all states. */
    private var utilities = doubleListOf(value = 0.0, amount = manager.totalCell)

    private fun resetUtility() {
        utilities = doubleListOf(0.0, manager.totalCell)
    }

    fun runValueIteration(maxIteration: Int) {
        resetUtility()

        var currentIterationNo = 1
        val nextUtilities = doubleListOf(0.0, manager.totalCell).toMutableList()

        while (currentIterationNo <= maxIteration) {
            pairCombination(manager.rowCount, manager.columnCount) { x, y ->
                val actions = manager.getPossibleActions(x, y)

                // Best utility will be null if there is no possible action
                val bestUtility = actions.map { calculateUtility(x, y, it) }
                    .maxByOrNull { it }

                // Skip state with no action (AKA walls)
                bestUtility?.let {
                    val indexToUpdate = manager.toIndex(x, y)
                    nextUtilities[indexToUpdate] = calculateNextUtility(x, y, it)
                }
            }

            currentIterationNo++
            utilities = nextUtilities
        }
    }

    fun getPrintableUtilities(pad: Int = 3, decimalPlaces: Int = 5): String {
        val rounded = utilities.map { it.round(decimalPlaces).toString() }
        val maxLength = max(rounded.maxByOrNull { it.length }!!.length, "WALL".length)

        var result = ""

        pairCombination(manager.rowCount, manager.columnCount, onNextY = { result += "\n" }) { x, y ->
            result += (if (!manager.isWall(x, y)) {
                utilities[manager.toIndex(x, y)].round(decimalPlaces)
            } else {
                "WALL"
            }).toString().padStart(maxLength + pad)
        }

        return result
    }

    /**
     * Calculates the utility if the agent takes the specified action.
     *
     * @param x The current x position of the agent.
     * @param y The current y position of the agent.
     * @param action The action that the agent takes.
     *
     * @return The resulting utility.
     */
    private fun calculateUtility(x: Int, y: Int, action: Action): Double {
        val first = manager.getCoordinateForAction(x, y, action)
        var second = x to y
        var third = x to y

        when (action) {
            Action.UP, Action.DOWN -> {
                second = manager.getCoordinateForAction(x, y, Action.LEFT)
                third = manager.getCoordinateForAction(x, y, Action.RIGHT)
            }

            Action.RIGHT, Action.LEFT -> {
                second = manager.getCoordinateForAction(x, y, Action.DOWN)
                third = manager.getCoordinateForAction(x, y, Action.UP)
            }
        }

        val firstIndex = manager.toIndex(first.first, first.second)
        val secondIndex = manager.toIndex(second.first, second.second)
        val thirdIndex = manager.toIndex(third.first, third.second)

        return CORRECT_PROB * utilities[firstIndex] +
                SWIVEL_PROB * utilities[secondIndex] +
                SWIVEL_PROB * utilities[thirdIndex]

    }

    /**
     * Calculates the utility value for the next iteration using Bellman's equation.
     *
     * @param x The current x position of the agent.
     * @param y The current y position of the agent.
     * @param bestUtility The best utility value for the current position.
     *
     * @return The utility value for the next iteration.
     */
    private fun calculateNextUtility(x: Int, y: Int, bestUtility: Double) =
        manager.getReward(x, y) + gamma * bestUtility

    companion object {
        const val CORRECT_PROB = 0.8
        const val SWIVEL_PROB = 0.1
    }
}