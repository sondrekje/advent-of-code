package `2021`

import org.junit.jupiter.api.Test
import java.io.File

fun part1(signalsAndOutput: List<Pair<List<String>, List<String>>>) =
    signalsAndOutput.sumOf { (_, output) -> output.count { it.length in listOf(2, 3, 4, 7) } }

fun part2(signalsAndOutput: List<Pair<List<String>, List<String>>>) = signalsAndOutput.sumOf { (signals, output) ->
    val signalsAsChars = signals.map { it.toSet() }

    val one = signalsAsChars.first { it.size == 2 }
    val four = signalsAsChars.first { it.size == 4 }
    val seven = signalsAsChars.first { it.size == 3 }
    val eight = signalsAsChars.first { it.size == 7 }

    val top = seven - four
    val bottomLeftCorner = eight - seven - four
    val right = one - top
    val midLeftUpperCorner = eight - bottomLeftCorner - right - top

    output.map { value ->
        val chars = value.toList()
        when {
            value.length == 6 && right.all { it in chars } && !midLeftUpperCorner.all { it in chars } -> 0
            value.length == 6 && !right.all { it in chars } && right.any { it in chars } -> 6
            value.length == 6 -> 9
            value.length == 5 && midLeftUpperCorner.all { it in chars } -> 5
            value.length == 5 && right.all { it in chars } -> 3
            value.length == 5 -> 2
            value.length == 2 -> 1
            value.length == 4 -> 4
            value.length == 3 -> 7
            value.length == 7 -> 8
            else -> error("")
        }
    }.joinToString(separator = "").toInt()
}

class Day8Test {
    @Test
    fun day8() {
        File("src/2021/Day8.txt").readLines().toListOfSignalAndOutput().let { input ->
            input.let(::part1).let(::println)
            input.let(::part2).let(::println)
        }
    }

    @Test
    fun day8Test() {
        """
            be cfbegad cbdgef fgaecd cgeb fdcge agebfd fecdb fabcd edb | fdgacbe cefdb cefbgd gcbe
            edbfga begcd cbg gc gcadebf fbgde acbgfd abcde gfcbed gfec | fcgedb cgb dgebacf gc
            fgaebd cg bdaec gdafb agbcfd gdcbef bgcad gfac gcb cdgabef | cg cg fdcagb cbg
            fbegcd cbd adcefb dageb afcb bc aefdc ecdab fgdeca fcdbega | efabcd cedba gadfec cb
            aecbfdg fbg gf bafeg dbefa fcge gcbea fcaegb dgceab fcbdga | gecf egdcabf bgf bfgea
            fgeab ca afcebg bdacfeg cfaedg gcfdb baec bfadeg bafgc acf | gebdcfa ecba ca fadegcb
            dbcfg fgd bdegcaf fgec aegbdf ecdfab fbedc dacgb gdcebf gf | cefg dcbef fcge gbcadfe
            bdfegc cbegaf gecbf dfcage bdacg ed bedf ced adcbefg gebcd | ed bcgafe cdgba cbgef
            egadfb cdbfeg cegd fecab cgb gbdefca cg fgcdab egfdb bfceg | gbdfcae bgc cg cgb
            gcafb gcf dcaebfg ecagb gf abcdeg gaef cafbge fdbac fegbdc | fgae cfgab fg bagce
        """.trimIndent().split("\n").toListOfSignalAndOutput().let { input ->
            check(part1(input) == 26)
            check(part2(input) == 61229)
        }
    }
}

fun List<String>.toListOfSignalAndOutput() = map { line ->
    val (signals, output) = line.split(" | ")
    signals.split(" ") to output.split(" ")
}
