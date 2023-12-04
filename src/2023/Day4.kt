package `2023`

import org.junit.jupiter.api.Test
import java.io.File

data class ScratchCard(
    val id: Int,
    val winningNumbers: List<Int>,
    val containedNumbers: List<Int>,
) {
    companion object {
        fun fromLine(line: String): ScratchCard {
            val (id, numbers) = line.split(":")
            val (winning, contained) = numbers.split("|").map {
                it.trim().split("\\s+".toRegex()).map(String::toInt)
            }

            return ScratchCard(
                id = id.filter(Char::isDigit).toInt(),
                winningNumbers = winning,
                containedNumbers = contained
            )
        }
    }
}

val ScratchCard.score: Int
    get() = containedNumbers.fold(null) { acc: Int?, currentNumber ->
        if (currentNumber in winningNumbers) {
            acc?.let { acc * 2 } ?: 1
        } else acc
    } ?: 0

val ScratchCard.winsCopies get() = containedNumbers.count { it in winningNumbers }

fun List<ScratchCard>.generateCards() = sequence {
    val templates = this@generateCards.associateBy(ScratchCard::id)
    val queue = ArrayDeque(this@generateCards)

    while (queue.isNotEmpty()) {
        val currentCard = queue.removeFirst()
        yield(currentCard)

        repeat(currentCard.winsCopies) { iteration ->
            val newCardId = currentCard.id + (iteration + 1)
            val templateCard = templates[newCardId]

            templateCard?.let {
                queue.addLast(it)
            }
        }
    }
}

class Day4 {

    @Test
    fun day4() {
        val input = File("./src/2023/Day4.txt").readLines()
            .map(ScratchCard::fromLine)

        val part1 = input.sumOf(ScratchCard::score)
        println(part1)
        val part2 = input.generateCards().toList().size
        println(part2)
        check(part2 == 9425061)
    }

    @Test
    fun test() {
        val input = """
            Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
            Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19
            Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1
            Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83
            Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36
            Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11
        """.trimIndent().split("\n")

        val parsed = input.map(ScratchCard::fromLine)

        println(parsed)

        val scoreDay1 = parsed.sumOf(ScratchCard::score)
        check(scoreDay1 == 13)
        val day2 = parsed.generateCards()
        check(day2.toList().size == 30)
    }
}