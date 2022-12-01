package `2021`

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class Day01Test {

    @Test
    fun `countDepthIncreases - should get the correct amount of increases`() {
        val input = listOf(199, 200, 208, 210, 200, 207, 240, 269, 260, 263)
        val expected = 7

        assertEquals(expected, countDepthIncreases(input))
    }

    @Test
    fun `countDepthIncreaseByThreeWindow - three window should get the correct amount of increases`() {
        val input = listOf(199, 200, 208, 210, 200, 207, 240, 269, 260, 263)
        val expected = 5

        assertEquals(expected, countDepthIncreaseByThreeWindow(input))
    }
}