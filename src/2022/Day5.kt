package `2022`

import org.junit.jupiter.api.Test
import java.io.File

class SupplyStackGroup(private val columnsOfBoxes: List<List<Char>>) {

    override fun toString() = columnsOfBoxes.joinToString(separator = "") { stack -> "${stack.last()}" }

    fun move(
        instruction: Instruction,
        stackRetainOrder: Boolean = false,
    ): SupplyStackGroup {
        val boxesToMove = columnsOfBoxes[instruction.from - 1].takeLast(instruction.amount)
        return SupplyStackGroup(
            columnsOfBoxes = columnsOfBoxes.mapIndexed { index, boxes ->
                when (index + 1) {
                    instruction.from -> boxes.dropLast(instruction.amount)
                    instruction.to -> boxes + if (!stackRetainOrder) boxesToMove.reversed() else boxesToMove
                    else -> boxes
                }
            }
        )
    }

    companion object {
        fun fromSetup(setup: List<String>): SupplyStackGroup {
            val rowsToBuild = setup.last().filter(Char::isDigit)
            val stacks = setup.dropLast(1)

            val rowsToColumnsBottomToTop = rowsToBuild.map { row ->
                val stringIndex = setup.last().indexOf(row)
                stacks.map { it[stringIndex] }.filter(Char::isLetter).reversed()
            }
            return SupplyStackGroup(rowsToColumnsBottomToTop)
        }
    }
}

fun day5_part1(input: List<String>) = input.splitAt(String::isEmpty).let { (setup, moves) ->
    parseInstructions(moves)
        .fold(SupplyStackGroup.fromSetup(setup)) { stacks, instructions ->
            stacks.move(instructions)
        }
}
fun day5_part2(input: List<String>) = input.splitAt(String::isEmpty).let { (setup, moves) ->
    parseInstructions(moves)
        .fold(SupplyStackGroup.fromSetup(setup)) { stacks, instructions ->
            stacks.move(instructions, stackRetainOrder = true)
        }
}

class Day5 {

    @Test
    fun day5() {
        File("src/2022/Day5.txt").readLines().let { input ->
            day5_part1(input).let(::println)
            day5_part2(input).let(::println)
        }
    }

    @Test
    fun testDay5() {
        """
                |    [D]    
                |[N] [C]    
                |[Z] [M] [P]
                | 1   2   3 
                |
                |move 1 from 2 to 1
                |move 3 from 1 to 3
                |move 2 from 2 to 1
                |move 1 from 1 to 2
        """.trimMargin().split("\n").splitAt(String::isEmpty).run {
            val (setup, moves) = this

            val initStacks = SupplyStackGroup.fromSetup(setup)
            val instructions = parseInstructions(moves)

            val part1 = instructions.fold(initStacks) { stacks, instruction ->
                stacks.move(instruction)
            }.toString()
            val part2 = instructions.fold(initStacks) { stacks, instruction ->
                stacks.move(instruction, stackRetainOrder = true)
            }.toString()
            check(part1 == "CMZ")
            check(part2 == "MCD")
        }
    }
}

data class Instruction(val amount: Int, val from: Int, val to: Int)

private fun parseInstructions(instructions: List<String>): List<Instruction> = instructions.map { line ->
    val (amount, from, to) = line.split(" ").filter { string -> string.toCharArray().all(Char::isDigit) }
        .map(String::toInt)

    Instruction(amount, from, to)
}

fun <T> List<T>.splitAt(predicate: (T) -> Boolean) = fold(emptyList<List<T>>()) { lists, currentItem ->
    val currentOrNewList = lists.lastOrNull() ?: emptyList()

    if (predicate(currentItem)) lists + listOf(emptyList())
    else lists.dropLast(1) + listOf(currentOrNewList + currentItem)
}
