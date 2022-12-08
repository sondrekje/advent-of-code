import `2021`.everyPoint
import `2021`.toGrid
import `2021`.Grid
import `2021`.get
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.math.abs

class Day8Test {

    @Test
    fun day8() {
        File("src/2022/Day8.txt").readLines().let(::toGrid).let {
            println(getVisibleTrees(it).size)
            println(getHighestPossibleScenicScore(it))
        }
    }

    @Test
    fun day8Test() {
        val grid = """
            30373
            25512
            65332
            33549
            35390
        """.trimIndent().split("\n").let(::toGrid)
        val visibleTrees = getVisibleTrees(grid)
        check(visibleTrees.size == 21)
        check(getHighestPossibleScenicScore(grid) == 8)
    }
}

fun getHighestPossibleScenicScore(grid: Grid) = grid.everyPoint()
    .map { (_, point) -> getScenicScoreForTree(grid, point) }
    .maxOf { it }

fun getScenicScoreForTree(grid: Grid, point: Pair<Int, Int>) = grid
        .allLines(point)
        .map { lines ->
            val toCheck = lines.filterNot { it == point }
                .takeWhile { grid[it] == null || (grid[it]!! < grid[point]!!) }

            val firstHigher = lines.filterNot { it == point }.firstOrNull {
                !(grid[it] == null || (grid[it]!! < grid[point]!!))
            } ?: return@map toCheck.size

            val delta = abs(firstHigher.first - point.first).coerceAtLeast(abs(firstHigher.second - point.second))
            delta
        }.reduce { acc, it -> acc * it }

fun getVisibleTrees(grid: Grid) = grid.everyPoint()
    .filterNot { it.second in grid.edges }
    .filter { (height, point) ->
        grid.allLines(point)
            .any { otherPoints ->
                val toCheck = otherPoints.filterNot { it == point }

                toCheck.all { grid[it] == null || (grid[it]!! < height) }
            }
    } + grid.edges

fun Grid.allLines(start: Pair<Int, Int>) = listOf(
    ::topLine,
    ::leftLine,
    ::bottomLine,
    ::rightLine,
).map { fn -> fn(start) }

fun Grid.leftLine(start: Pair<Int, Int>) = (start.first downTo 0).map { x -> x to start.second }
fun Grid.topLine(start: Pair<Int, Int>) = (start.second downTo 0).map { y -> start.first to y }
fun Grid.rightLine(start: Pair<Int, Int>) = (start.first..indices.last).map { x -> x to start.second }
fun Grid.bottomLine(start: Pair<Int, Int>) = (start.second..last().indices.last).map { y -> start.first to y }

val Grid.maxX get() = indices.last
val Grid.maxY get() = first().indices.last

val Grid.edges
    get(): List<Pair<Int, Int>> {
        val topEdge = indices.map { x -> x to 0 }
        val rightEdge = first().indices.map { y -> maxY to y }
        val bottomEdge = indices.map { x -> x to maxY }
        val leftEdge = first().indices.map { y -> 0 to y }
        return (topEdge + rightEdge + bottomEdge + leftEdge).distinct()
    }
