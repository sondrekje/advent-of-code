package `2023`

import `2023`.GridUtils.getOptNeighbours
import org.junit.jupiter.api.Test
import java.io.File

val REGEX_DIGIT_SEQUENCE = """\d+""".toRegex()

val Char.isSymbol get() = !isDigit() && this != '.'

data class Schematic(val grid: List<List<Char>>) {
    companion object {
        fun fromRows(lines: List<String>) = Schematic(lines.map(String::toList))
    }
}

fun Schematic.hasSymbolAdjacent(row: Int, col: Int) =
    getOptNeighbours(
        row = row,
        col = col,
    ).any { (newRow, newCol) ->
        grid.getOrNull(newRow)?.getOrNull(newCol)?.isSymbol == true
    }

fun Schematic.part1() = grid.flatMapIndexed { x, row ->
    REGEX_DIGIT_SEQUENCE.findAll(row.joinToString("")).mapNotNull { matchResult ->
        val (numberString, startCol, endCol) = matchResult.valueAndRange

        if ((startCol..endCol).any { y -> hasSymbolAdjacent(x, y) }) {
            numberString.toInt()
        } else {
            null
        }
    }
}.sum()

fun Schematic.part2() = grid.flatMapIndexed { x, row ->
    row.mapIndexedNotNull { y, cell ->
        if (cell == '*') {
            val adjacentNumbers = getOptNeighbours(x, y)
                .mapNotNull { (newRow, newCol) -> findNumberAt(newRow, newCol) }
                .distinct()

            if (adjacentNumbers.size == 2) adjacentNumbers else null
        } else {
            null
        }
    }
}.sumOf { adjacentNumbers -> adjacentNumbers.reduce { acc, num -> acc * num } }

fun Schematic.findNumberAt(row: Int, col: Int): Int? {
    val rowDigits = grid.getOrNull(row) ?: return null

    val leftPart = rowDigits.take(col).reversed().takeWhile(Char::isDigit).reversed().joinToString("")
    val rightPart = rowDigits.drop(col + 1).takeWhile(Char::isDigit).joinToString("")

    return (leftPart + rowDigits.getOrNull(col) + rightPart).toIntOrNull()
}

class Day3 {

    @Test
    fun day3() {
        val input = Schematic.fromRows(File("./src/2023/Day3.txt").readLines())

        val part1 = input.part1()
        println(part1)
        val part2 = input.part2()
        println(part2)
    }

    @Test
    fun test() {
        val input = """
            |467..114..
            |...*......
            |..35..633.
            |......#...
            |617*......
            |.....+.58.
            |..592.....
            |......755.
            |...${'$'}.*....
            |.664.598..
        """.trimMargin()

        val grid = Schematic.fromRows(input.split("\n"))

        val sum = grid.part1()
        check(sum == 4361)
        val sum2 = grid.part2()
        check(sum2 == 467835)
    }
}