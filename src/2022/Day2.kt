package `2022`

import `2022`.RoundResult.LOSS
import `2022`.RoundResult.DRAW
import `2022`.RoundResult.WIN
import org.junit.jupiter.api.Test
import java.io.File

enum class RoundResult(val from: Char, val score: Int) {
    LOSS('X', 0),
    DRAW('Y', 3),
    WIN('Z', 6),
    ;
}
enum class Move(
    val from: Set<Char>,
    val score: Int,
) {
    ROCK(setOf('A', 'X'), 1),
    PAPER(setOf('B', 'Y'), 2),
    SCISSORS(setOf('C', 'Z'), 3),
    ;

    infix fun beats(other: Move) = when {
        this == ROCK && other == SCISSORS -> WIN
        this == PAPER && other == ROCK -> WIN
        this == SCISSORS && other == PAPER -> WIN
        this == other -> DRAW
        else -> LOSS
    }

    companion object {
        fun fromRound_part1(line: String) = line.split(" ").let { (opponent, me) ->
            val from = { char: Char -> Move.values().single { char in it.from } }

            from(opponent.single()) to from(me.single())
        }
        fun fromRound_part2(line: String) = line.split(" ").let { (opponent, desiredResult) ->
            val move = Move.values().single { opponent.single() in it.from }
            move to RoundResult.values().single { it.from == desiredResult.single() }
        }
    }
}

fun Pair<Move, Move>.evaluateScore(): Int {
    val (opponentsMove, myMove) = this
    val matchScore = (myMove beats opponentsMove).score
    return matchScore + myMove.score
}

fun Pair<Move, RoundResult>.evaluteScorePart2(): Int {
    val (opponentsMove, desiredResult) = this

    val myMove = Move.values().single { it beats opponentsMove == desiredResult }
    return myMove.score + desiredResult.score
}

fun day2_part1(input: String) = input
    .split("\n")
    .map(Move::fromRound_part1)
    .fold(0) { total, round -> total + round.evaluateScore() }
fun day2_part2(input: String) = input
    .split("\n")
    .map(Move::fromRound_part2)
    .fold(0) { total, round -> total + round.evaluteScorePart2() }

class Day2Test {

    @Test
    fun day2() {
        File("src/2022/Day2.txt").readText().let { input ->
            input.let(::day2_part1).run(::println)
            input.let(::day2_part2).run(::println)
        }
    }

    @Test
    fun day2Test() {
        """
            A Y
            B X
            C Z
        """.trimIndent().let { input ->
            check(day2_part1(input) == 15)
            check(day2_part2(input) == 12)
        }
    }
}