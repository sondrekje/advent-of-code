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
    fun `measureThreeWindowSum - should get the right sums`() {
        val input = listOf(199, 200, 208, 210, 200, 207, 240, 269, 260, 263)
        val expected = listOf(607, 618, 618, 617, 647, 716, 769, 792)

        assertEquals(expected, measureThreeWindowSum(input))
    }

    @Test
    fun `countDepthIncrease - three window should get the correct amount of increases`() {
        val input = listOf(199, 200, 208, 210, 200, 207, 240, 269, 260, 263)
        val expected = 5

        assertEquals(expected, countDepthIncreaseByThreeWindow(input))
    }
}