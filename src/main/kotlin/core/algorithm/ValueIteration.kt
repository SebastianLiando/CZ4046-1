package core.algorithm

import utils.Action
import utils.MazeManager

/**
 * Runs the value iteration algorithm.
 */
class ValueIteration(
    gamma: Double, manager: MazeManager
) : BaseValueIteration(gamma, manager) {

    override fun calculateBestUtilityForState(x: Int, y: Int, actions: List<Action>): Double? {
        return actions                           // All possible actions
            .map { calculateUtility(x, y, it) }  // Calculate the utility if the actions is taken
            .maxByOrNull { it }                  // Get the maximum expected utility
    }
}