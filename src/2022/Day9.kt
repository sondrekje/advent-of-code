package `2022`

import org.junit.jupiter.api.Test
import java.io.File
import kotlin.math.abs
import kotlin.math.sign

enum class Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT
}

data class MoveInstruction(val direction: Direction, val step: Int)
val initialPos = 0 to 0

private fun moveRope(knots: Int, instructions: List<MoveInstruction>): Int {
    val ropeState = Array(knots) { initialPos }
    val visited = mutableSetOf(initialPos)

    val instructionsFlat = instructions.flatMap { instruction ->
        if (instruction.step == 1) listOf(instruction.direction)
        else (1..instruction.step).map { instruction.direction }
    }

    instructionsFlat.forEach { direction ->
        ropeState[0] = ropeState[0].moveTowards(direction)
        ropeState.indices.windowed(2, 1).forEach { (head, tail) ->
            if (abs(ropeState[head].first - ropeState[tail].first) > 1 || abs(ropeState[head].second - ropeState[tail].second) > 1) {
                ropeState[tail] = ropeState[tail].moveTowards(ropeState[head])
            }
        }
        visited += ropeState.last()
    }
    return visited.size
}

fun Pair<Int, Int>.moveTowards(other: Pair<Int, Int>): Pair<Int, Int> {
    val (x, y) = this
    val (otherX, otherY) = other
    return ((otherX - x).sign + x to (otherY - y).sign + y)
}
fun Pair<Int, Int>.moveTowards(direction: Direction): Pair<Int, Int> {
    val (x, y) = this
    return when (direction) {
        Direction.UP -> x to y - 1
        Direction.RIGHT -> x + 1 to y
        Direction.DOWN -> x to y + 1
        Direction.LEFT -> x  - 1 to y
    }
}

class Day9Test {

    @Test
    fun day9() {
        File("src/2022/Day9.txt").readLines().let { input ->
            println(moveRope(2, parseInstructions(input)))
            println(moveRope(10, parseInstructions(input)))
        }
    }

    @Test
    fun day9Test() {
        """
            R 4
            U 4
            L 3
            D 1
            R 4
            D 1
            L 5
            R 2
        """.trimIndent().split("\n").let { input ->
            println(moveRope(2, parseInstructions(input)))
        }
    }

}

private fun parseInstructions(lines: List<String>) = lines.map {
    val (direction, step) = it.split(" ")
    MoveInstruction(
        direction = when (direction) {
            "U" -> Direction.UP
            "D" -> Direction.DOWN
            "L" -> Direction.LEFT
            "R" -> Direction.RIGHT
            else -> error("unrecognized direction=$direction for input=$it")
        },
        step = step.toInt(),
    )
}
