package utils

import org.hamcrest.CoreMatchers.`is`
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

    private fun testRequire(block: () -> Unit) {
        expectedException.expect(IllegalArgumentException::class.java)
        block()
    }
}