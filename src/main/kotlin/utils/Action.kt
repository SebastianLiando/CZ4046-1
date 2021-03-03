package utils

/**
 * Represents the action that the agent should take.
 *
 */
enum class Action {
    UP, DOWN, LEFT, RIGHT;

    companion object {
        /** Returns a random action in each call. */
        val randomAction
            get() = values().random()
    }
}