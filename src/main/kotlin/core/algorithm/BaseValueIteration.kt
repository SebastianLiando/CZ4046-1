package core.algorithm

import extensions.copy
import extensions.round
import utils.*
import kotlin.math.ceil
import kotlin.math.log10

/**
 * The base class for value iteration algorithm.
 *
 * @property gamma The discount factor.
 * @property manager The maze manager.
 */
abstract class BaseValueIteration(
    private val gamma: Double, private val manager: MazeManager
) : MarkovAlgorithm {
    /** Current utilities of all states. */
    var utilities = doubleListOf(value = 0.0, amount = manager.totalCell)
        private set

    private val _historyUtilities = mutableListOf(utilities)
    override val historyUtilities: List<List<Double>> = _historyUtilities

    /** The optimal policy given the current utilities. */
    val optimalPolicy: List<Action?>
        get() = utilities.mapIndexed { index, _ ->
            val (x, y) = manager.toCoordinate(index)

            // From all possible actions of this state
            manager.getPossibleActions(x, y).maxByOrNull {
                // Get the resulting state from the action
                val (nextX, nextY) = manager.getCoordinateForAction(x, y, it)
                // Compare the utility
                utilities[manager.toIndex(nextX, nextY)]
            }
        }

    /**
     * Sets all states' utilities to zero.
     *
     */
    private fun resetUtility() {
        utilities = doubleListOf(0.0, manager.totalCell)
        _historyUtilities.clear()
        _historyUtilities.add(utilities)
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
    override fun runAlgorithm(maxIteration: Int) {
        // Set all utility to 0
        resetUtility()

        // To save the utilities for the next state
        val nextUtilities = doubleListOf(0.0, manager.totalCell).toMutableList()

        repeat(maxIteration) {
            // Iterate through every state
            pairCombination(manager.rowCount, manager.columnCount) { x, y ->
                // Get all the possible actions of this state
                val actions = manager.getPossibleActions(x, y)

                /* Get max utility for the state.
                 Best utility will be null if there is no possible action */
                val bestUtility = calculateBestUtilityForState(x, y, actions)

                // Skip state with no action (i.e. walls)
                bestUtility?.let {
                    val indexToUpdate = manager.toIndex(x, y)

                    // Calculate utility using Bellman equation
                    nextUtilities[indexToUpdate] = calculateNextUtility(x, y, it)
                }
            }

            // Add to history
            _historyUtilities.add(nextUtilities.copy())

            // Update the utilities for the next iteration
            utilities = nextUtilities
        }
    }

    override fun getPrintableUtilities(pad: Int, decimalPlaces: Int): String {
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

    override fun getPrintablePolicy(pad: Int): String {
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
     * Returns the number of iteration required to have an error of maximum [e].
     *
     * @param e The maximum error allowed.
     * @return The number of iterations required.
     */
    fun getRequiredIterationForError(e: Double): Int {
        return ceil(log10(2 * manager.maxReward / (e * (1 - gamma))) / log10(1 / gamma)).toInt()
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