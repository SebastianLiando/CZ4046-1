package core.algorithm

import extensions.round
import utils.*
import kotlin.math.max

/**
 * The base class for value iteration algorithm.
 *
 * @property gamma The discount factor.
 * @property manager The maze manager.
 */
abstract class BaseValueIteration(private val gamma: Double, private val manager: MazeManager) {
    /** Current utilities of all states. */
    var utilities = doubleListOf(value = 0.0, amount = manager.totalCell)
        private set

    /** The optimal policy given the current utilities. */
    val optimalPolicy: List<Action?>
        get() = utilities.mapIndexed { index, _ ->
            val (x, y) = manager.toCoordinate(index)

            manager.getPossibleActions(x, y).maxByOrNull {
                val (nextX, nextY) = manager.getCoordinateForAction(x, y, it)
                utilities[manager.toIndex(nextX, nextY)]
            }
        }

    /**
     * Sets all states' utilities to zero.
     *
     */
    private fun resetUtility() {
        utilities = doubleListOf(0.0, manager.totalCell)
    }

    /**
     * Returns the best utility for the current state.
     *
     * @param x The x-coordinate of current state.
     * @param y The y-coordinate of current state.
     * @param actions The possible actions for the current state.
     *
     * @return The best utility for the current state.
     */
    abstract fun calculateBestUtilityForState(x: Int, y: Int, actions: List<Action>): Double?

    /**
     * Runs value iteration algorithm.
     *
     * @param maxIteration The number of iterations to do.
     */
    fun runAlgorithm(maxIteration: Int) {
        resetUtility()

        val nextUtilities = doubleListOf(0.0, manager.totalCell).toMutableList()

        repeat(maxIteration) {
            pairCombination(manager.rowCount, manager.columnCount) { x, y ->
                val actions = manager.getPossibleActions(x, y)

                // Best utility will be null if there is no possible action
                val bestUtility = calculateBestUtilityForState(x, y, actions)

                // Skip state with no action (AKA walls)
                bestUtility?.let {
                    val indexToUpdate = manager.toIndex(x, y)
                    nextUtilities[indexToUpdate] = calculateNextUtility(x, y, it)
                }
            }

            utilities = nextUtilities
        }
    }

    fun getPrintableUtilities(pad: Int = 3, decimalPlaces: Int = 5): String {
        return getTableLikeString(
            manager.columnCount,
            manager.rowCount,
            utilities,
            pad
        ) { utility, (x, y) ->
            if (!manager.isWall(x, y)) {
                utility.round(decimalPlaces).toString()
            } else {
                "WALL"
            }
        }
    }

    fun getPrintablePolicy(pad: Int = 3): String {
        return getTableLikeString(
            manager.columnCount,
            manager.rowCount,
            optimalPolicy,
            pad
        ) { action, _ ->
            action?.toString() ?: "WALL"
        }
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
    protected fun calculateUtility(x: Int, y: Int, action: Action): Double {
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