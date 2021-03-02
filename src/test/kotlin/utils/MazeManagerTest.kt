package utils

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test

class MazeManagerTest {
    private lateinit var sut: MazeManager

    companion object {
        const val COLUMN = 6
        const val ROW = 6
    }

    @Before
    fun before() {
        sut = MazeManager(loadMaze("maze.txt"), CoordinateManager(COLUMN, ROW))
    }

    @Test
    fun `isWall() checks if the chosen coordinate is a wall`() {
        assertThat(sut.isWall(1, 0), `is`(true))
        assertThat(sut.isWall(0, 0), `is`(false))
    }

    @Test
    fun `getReward() returns -0,04 for start point`() {
        assertThat(sut.getReward(2, 3), `is`(-0.04))
    }

    @Test
    fun `getReward() returns the given value in the file`() {
        assertThat(sut.getReward(0, 0), `is`(1.0))
    }

    @Test
    fun `printMaze() prints the maze justified`() {
        val result = "       1       W       1   -0.04   -0.04       1\n" +
                "   -0.04      -1   -0.04       1       W      -1\n" +
                "   -0.04   -0.04      -1   -0.04       1   -0.04\n" +
                "   -0.04   -0.04       S      -1   -0.04       1\n" +
                "   -0.04       W       W       W      -1   -0.04\n" +
                "   -0.04   -0.04   -0.04   -0.04   -0.04   -0.04\n"

        assertThat(sut.getPrintableMaze(), `is`(result))
    }

    @Test
    fun `A wall does not have possible actions`() {
        assertThat(sut.getPossibleActions(1, 0).isEmpty(), `is`(true))
    }

    @Test
    fun `Get possible actions - left and right`() {
        assertThat(sut.getPossibleActions(1, 5), `is`(listOf(Action.LEFT, Action.RIGHT)))
    }

    @Test
    fun `Get possible actions - up and down`() {
        assertThat(sut.getPossibleActions(5, 1), `is`(listOf(Action.UP, Action.DOWN)))
    }

    @Test
    fun `Total maze count`() {
        assertThat(sut.totalCell, `is`(36))
    }

    @Test
    fun `Move up`() {
        assertThat(sut.getCoordinateForAction(0, 1, Action.UP), `is`(0 to 0))
    }

    @Test
    fun `Move down`() {
        assertThat(sut.getCoordinateForAction(0, 1, Action.DOWN), `is`(0 to 2))
    }

    @Test
    fun `Move right`() {
        assertThat(sut.getCoordinateForAction(1, 1, Action.RIGHT), `is`(2 to 1))
    }

    @Test
    fun `Move left`() {
        assertThat(sut.getCoordinateForAction(1, 1, Action.LEFT), `is`(0 to 1))
    }

    @Test
    fun `Moving out of bounds stays in place`() {
        assertThat(sut.getCoordinateForAction(0, 1, Action.LEFT), `is`(0 to 1))
    }

    @Test
    fun `Moving to obstacles stays in place`() {
        assertThat(sut.getCoordinateForAction(0, 0, Action.RIGHT), `is`(0 to 0))
    }
}