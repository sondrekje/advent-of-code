data class Point(val x: Int, val y: Int)

val part1IgnoreMapping: (startX: Int, startY: Int, endX: Int, endY: Int) -> Boolean = { startX, startY, endX, endY ->
    !((startX == endX) || (startY == endY))
}

fun handlePoint(startX: Int, startY: Int, endX: Int, endY: Int): List<Point> {
    if (startX == endX) return (startY..endY).map { Point(startX, it) }
    if (startY == endY) return (startX..endX).map { Point(it, startY) }

    val deltaX = endX - startX
    val deltaY = endY - startY
    val sign = if (deltaY > 0) 1 else if (deltaY < 0) -1 else 0
    return (0..deltaX).map { delta ->
        Point(startX + delta, startY + sign * delta)
    }
}

fun parseLineToTouchedPoints(
    line: String,
    ignoreMapping: (startX: Int, startY: Int, endX: Int, endY: Int) -> Boolean = { _, _, _, _ -> false },
): List<Point> {
    val (start, end) = line.split("->").map { it.trim() }
    val (startX, startY) = start.split(",").map { it.toInt() }
    val (endX, endY) = end.split(",").map { it.toInt() }
    if (ignoreMapping(startX, startY, endX, endY)) return emptyList()

    return if (
        startX < endX ||
        startX == endX && startY < endY
    ) handlePoint(startX, startY, endX, endY) else handlePoint(endX, endY, startX, startY)
}

val List<Point>.intersections: Int
    get() = groupingBy { it }.eachCount().filterValues { it >= 2 }.size

fun main() {
    val input = readInput("Day5")

    val part1 = input.flatMap { parseLineToTouchedPoints(it, part1IgnoreMapping) }.also { println(it.intersections) }
    val part2 = input.flatMap { parseLineToTouchedPoints(it) }.also { println(it.intersections) }
}