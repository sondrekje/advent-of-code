package `2021`

import java.io.File

data class BingoBoard(
    val rows: List<List<Int>>,
) {
    private val columns = (0..4).map { column -> rows.map { row -> row[column] } }
    private fun hasAnyRowMatch(selected: List<Int>) = rows.any { row -> row.all { it in selected } }
    private fun hasAnyColumnMatch(selected: List<Int>) = columns.any { column -> column.all { it in selected } }
    fun hasAnyMatch(selected: List<Int>) = hasAnyRowMatch(selected) || hasAnyColumnMatch(selected)
    companion object {
        fun fromTextBoard(board: String) = BingoBoard(
            board.split("\n").map { line ->
                val row = line.split(" ")
                    .filter(String::isNotEmpty)
                    .map { it.toInt() }

                row
            }
        )
    }
}

data class WinningGame(val board: BingoBoard, val drawn: List<Int>)
val WinningGame.winningScore: Int get() {
    val (board, drawn) = this
    val winningScore = drawn.last()
    val untouchedNums = board.rows.flatten().filter { it !in drawn }
    return untouchedNums.sum() * winningScore
}
fun List<BingoBoard>.findWinners(drawn: List<Int>): List<WinningGame> {
    val winningGames = mutableListOf<WinningGame>()
    var current = 0
    while (current < drawn.size) {
        val numsDrawnBefore = drawn.take(current)
        val currentWonBoards = winningGames.map { it.board }
        val matchingBoards = filter { it.hasAnyMatch(numsDrawnBefore) && it !in currentWonBoards }

        matchingBoards.forEach { board ->
            winningGames.add(
                WinningGame(
                    board = board,
                    drawn = numsDrawnBefore,
                )
            )
        }
        current++
    }
    return winningGames
}

fun main() {
    val gameInput = File("src", "2021/Day4.txt4.txt").readText()
    val splitGame = gameInput.split("\n")
    val drawnNums = splitGame.first().split(",").map { it.toInt() }
    val boards = gameInput.dropWhile { it != '\n' }
        .trim()
        .split("\n\n")
        .map { board ->
            BingoBoard.fromTextBoard(board)
        }

    val winners = boards.findWinners(drawnNums)
    val part1 = winners.first().winningScore.also { println(it) }

    val part2 = winners.last().winningScore.also { println(it) }
}