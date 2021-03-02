package utils

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import java.lang.IllegalArgumentException

class CoordinateManagerTest {
    private lateinit var sut: CoordinateManager

    @get:Rule
    var expectedException: ExpectedException = ExpectedException.none()

    companion object {
        private const val COLUMN = 6
        private const val ROW = 6
    }

    @Before
    fun before() {
        sut = CoordinateManager(column = COLUMN, row = ROW)
    }

    @Test
    fun `Coordinate to index`() {
        assertThat(sut.toIndex(3, 5), `is`(33))
    }

    @Test
    fun `Index to coordinate`() {
        assertThat(sut.toCoordinate(33), `is`(3 to 5))
    }

    @Test
    fun `Negative coordinate throws error`() {
        testRequire { sut.toIndex(-1, 2) }
    }

    @Test
    fun `Out of bounds coordinate throws error`() {
        testRequire { sut.toIndex(7, 5) }
    }

    @Test
    fun `Negative index throws error`() {
        testRequire { sut.toCoordinate(-1) }
    }

    @Test
    fun `Out of bounds index throws error`() {
        testRequire { sut.toCoordinate(36) }
    }

    @Test
    fun `Get coordinate above exist`() {
        assertThat(sut.getCoordinateAbove(3, 3), `is`(3 to 2))
    }

    @Test
    fun `Get coordinate above not exist`() {
        assertThat(sut.getCoordinateAbove(3, 0), `is`(nullValue()))
    }

    @Test
    fun `Get coordinate below exist`() {
        assertThat(sut.getCoordinateBelow(3, 3), `is`(3 to 4))
    }

    @Test
    fun `Get coordinate below not exist`() {
        assertThat(sut.getCoordinateBelow(3, 5), `is`(nullValue()))
    }

    @Test
    fun `Get coordinate left exist`() {
        assertThat(sut.getCoordinateLeft(3, 3), `is`(2 to 3))
    }

    @Test
    fun `Get coordinate left not exist`() {
        assertThat(sut.getCoordinateLeft(0, 0), `is`(nullValue()))
    }

    @Test
    fun `Get coordinate right exist`() {
        assertThat(sut.getCoordinateRight(3, 3), `is`(4 to 3))
    }

    @Test
    fun `Get coordinate right not exist`() {
        assertThat(sut.getCoordinateRight(5, 0), `is`(nullValue()))
    }

    private fun testRequire(block: () -> Unit) {
        expectedException.expect(IllegalArgumentException::class.java)
        block()
    }
}