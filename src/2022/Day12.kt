package `2022`

import neighbours
import org.junit.jupiter.api.Test
import java.io.File
import java.util.*

class Day12Test {

    @Test
    fun day12() {
        File("src/2022/Day12.txt").readLines().toGrid().let {
            val part1 = it.part1()
            println(part1.size)
            println(it.part2().size)
        }
    }

    @Test
    fun testDay12() {
        """
            Sabqponm
            abcryxxl
            accszExk
            acctuvwj
            abdefghi
        """.trimIndent().split("\n").toGrid().let { grid ->
            val part1 = grid.part1()
            check(part1.size == 31)
            check(grid.findShortestPathRecursive().size == part1.size)
            val part2 = grid.part2()
            println(part2.size)
        }
    }
}

fun Grid.part1(
    source: Point = everyPoint().single { (char) -> char == 'S' }.second,
    target: Point = everyPoint().single { (char) -> char == 'E' }.second,
) = findShortestPath(source, target)

fun Grid.part2(
    sources: List<Point> = everyPoint().filter { (char) -> char.elevation == 0 }.map { (_, point) -> point },
    target: Point = everyPoint().single { (char) -> char == 'E' }.second,
) = sources.map { source -> part1(source, target) }
    .filter(List<Point>::isNotEmpty)
    .minBy(List<Point>::size)

fun Grid.findShortestPath(
    source: Point = everyPoint().single { (char) -> char == 'S' }.second,
    target: Point = everyPoint().single { (char) -> char == 'E' }.second,
): List<Point> {
    val queue = LinkedList<Point>().apply { add(source) }
    val visited = mutableSetOf<Point>().apply { add(source) }
    val path = mutableMapOf<Point, Point>()

    var current: Point? = null
    while (queue.isNotEmpty()) {
        current = queue.poll()
        if (current == target) {
            break
        }
        current.validPathSquares(this).filterNot(visited::contains).forEach { neighbour ->
            queue.add(neighbour)
            visited.add(neighbour)
            path[neighbour] = current
        }
    }

    return if (current == target) { path.path(source, target) } else emptyList()
}

fun Grid.findShortestPathRecursive(
    source: Point = everyPoint().single { (char) -> char == 'S' }.second,
    target: Point = everyPoint().single { (char) -> char == 'E' }.second,
): List<Point> {
    tailrec fun search(
        queue: List<Point>,
        visited: Set<Point>,
        path: Map<Point, Point>,
        acc: List<Point>,
    ): List<Point> {
        val current = queue.firstOrNull() ?: return emptyList()
        if (current == target) {
            return path.path(source, target)
        }
        val unvisitedNeighbours = current.validPathSquares(this).filterNot(visited::contains)
        return search(
            queue = queue.drop(1) + unvisitedNeighbours,
            visited = visited + current,
            path = path + unvisitedNeighbours.associateWith { current },
            acc = acc + current,
        )
    }
    return search(listOf(source), setOf(source), emptyMap(), emptyList())
}

fun Map<Point, Point>.path(
    source: Point,
    target: Point,
    includeSourceInPath: Boolean = false,
): List<Point> {
    val result = mutableListOf<Point>()
    var node = target
    while (node != source) {
        result.add(node)
        node = this[node]!!
    }
    result.reverse()
    if (includeSourceInPath) {
        result.add(source)
    }
    return result
}

private typealias Grid = Array<CharArray>

private fun List<String>.toGrid(): Grid = map(String::toCharArray).toTypedArray()
private typealias Point = Pair<Int, Int>

private operator fun Grid.contains(coordinate: Pair<Int, Int>): Boolean {
    val (x, y) = coordinate
    return (y in indices && x in this[y].indices)
}

private operator fun Grid.get(point: Point) = if (point in this) this[point.second][point.first] else null

private fun Grid.everyPoint(): List<Pair<Char, Point>> =
    flatMapIndexed { y, rows ->
        rows.mapIndexed { x, value -> value to (x to y) }
    }

private val Char.elevation: Int
    get() = when (this) {
        'S' -> 0
        'E' -> 'z'.elevation + 1
        else -> this - 'a'
    }

private fun Point.validPathSquares(grid: Grid): List<Point> = neighbours
    .filter { grid[it] != null }
    .filter { (grid[it]!!.elevation - grid[this]!!.elevation) <= 1 }
