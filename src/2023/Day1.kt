package `2023`

import org.junit.jupiter.api.Test
import java.io.File

private fun part1(lines: List<String>) = lines.fold(0) { acc, line ->
    val sumFirstAndLast = line.filter(Char::isDigit)
        .let { "${it.first()}${it.last()}".toInt() }
    acc + sumFirstAndLast
}

private val wordToDigit = mapOf(
    "one" to "1",
    "two" to "2",
    "three" to "3",
    "four" to "4",
    "five" to "5",
    "six" to "6",
    "seven" to "7",
    "eight" to "8",
    "nine" to "9"
)

private fun part2(lines: List<String>): Int = lines.sumOf { line ->
    val first = wordToDigit.entries
        .mapNotNull { if (it.key in line) line.indexOf(it.key) to it.value else null }
        .minByOrNull { (index, _) -> index }

    val modifiedLine = first?.let { (index, value) -> line.replaceRange(index, index + value.length, value) } ?: line

    val last = wordToDigit.entries
        .mapNotNull { if (it.key in modifiedLine) modifiedLine.lastIndexOf(it.key) to it.value else null }
        .maxByOrNull { (index, _) -> index }

    val finalLine = last?.let { (index, value) -> modifiedLine.replaceRange(index, index + value.length, value) } ?: modifiedLine

    finalLine.filter(Char::isDigit).let { "${it.first()}${it.last()}".toInt() }
}

class Day1 {
    @Test
    fun day1() {
        val input = File("src/2023/Day1.txt").readLines()

        val part1 = part1(input)
        println(part1)
        val part2 = part2(input)
        println(part2)
    }

    @Test
    fun test() {
        val lines = """
            two1nine
            eightwothree
            abcone2threexyz
            xtwone3four
            4nineeightseven2
            zoneight234
            7pqrstsixteen
        """.trimIndent().split("\n")

        val part2 = part2(lines)
        check(part2 == 281)
    }
}
