import java.io.File

fun readInput(name: String, year: Long = 2024) = File("src", "$year/$name.txt").readLines()

object Ansi {
    const val RESET = "\u001B[0m"
    const val BLACK = "\u001B[30m"
    const val RED = "\u001B[31m"
    const val GREEN = "\u001B[32m"
    const val YELLOW = "\u001B[33m"
    const val BLUE = "\u001B[34m"
    const val PURPLE = "\u001B[35m"
    const val CYAN = "\u001B[36m"
    const val WHITE = "\u001B[37m"

}


operator fun Grid.get(point: Pair<Int, Int>) =
    if (point in this) this[point.second][point.first] else null

operator fun Grid.set(point: Pair<Int, Int>, value: Int) {
    this[point.second][point.first] = value
}

operator fun Grid.contains(coordinate: Pair<Int, Int>): Boolean {
    val (x, y) = coordinate
    return (y in indices && x in this[y].indices)
}

val Pair<Int, Int>.neighbours
    get(): List<Pair<Int, Int>> {
        val (x, y) = this
        return listOf(
            x to y - 1, // above
            x to y + 1, // below
            x - 1 to y, // left
            x + 1 to y, // right
        )
    }

val Pair<Int, Int>.diagonalNeighbours
    get(): List<Pair<Int, Int>> {
        val (x, y) = this
        return listOf(
            x + 1 to y - 1, // upper right
            x + 1 to y + 1, // lower right
            x - 1 to y + 1, // lower left
            x - 1 to y - 1, // upper left
        )
    }

val Pair<Int, Int>.allNeighbours get() = neighbours + diagonalNeighbours

typealias Grid = Array<IntArray>

fun Grid.forEvery(fn: (point: Pair<Int, Int>) -> Unit) {
    forEachIndexed { y, row ->
        row.forEachIndexed { x, _ ->
            fn(x to y)
        }
    }
}

fun Grid.everyPoint(): List<Pair<Int, Pair<Int, Int>>> =
    flatMapIndexed { y, rows ->
        rows.mapIndexed { x, value -> value to (x to y) }
    }

fun toGrid(input: List<String>) = input.map { row -> row.map(Char::digitToInt).toIntArray() }.toTypedArray()

fun Array<IntArray>.debugPrint(
    highlightList: List<Pair<Int, Int>>? = null,
    highlightMap: Map<String, Pair<Int, Int>> = highlightList?.let {
        (0..(it.size)).mapIndexed { index, it -> "$index" to highlightList[it] }.toMap()
    } ?: emptyMap(),
) {
    println("")

    val ansiColors = listOf(Ansi.RED, Ansi.GREEN, Ansi.YELLOW, Ansi.BLUE, Ansi.PURPLE)
    check(highlightMap.size < ansiColors.size)
    val pointWithOptColor: (point: Pair<Int, Int>, value: Int) -> String? = { point, value ->
        if (point in highlightMap.values) {
            val colorIndex = highlightMap.entries.indexOfFirst { (_, it) -> it == point }
            "${ansiColors[colorIndex]}${value}${Ansi.RESET}"
        } else "$value"
    }

    forEachIndexed { y, row ->
        row.forEachIndexed { x, value ->
            val coordinate = x to y
            print(pointWithOptColor(coordinate, value))
        }
        println()
    }
}