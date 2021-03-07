package core

import java.io.File
import java.util.*

/**
 * Contains project configuration.
 */
object Config {
    /** The width of the maze. */
    const val COLUMN_COUNT = 6

    /** The height of the maze. */
    const val ROW_COUNT = 6

    /** The character which represents the starting state. */
    const val START_CHAR = 's'

    /** The character which represents the wall state. */
    const val WALL_CHAR = 'w'

    /** The discount factor to use. */
    const val GAMMA = 0.99

    /** Output directory. */
    private val parentFile = File(System.getProperty("user.dir"), "output")

    /** Directory for value iteration files. */
    private val valueIterationParent = File(parentFile, "value iteration")

    /** Directory for policy iteration files. */
    private val policyIterationParent = File(parentFile, "policy iteration")

    /** File name based on current time. */
    private val fileName
        get() = "${Date().time}.csv"

    /** The [File] to save value iteration state utility history. */
    val valueIterationFile
        get() = File(valueIterationParent, fileName)

    /** The [File] to save policy iteration state utility history. */
    val policyIterationFile
        get() = File(policyIterationParent, fileName)
}