fun countDepthIncreases(depths: List<Int>) = depths.zipWithNext().count { (a, b) -> a < b }

fun measureThreeWindowSum(depths: List<Int>): List<Int> = depths
    .windowed(3) { it.sum() }

fun countDepthIncreaseByThreeWindow(depths: List<Int>) = countDepthIncreases(measureThreeWindowSum(depths))

fun main() {
    val day1Int = readInput("Day01").map(String::toInt)
    val part1 = countDepthIncreases(day1Int).also { println(it) }

    val part2 = countDepthIncreaseByThreeWindow(day1Int).also { println(it) }
}