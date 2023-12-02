package `2023`

import org.junit.jupiter.api.Test
import java.io.File

enum class Color {
    Blue,
    Red,
    Green,
    ;
}

data class GameSubset(val record: Map<Color, /* Amount */ Int>) {
    companion object
}

fun GameSubset.Companion.fromGroup(lineWithGroups: String) =
    lineWithGroups.split(",").map { it.trim().split(" ") }.associate {
        val color = Color.valueOf(it[1].replaceFirstChar(Char::uppercase))
        val amount = it.first().toInt()
        color to amount
    }.let(::GameSubset)

data class Game(val id: Int, val subsets: List<GameSubset>) {
    companion object
}

fun Game.Companion.fromLine(line: String) = line.let {
    val (id, subsets) = it.substringBefore(":").filter(Char::isDigit) to
            it.substringAfter(":").split(";").map(GameSubset.Companion::fromGroup)

    Game(id.toInt(), subsets)
}

fun Game.isPossible(availableCubes: Map<Color, Int>): Boolean = subsets.all { subset ->
    subset.record.all { (color, amount) ->
        amount <= (availableCubes[color] ?: 0)
    }
}

val Game.minimumRequiredCubes: Map<Color, Int>
    get() = Color.entries.associateWith { color ->
        subsets.maxOfOrNull { it.record[color] ?: 0 } ?: 0
    }

val loadedCubes = mapOf(
    Color.Red to 12,
    Color.Green to 13,
    Color.Blue to 14,
)

class Day2 {

    @Test
    fun part1() {
        val input = File("./src/2023/Day2.txt").readLines()
        val allGames = input.map(Game::fromLine)

        val possibleGamesSum = allGames.filter { it.isPossible(loadedCubes) }
            .sumOf { it.id }

        check(possibleGamesSum == 2416)
    }

    @Test
    fun part2() {

        val input = File("./src/2023/Day2.txt").readLines()
        val allGames = input.map(Game::fromLine)

        val minimumRequired = allGames.fold(0) { acc, game ->
            acc + game.minimumRequiredCubes.values.reduce { total, value -> total * value }
        }

        println(minimumRequired)
        check(minimumRequired == 63307)
    }

    @Test
    fun testParse() {
        val toParse = """
            Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
            Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
            Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
            Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
            Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green
        """.trimIndent()

        val games = toParse.split("\n")
            .map(Game::fromLine)

        check(games.first().id == 1)
        check(games.first().subsets.first().record[Color.Blue] == 3)
        check(games.last().subsets.first().record[Color.Blue] == 1)
        check(games.last().subsets.last().record[Color.Blue] == 2)

        println(games.joinToString(separator = "\n"))

        val possibleGames = games.filter { it.isPossible(loadedCubes) }

        check(possibleGames.all { it.id in listOf(1, 2, 5) })

        val minimumCubesPerGame = games.associate {
            it.id to it.minimumRequiredCubes
        }
        println(minimumCubesPerGame)
    }
}
