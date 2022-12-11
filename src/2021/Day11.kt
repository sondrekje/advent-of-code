package `2021`

import get
import set
import contains
import Grid
import allNeighbours
import debugPrint
import everyPoint
import org.junit.jupiter.api.Test
import toGrid
import java.io.File

fun steps(grid: Grid, maxSteps: Int = 100): Pair<Int, Int?> {
    var steps = 0
    var flashes = 0
    var firstStepEveryoneFlashed: Int? = null
    while (true) {
        if (steps == maxSteps) {
            break
        }
        grid.everyPoint().forEach { (_, octopus) ->
            grid[octopus] = grid[octopus]!! + 1
        }
        do {
            val flashers = grid.everyPoint().filter { (level) -> level > 9 }
            flashers.forEach { (_, point) ->
                grid[point] = 0
                flashes++
            }

            flashers.map { (_, point) ->
                point.allNeighbours
                    .filter { it in grid && grid[it] != 0 }
                    .forEach {
                        grid[it] = grid[it]!! + 1
                    }
            }
            grid.debugPrint(flashers.map(Pair<Int, Pair<Int, Int>>::second))
        } while (flashers.isNotEmpty())
        steps++
        if (firstStepEveryoneFlashed == null && grid.everyPoint().all { (level) -> level == 0 }) {
            firstStepEveryoneFlashed = steps
        }
    }
    return flashes to firstStepEveryoneFlashed
}

class Day11Test {

    @Test
    fun day11() {
        File("src/2021/Day11.txt").readLines().let { input ->
            println(steps(toGrid(input)).first)
            println(steps(toGrid(input), 500).second)
        }
    }

    @Test
    fun day11Test() {
        val input = """
            5483143223
            2745854711
            5264556173
            6141336146
            6357385478
            4167524645
            2176841721
            6882881134
            4846848554
            5283751526
        """.trimIndent().split("\n")

        check(steps(toGrid(input), 200).second == 195)
        check(steps(toGrid(input)).first == 1656)
    }
}
