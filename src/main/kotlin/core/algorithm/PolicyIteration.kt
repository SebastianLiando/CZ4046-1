package core.algorithm

import extensions.contentEquals
import extensions.round
import utils.Action
import utils.MazeManager
import utils.getTableLikeString

/**
 * Runs policy iteration algorithm using modified policy evaluation.
 *
 * @property manager The maze manager.
 * @property gamma The discount factor.
 * @property k The number of iterations for simplified value iteration.
 */
class PolicyIteration(
    private val manager: MazeManager,
    private val gamma: Double,
    private val k: Int
) : MarkovAlgorithm {
    /** The current policy. */
    private var currentPolicy: List<Action?>

    init {
        currentPolicy = getRandomPolicy()
    }

    /** Returns the estimated utility for the optimal policy. */
    val estimatedUtility: List<Double>
        get() {
            val simplifiedIterator = SimplifiedValueIteration(gamma, manager, currentPolicy)
            simplifiedIterator.runAlgorithm(k)

            return simplifiedIterator.utilities
        }

    private val _historyUtilities = mutableListOf(estimatedUtility)
    override val historyUtilities: List<List<Double>> = _historyUtilities

    /** Resets the algorithm policy back to the initial policy. */
    private fun reset() {
        // Assign random policy
        currentPolicy = getRandomPolicy()

        // Clear state utility history
        _historyUtilities.clear()
        _historyUtilities.add(estimatedUtility)
    }

    /**
     * Generates a random policy.
     *
     * @return Random policy.
     */
    private fun getRandomPolicy(): List<Action?> {
        // Start with null action on all states
        val randomPolicy = Array<Action?>(manager.rowCount * manager.columnCount) { null }

        // Iterate through all state
        (0 until manager.rowCount * manager.columnCount).forEach { index ->
            // Get the corresponding state
            val (x, y) = manager.toCoordinate(index)

            /* Get random action from all possible actions in that state and
             assign it to the state */
            val randomAction = manager.getPossibleActions(x, y).randomOrNull()
            randomAction?.let { randomPolicy[index] = it }
        }

        return randomPolicy.toList()
    }

    /**
     * Runs the algorithm and return the optimal policy.
     *
     * @param maxIteration The maximum allowed iteration.
     *
     * @return The optimal policy.
     */
    override fun runAlgorithm(maxIteration: Int) {
        // Set the current policy randomly
        reset()

        // Limit the iteration count
        repeat(maxIteration) {
            print("\rIteration ${it + 1}")
            // Policy evaluation
            val simplifiedIterator = SimplifiedValueIteration(gamma, manager, currentPolicy)

            simplifiedIterator.runAlgorithm(k)

            // Policy improvement
            val newOptimal = simplifiedIterator.optimalPolicy

            // Stop algorithm if the policy does not change
            if (currentPolicy.contentEquals(newOptimal)) {
                println("\rPolicy iteration ended in ${it + 1} iterations")
                return
            }

            // Update the current policy
            currentPolicy = newOptimal
            _historyUtilities.add(estimatedUtility)
        }
    }

    override fun getPrintablePolicy(pad: Int) = getTableLikeString(
        manager.columnCount,
        manager.rowCount,
        currentPolicy,
        pad
    ) { policy, _ ->
        policy?.toString() ?: "WALL"
    }

    override fun getPrintableUtilities(pad: Int, decimalPlaces: Int) = getTableLikeString(
        manager.columnCount,
        manager.rowCount,
        estimatedUtility,
        pad
    ) { estimate, (x, y) ->
        if (!manager.isWall(x, y)) {
            estimate.round(decimalPlaces).toString()
        } else {
            "WALL"
        }
    }
}