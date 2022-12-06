package `2021`

import org.junit.jupiter.api.Test
import java.io.File

val openToClose = mapOf(
    '(' to ')',
    '[' to ']',
    '{' to '}',
    '<' to '>',
)

fun List<SyntaxParseResult>.printResult() {
    forEach { result ->
        when (result) {
            is CorruptedSyntaxError -> println("${result.line} - Expected '${result.expected}', but found '${result.found}' instead")
            is IncompleteSyntaxError -> println("${result.line} - Complete by adding '${result.unclosed.joinToString(separator = "")}'")
            else -> {}
        }
    }
}

sealed class SyntaxParseResult
data class CorruptedSyntaxError(
    val line: String,
    val expected: Char,
    val found: Char,
) : SyntaxParseResult()

data class IncompleteSyntaxError(
    val line: String,
    val unclosed: List<Char>,
) : SyntaxParseResult()

object Success : SyntaxParseResult()

val scoreTableSyntaxError = mapOf(
    ')' to 3,
    ']' to 57,
    '}' to 1197,
    '>' to 25137,
)
val scoreTableAutocomplete = listOf(')', ']', '}', '>').withIndex()
    .associate { it.value to it.index + 1 }

val List<SyntaxParseResult>.syntaxErrorScore
    get() = scoreTableSyntaxError.entries.sumOf { (token, score) ->
        filterIsInstance<CorruptedSyntaxError>().count { err -> err.found == token } * score
    }

val IncompleteSyntaxError.autoCompleteScore get() = unclosed.fold(0L) { total, syntaxError ->
    (total * 5) + scoreTableAutocomplete[syntaxError]!!
}

val List<SyntaxParseResult>.autoCompleteScore get() = filterIsInstance<IncompleteSyntaxError>()
    .map(IncompleteSyntaxError::autoCompleteScore)
    .sorted()
    .let { list -> list[list.lastIndex / 2] }

private fun parseLine(line: String): SyntaxParseResult {
    val tokens = ArrayDeque<Char>()
    line.forEach { char ->
        val peekLast = tokens.lastOrNull()
        if (char in openToClose.keys) {
            tokens.addLast(openToClose[char]!!)
        } else if (char != tokens.removeLast()) {
            return CorruptedSyntaxError(
                line = line,
                expected = peekLast!!,
                found = char,
            )
        }
    }

    return if (tokens.isEmpty()) Success else IncompleteSyntaxError(line, tokens.reversed())
}

fun parseNavigationSubsystem(lines: List<String>) = lines.map(::parseLine).also(List<SyntaxParseResult>::printResult)

val part1 = { input: List<String> -> parseNavigationSubsystem(input).syntaxErrorScore }
val part2 = { input: List<String> -> parseNavigationSubsystem(input).autoCompleteScore }

class Day10Test {

    @Test
    fun day10() {
        File("src/2021/Day10.txt").readLines().let { input ->
            input.let(part1).run(::println)
            input.let(part2).run(::println)
        }
    }

    @Test
    fun day10Test() {
        """
            [({(<(())[]>[[{[]{<()<>>
            [(()[<>])]({[<{<<[]>>(
            {([(<{}[<>[]}>{[]{[(<()>
            (((({<>}<{<{<>}{[]{[]{}
            [[<[([]))<([[{}[[()]]]
            [{[{({}]{}}([{[{{{}}([]
            {<[[]]>}<{[{[{[]{()[[[]
            [<(<(<(<{}))><([]([]()
            <{([([[(<>()){}]>(<<{{
            <{([{{}}[<[[[<>{}]]]>[]]
        """.trimIndent().split("\n").let { input ->
            check(part2(input) == 288957L)
            val subsystem = parseNavigationSubsystem(input)
            check(subsystem.syntaxErrorScore == 26397)
            check(subsystem.autoCompleteScore == 288957L)
        }
    }
}
