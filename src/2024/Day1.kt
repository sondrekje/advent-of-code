package `2024`

import readInput
import kotlin.math.abs

private const val PAIR_SEPERATOR = "   "

private fun part1(lines: List<String>): Long {
    val linesSplit = lines.map { line -> line.split(PAIR_SEPERATOR).map { it.toLong() } }
    val leftArray = linesSplit.map { it[0] }.sorted()
    val rightArray = linesSplit.map { it[1] }.sorted()

    return leftArray.zip(rightArray).sumOf { (left, right) ->
        abs(left - right)
    }
}

private fun part2(lines: List<String>): Long {
    val linesSplit = lines.map { line -> line.split(PAIR_SEPERATOR).map { it.toLong() } }
    val leftArray = linesSplit.map { it[0] }.sorted()
    val rightArray = linesSplit.map { it[1] }.sorted()

    return leftArray.fold(0L) { acc, left ->
        val count = rightArray.count { it == left }
        if (count != 0) acc + (left * count) else acc + 0
    }
}

fun main() {
    val testInput = """
        3   4
        4   3
        2   5
        1   3
        3   9
        3   3
    """.trimIndent()

    val test = testInput.split("\n").let(::part1)
    val test2 = testInput.split("\n").let(::part2)
    val part1 = readInput("Day1").let(::part1)
    val part2 = readInput("Day1").let(::part2)
    println(test)
    println(test2)
    println(part1)
    println(part2)

}
