package `2023`

import org.junit.jupiter.api.Test
import java.io.File

data class Race(
    val time: Long,
    val distance: Long,
) {
    companion object {
        fun parseRaces(input: String) = input.split("\n")
            .map {
                it.substringAfter(":").split(" ")
                    .filter(String::isNotBlank)
                    .map(String::toLong)
            }
            .let { (times, distances) ->
                times.zip(distances).map { (time, distance) ->
                    Race(time, distance)
                }
            }
        fun parseSingleRace(input: String) = input.split("\n").let { (timeStr, distanceStr) ->
            Race(
                time = timeStr.filter(Char::isDigit).toLong(),
                distance = distanceStr.filter(Char::isDigit).toLong(),
            )
        }
    }
}

val Race.winningWays get() = (1 until time).count { holdFor -> gainedDistance(time, holdFor) > distance }

private fun gainedDistance(time: Long, holdFor: Long): Long =
    if (holdFor >= time) 0 else (time - holdFor) * holdFor

class Day6 {

    @Test
    fun day6() {
        val parsed = Race.parseRaces(File("./src/2023/Day6.txt").readText())

        val part1 = parsed.fold(1) { acc, cur -> acc * cur.winningWays }
        val part2 = Race.parseSingleRace(File("./src/2023/Day6.txt").readText()).winningWays
        check(part1 == 170000)
        check(part2 == 20537782)
    }

    @Test
    fun test() {
        val input = """
            Time:      7  15   30
            Distance:  9  40  200
        """.trimIndent()

        val parsed = Race.parseRaces(input)
        println(parsed)

        val part1 = parsed.fold(1) { acc, cur -> acc * cur.winningWays }
        check(part1 == 288)
        val part2 = Race.parseSingleRace(input).winningWays
        check(part2 == 71503)
    }
}