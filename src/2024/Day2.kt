package `2024`

import readInput
private fun isSafe(report: List<Int>): Boolean {
    if (report.size < 2) return true

    val deltas = report.zipWithNext().map { (a, b) -> b - a }

    val firstDelta = deltas.firstOrNull() ?: 0
    val isIncrement = firstDelta > 0

    val validDeltas = if (isIncrement) (1..3) else (-3..-1)

    return deltas.all { it in validDeltas }
}

private fun isSafeWithDampener(report: List<Int>): Boolean {
    if (isSafe(report)) return true

    for (i in report.indices) {
        val modifiedReport = report.filterIndexed { index, _ -> index != i }
        if (isSafe(modifiedReport)) {
            return true
        }
    }

    return false
}

private fun String.reportsFromLine() = split(" ").map(String::toInt)

private fun part1(lines: List<String>): Int {
    val reports = lines.map(String::reportsFromLine)

    return reports.count(::isSafe)
}

private fun part2(lines: List<String>): Int {
    val reports = lines.map(String::reportsFromLine)

    return reports.count(::isSafeWithDampener)
}

fun main() {
    val input = readInput("Day2")
    val testInput = """
        7 6 4 2 1
        1 2 7 8 9
        9 7 6 2 1
        1 3 2 4 5
        8 6 4 4 1
        1 3 6 7 9
    """.trimIndent().split("\n")

    println(part1(testInput))
    println(part2(testInput))

    println(part1(input))
    println(part2(input))
}