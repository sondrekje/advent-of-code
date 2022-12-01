package `2021`

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day5Test {

    @Test
    fun `can parse instructions and find touched points`() {
        val instruction1 = "1,1 -> 1,3"
        val instruction2 = "9,7 -> 7,7"
        val instruction3 = "1,1 -> 3,3"
        val instruction4 = "9,7 -> 7,9"

        val expected1 = listOf(Point(1, 1), Point(1, 2), Point(1, 3))
        val expected2 = listOf(Point(7, 7), Point(8, 7), Point(9, 7))
        val expected3 = listOf(Point(1, 1), Point(2, 2), Point(3, 3))
        val expected4 = listOf(Point(7, 9), Point(8, 8), Point(9, 7))

        assertEquals(expected1, parseLineToTouchedPoints(instruction1))
        assertEquals(expected2, parseLineToTouchedPoints(instruction2))
        assertEquals(expected3, parseLineToTouchedPoints(instruction3))
        assertEquals(expected4, parseLineToTouchedPoints(instruction4))
    }

    @Test
    fun `should find most overlapping points`() {
        val input = """
            0,9 -> 5,9
            8,0 -> 0,8
            9,4 -> 3,4
            2,2 -> 2,1
            7,0 -> 7,4
            6,4 -> 2,0
            0,9 -> 2,9
            3,4 -> 1,4
            0,0 -> 8,8
            5,5 -> 8,2
        """.trimIndent()

        val touchedPoints = input.split("\n").flatMap { parseLineToTouchedPoints(it, part1IgnoreMapping) }

        assertEquals(5, touchedPoints.intersections)
    }
}
