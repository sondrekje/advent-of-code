package `2022`

import org.junit.jupiter.api.Test
import java.io.File

class Day10Test {

    @Test
    fun day10() {
        File("src/2022/Day10.txt").readLines().let(::parseInstructionsToValuePerCycle).let { input ->
            println(part1_day10(input))
            println(part2_day10(input))
        }
    }

    @Test
    fun day10Test() {
        """
            addx 15
            addx -11
            addx 6
            addx -3
            addx 5
            addx -1
            addx -8
            addx 13
            addx 4
            noop
            addx -1
            addx 5
            addx -1
            addx 5
            addx -1
            addx 5
            addx -1
            addx 5
            addx -1
            addx -35
            addx 1
            addx 24
            addx -19
            addx 1
            addx 16
            addx -11
            noop
            noop
            addx 21
            addx -15
            noop
            noop
            addx -3
            addx 9
            addx 1
            addx -3
            addx 8
            addx 1
            addx 5
            noop
            noop
            noop
            noop
            noop
            addx -36
            noop
            addx 1
            addx 7
            noop
            noop
            noop
            addx 2
            addx 6
            noop
            noop
            noop
            noop
            noop
            addx 1
            noop
            noop
            addx 7
            addx 1
            noop
            addx -13
            addx 13
            addx 7
            noop
            addx 1
            addx -33
            noop
            noop
            noop
            addx 2
            noop
            noop
            noop
            addx 8
            noop
            addx -1
            addx 2
            addx 1
            noop
            addx 17
            addx -9
            addx 1
            addx 1
            addx -3
            addx 11
            noop
            noop
            addx 1
            noop
            addx 1
            noop
            noop
            addx -13
            addx -19
            addx 1
            addx 3
            addx 26
            addx -30
            addx 12
            addx -1
            addx 3
            addx 1
            noop
            noop
            noop
            addx -9
            addx 18
            addx 1
            addx 2
            noop
            noop
            addx 9
            noop
            noop
            noop
            addx -1
            addx 2
            addx -37
            addx 1
            addx 3
            noop
            addx 15
            addx -21
            addx 22
            addx -6
            addx 1
            noop
            addx 2
            addx 1
            noop
            addx -10
            noop
            noop
            addx 20
            addx 1
            addx 2
            addx 2
            addx -6
            addx -11
            noop
            noop
            noop
        """.trimIndent().split("\n").let(::parseInstructionsToValuePerCycle).let { input ->
            check(part1_day10(input) == 13140)
            check(input[60] == 19)
            check(input[100] == 18)
            check(input[140] == 21)
            check(input[180] == 16) { "180" }
            check(input[220] == 18) { "220" }
        }
    }
}

fun part1_day10(cyclesWithValues: List<Int>): Int {
    val relevantCycles = (20..20).toList() + (60..220 step 40)
    return relevantCycles.sumOf { cyclesWithValues[it] * it }
}

fun part2_day10(cyclesWithValues: List<Int>) = (cyclesWithValues.drop(1)).foldIndexed("") { i, built, value ->
    built + when {
        (i % 40) == 0 && i != 0 -> "\n"
        (i % 40) - value in -1..1 -> "█"
        else -> " "
    }
}

fun parseInstructionsToValuePerCycle(instructions: List<String>): List<Int> = (listOf(0, 1) + instructions.flatMap { instruction ->
    if ("addx" !in instruction) listOf(0)
    else listOf(0, instruction.split(" ").last().toInt())
}).runningReduce(Int::plus)
