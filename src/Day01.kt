fun countDepthIncreases(depths: List<Int>) = depths.mapIndexedNotNull { index, num ->
    depths.getOrNull(index + 1)?.let {
        if (it > num) num else null
    }
}.size

fun measureThreeWindowSum(depths: List<Int>): List<Int> = depths.mapIndexedNotNull { index, i ->
    val next = depths.getOrNull(index + 1)
    depths.getOrNull(index + 2)?.let { last ->
        i + next!! + last
    }
}

fun countDepthIncreaseByThreeWindow(depths: List<Int>) = countDepthIncreases(measureThreeWindowSum(depths))

fun main() {
    val day1Int = readInput("Day01").map(String::toInt)
    val part1 = countDepthIncreases(day1Int).also { println(it) } // 1532

    val part2 = countDepthIncreaseByThreeWindow(day1Int).also { println(it) }
}