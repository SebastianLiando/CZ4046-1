package core.algorithm

interface MarkovAlgorithm {
    /**
     * Returns a table-like [String] that displays the utilities of each state in it.
     *
     * @param pad The padding between elements.
     * @param decimalPlaces The number of decimal places to show.
     *
     * @return Utilities of each state in table-like format.
     */
    fun getPrintableUtilities(pad: Int = 3, decimalPlaces: Int = 5): String

    /**
     * Returns a table-like [String] that displays the policy.
     *
     * @param pad The padding between elements.
     *
     * @return The policy in table-like format.
     */
    fun getPrintablePolicy(pad: Int = 3): String

    /** Contains the utilities of states in each iteration. */
    val historyUtilities: List<List<Double>>
}