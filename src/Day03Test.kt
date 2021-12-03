import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day03Test {

    @Test
    fun `should calculate gamma rate`() {
        val input = listOf("00100", "11110", "10110", "10111", "10101", "01111", "00111", "11100", "10000", "11001", "00010", "01010")
        val expected = "10110"
        val expectedDecimal = 22

        val result = calculateGammaRate(input)
        assertEquals(expectedDecimal, result.toInt(2))
        assertEquals(expected, result)
    }

    @Test
    fun `should calculate epsilon rate`() {
        val input = listOf("00100", "11110", "10110", "10111", "10101", "01111", "00111", "11100", "10000", "11001", "00010", "01010")
        val expected = "01001"
        val expectedDecial = 9

        val result = calculateEpsilonRate(input)
        assertEquals(expectedDecial, result.toInt(2))
        assertEquals(expected, result)
    }

    @Test
    fun `should calculate power consumption`() {
        val input = listOf("00100", "11110", "10110", "10111", "10101", "01111", "00111", "11100", "10000", "11001", "00010", "01010")
        val expected = 198

        assertEquals(expected, calculatePowerConsumption(input))
    }

    @Test
    fun `should calculate find and calculate rating`() {
        val input = listOf("00100", "11110", "10110", "10111", "10101", "01111", "00111", "11100", "10000", "11001", "00010", "01010")
        val oxygenRating = findRating(input, findOxygenRatingCmpFn)
        val co2Rating = findRating(input, findCo2RatingCmpFn)

        assertEquals("10111", oxygenRating)
        assertEquals("01010", co2Rating)
    }
}