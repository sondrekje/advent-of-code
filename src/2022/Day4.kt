package `2022`

import org.junit.jupiter.api.Test
import java.io.File
import java.lang.Integer.max
import java.lang.Integer.min

fun parseAssignmentPair(pairRaw: String) = pairRaw.split(",")
    .map { assignment ->
        val (start, end) = assignment.split("-").map(String::toInt).sorted()
        start..end
    }
fun parseAssignmentPairs(assignmentPairsRaw: List<String>) = assignmentPairsRaw.map(::parseAssignmentPair)

fun day4_part1(assignmentPairsRaw: List<String>) = parseAssignmentPairs(assignmentPairsRaw)
    .filter { pair ->
        val (first, second) = pair
        first in second || second in first
    }

fun day4_part2(assignmentPairsRaw: List<String>) = parseAssignmentPairs(assignmentPairsRaw)
    .filter { pair ->
        val (first, second) = pair
        first in second || second in first || first.overlaps(second) || second.overlaps(first)
    }

class Day4Test {

    @Test
    fun day4() {
        File("src/2022/Day4.txt").readLines().let { input ->
            day4_part1(input).count().let(::println)
            day4_part2(input).count().let(::println)
        }
    }

    @Test
    fun test_day4() {
        """
            2-4,6-8
            2-3,4-5
            5-7,7-9
            2-8,3-7
            6-6,4-6
            2-6,4-8
        """.trimIndent().split("\n").run {
            println(day4_part1(this))
            println(day4_part2(this))
        }
    }

    @Test
    fun tmp() {
        check(3..7 in 2..8) { "1" }
        check(6..6 in 4..6) { "2" }

        check(5..7 !in 7..9) { "3" }
        check(7..9 !in 5..7) { "4" }

        check(2..6 !in 4..8) { "5" }
        check(4..8 !in 2..6) { "6" }
    }
}

operator fun IntRange.contains(other: IntRange) = ((first <= other.last && last >= other.first) && (first <= other.first && last >= other.last))
fun IntRange.overlaps(other: IntRange) = intersect(other).isNotEmpty()
