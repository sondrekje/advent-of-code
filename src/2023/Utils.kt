package `2023`

enum class Direction(val dx: Int, val dy: Int) {
    UP(-1, 0),
    DOWN(1, 0),
    LEFT(0, -1),
    RIGHT(0, 1),
    UPPER_LEFT(-1, -1),
    UPPER_RIGHT(-1, 1),
    LOWER_LEFT(1, -1),
    LOWER_RIGHT(1, 1),
    ;
}

val MatchResult.valueAndRange get() = Triple(value, range.first, range.last)

object GridUtils {
    fun getOptNeighbours(row: Int, col: Int): List<Pair<Int, Int>> =
        Direction.entries.map { dir ->
            val newRow = (row + dir.dx)
            val newCol = (col + dir.dy)
            newRow to newCol
        }
}
