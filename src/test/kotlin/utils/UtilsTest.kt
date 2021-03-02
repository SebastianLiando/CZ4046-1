package utils

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class UtilsTest {
    @Test
    fun `intListOf() creates a list of equal integer values`() {
        val result = doubleListOf(3.0, 3)
        assertThat(result.all { it == 3.0 }, `is`(true))
    }

    @Test
    fun `pairCombination() iterates through all combination`() {
        val expected = listOf(0 to 0, 1 to 0, 0 to 1, 1 to 1)

        val result = mutableListOf<Pair<Int, Int>>()
        pairCombination(2, 2) { x, y ->
            result.add(x to y)
        }

        assertThat(result, `is`(expected))
    }
}