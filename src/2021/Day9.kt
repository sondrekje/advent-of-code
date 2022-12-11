package `2021`

import get
import neighbours
import contains
import org.junit.jupiter.api.Test
import toGrid
import java.io.File

fun getLowPoints(grid: Array<IntArray>) = grid.flatMapIndexed { y, row ->
    row.mapIndexed { x, height ->
        val allNeighboursHigher = (x to y).neighbours
            .all { coordinate -> coordinate !in grid || grid[coordinate]!! > height }
        if (allNeighboursHigher) x to y else null
    }.filterNotNull()
}

fun getBasinRecursive(
    grid: Array<IntArray>,
    point: Pair<Int, Int>,
): List<Pair<Int, Int>> = point.neighbours
    .filter { neighbour -> grid[neighbour] != 9 && (grid[neighbour] ?: 0) > grid[point]!! }
    .fold<Pair<Int, Int>, List<Pair<Int, Int>>>(emptyList()) { acc, neighbourPoint ->
        val neighbourHeight = grid[neighbourPoint]!!
        val currentHeight = grid[point]!!

        val append = if (neighbourHeight != 9 && neighbourHeight > currentHeight)
            getBasinRecursive(grid, neighbourPoint) + neighbourPoint
        else emptyList()

        acc + append
    }.distinct()

fun day9_part1(grid: Array<IntArray>) = getLowPoints(grid).sumOf { grid.get(it)!! + 1 }
fun day9_part2(grid: Array<IntArray>) = getLowPoints(grid).map { getBasinRecursive(grid, it).size + 1 }
    .sortedByDescending { it }
    .take(3)
    .reduce { total, basin -> total * basin }

class Day9Test {

    @Test
    fun day9() {
        File("src/2021/Day9.txt").readLines().let(::toGrid).let { input ->
            input.let(::day9_part1).let(::println)
            input.let(::day9_part2).let(::println)
        }
    }

    @Test
    fun day9Test() {
        """
            2199943210
            3987894921
            9856789892
            8767896789
            9899965678
        """.trimIndent().split("\n").let(::toGrid).let { grid ->
            check(day9_part1(grid) == 15)
            check(day9_part2(grid) == 1134)
        }
    }
}
