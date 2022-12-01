package `2021`

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day02Test {

    @Test
    fun `parseInstructions part 1 - should find right position`() {
        val instructions = listOf(
            "forward 5",
            "down 5",
            "forward 8",
            "up 3",
            "down 8",
            "forward 2",
        )
        val expectedDepth = 10
        val expectedHorizontal = 15
        val expectedPosition = expectedDepth * expectedHorizontal

        assertEquals(expectedPosition, calculatePosition(instructions))
    }

    @Test
    fun `parseInstructions part 2 - should find right position`() {
        val instructions = listOf(
            "forward 5",
            "down 5",
            "forward 8",
            "up 3",
            "down 8",
            "forward 2",
        )

        val expectedDepth = 60
        val expectedHorizontal = 15
        val expectedPosition = expectedDepth * expectedHorizontal

        assertEquals(expectedPosition, calculatePosition2(instructions))
    }
}