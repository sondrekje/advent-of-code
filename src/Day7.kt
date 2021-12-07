import java.io.File
import kotlin.math.abs

val delta: (a: Int, b: Int) -> Int = { a, b -> abs(a - b) }
val deltaStep: (a: Int, b: Int) -> Int = { a, b ->
    val diff = delta(a, b)
    diff * (diff + 1) / 2
}

fun findMinimumFuelConsumption(
    nums: List<Int>,
    formula: (Int, Int) -> Int = delta,
): Map.Entry<Int, Int> {
    val minPosToTry = nums.minOrNull() ?: 0
    val maxPosToTry = nums.maxOrNull() ?: 0

    val fuelConsumptions = (minPosToTry..maxPosToTry).associateWith { pos ->
        nums.sumOf { num -> formula(pos, num) }
    }
    return fuelConsumptions.minByOrNull { it.value }!!
}

fun main() {
    val input = File("src", "Day7.txt").readText().split(",").map { it.toInt() }

    val part1 = findMinimumFuelConsumption(input).also { println(it) }
    val part2 = findMinimumFuelConsumption(input, deltaStep).also { println(it) }
}