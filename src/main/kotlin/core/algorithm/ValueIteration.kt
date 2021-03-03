package core.algorithm

import utils.Action
import utils.MazeManager

/**
 * Runs the value iteration algorithm.
 */
class ValueIteration(
    private val gamma: Double, private val manager: MazeManager
) : BaseValueIteration(gamma, manager) {

    override fun calculateBestUtilityForState(x: Int, y: Int, actions: List<Action>): Double? {
        return actions.map { calculateUtility(x, y, it) }.maxByOrNull { it }
    }
}