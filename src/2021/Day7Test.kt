package `2021`

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day7Test {

    @Test
    fun `should calculate fuel consumption`() {
        val inputNums = "16,1,2,0,4,2,7,1,2,14".split(",").map { it.toInt() }

        val fuelPart1 = findMinimumFuelConsumption(inputNums)
        val fuelPart2 = findMinimumFuelConsumption(inputNums, deltaStep)
        assertEquals(2, fuelPart1.key)
        assertEquals(5, fuelPart2.key)
        assertEquals(168, fuelPart2.value)
    }
}