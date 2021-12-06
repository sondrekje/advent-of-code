import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day6Test {

    @Test
    fun `should simulate correct amount of fish`() {
        val initial = listOf("3", "4", "3", "1", "2").map { Lanternfish(it.toInt()) }

        val after18days = simulateForDays(initial, 18)
        assertEquals(26, after18days[18]!!.size)

        val after80days = simulateForDays(initial, 80)
        assertEquals(5934, after80days[80]!!.size)
    }

}