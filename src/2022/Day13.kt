package `2022`

import org.junit.jupiter.api.Test
import java.io.File
import java.util.*

class Day13 {

    @Test
    fun day13() {
        File("src/2022/Day13.txt").readLines().let(::parseInput).let {
            val part1 = it.part1()
            val part2 = it.part2()
            println(part1)
            println(part2)
        }
    }

    @Test
    fun testDay13() {
        """
            [1,1,3,1,1]
            [1,1,5,1,1]

            [[1],[2,3,4]]
            [[1],4]

            [9]
            [[8,7,6]]

            [[4,4],4,4]
            [[4,4],4,4,4]

            [7,7,7,7]
            [7,7,7]

            []
            [3]

            [[[]]]
            [[]]

            [1,[2,[3,[4,[5,6,7]]]],8,9]
            [1,[2,[3,[4,[5,6,0]]]],8,9]
        """.trimIndent().split("\n").let(::parseInput).let { parsed ->
            val part1 = parsed.part1()
            check(part1 == 13)
            val part2 = parsed.part2()
            check(part2 == 140)
        }
    }

}

fun List<Pair<List<Any>, List<Any>>>.part1() =
    filter { (left, right) -> left <= right }.sumOf { indexOf(it) + 1 }

fun List<Pair<List<Any>, List<Any>>>.part2(): Int {
    val add = listOf(
        "[[2]]",
        "[[6]]",
    ).map(::parsePacketData)
    val flat = flatMap { (left, right) -> listOf(left, right) }
    val sorted = (add + flat).sortedWith(Any::compareTo)

    return add.map { sorted.indexOf(it) + 1 }.reduce(Int::times)
}

private operator fun Any.compareTo(right: Any): Int {
    when {
        this is Int && right is Int -> return this - right
        this is List<*> && right is List<*> -> {
            val minLength = minOf(this.size, right.size)
            for (i in 0 until minLength) {
                val compare = this[i]!!.compareTo(right[i]!!)
                if (compare != 0) {
                    return compare
                }
            }
            return this.size - right.size
        }

        this is Int -> return listOf(this).compareTo(right)
        right is Int -> return compareTo(listOf(right))
        else -> error("left=$this(${this::class.simpleName}) right=$right(${right::class.simpleName}")
    }
}

fun parsePacketData(data: String): List<Any> {
    val result = mutableListOf<Any>()
    val stack = LinkedList<MutableList<Any>>()
    var currentNumber = ""

    val addNumber = { list: MutableList<Any> ->
        if (currentNumber.isNotEmpty()) {
            list.add(currentNumber.toInt())
            currentNumber = ""
        }
    }

    for (c in data) {
        when (c) {
            '[' -> {
                addNumber(stack.peekFirst() ?: result)
                val newList = mutableListOf<Any>()
                (stack.peekFirst() ?: result).add(newList)
                stack.addFirst(newList)
            }

            ']' -> {
                addNumber(stack.peekFirst())
                stack.removeFirst()
            }

            ',' -> {
                addNumber(stack.peekFirst())
            }

            else -> currentNumber += c
        }
    }

    addNumber(result)
    return result
}

private fun parseInput(lines: List<String>) = lines
    .filter(String::isNotEmpty)
    .chunked(2) { pair ->
        val (first, second) = pair.map(::parsePacketData)
        first to second
    }
