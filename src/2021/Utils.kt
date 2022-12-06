package `2021`

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

val Pair<Int, Int>.diagonalNeighbours get(): List<Pair<Int, Int>> {
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

fun Array<IntArray>.debugPrint(highlight: List<Pair<Int, Int>> = emptyList()) {
    println("")
    forEachIndexed { y, row ->
        row.mapIndexed { x, value ->
            val coordinate = x to y
            if (coordinate in highlight) {
                print("\u001B[31m$value\u001B[0m")
            } else
            print(value)
        }
        println()
    }
    println("")
}