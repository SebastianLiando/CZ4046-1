package core

import java.io.File
import java.util.*

object Config {
    const val COLUMN_COUNT = 6
    const val ROW_COUNT = 6
    const val START_CHAR = 's'
    const val WALL_CHAR = 'w'
    const val GAMMA = 0.99

    private val parentFile = File(System.getProperty("user.dir"), "output")
    private val valueIterationParent = File(parentFile, "value iteration")
    private val policyIterationParent = File(parentFile, "policy iteration")

    private val fileName
        get() = "${Date().time}.csv"

    val valueIterationFile
        get() = File(valueIterationParent, fileName)

    val policyIterationFile
        get() = File(policyIterationParent, fileName)
}