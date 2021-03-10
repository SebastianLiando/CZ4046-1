package core.algorithm

import utils.Action
import utils.MazeManager

/**
 * Runs simplified version of value iteration. In simplified version, the value iteration
 * is provided with the action to take.
 *
 * @property policy The actions that the agent should take at each state.
 */
class SimplifiedValueIteration(
    gamma: Double,
    private val manager: MazeManager,
    private val policy: List<Action?>
) : BaseValueIteration(gamma, manager) {

    override fun calculateBestUtilityForState(x: Int, y: Int, actions: List<Action>): Double? {
        // Get the action according to the current policy
        val action = policy[manager.toIndex(x, y)]

        // Return the utility if the agent takes the action
        return action?.let { calculateUtility(x, y, it) }
    }
}