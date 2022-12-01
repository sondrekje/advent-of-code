package `2022`

import java.io.File
import org.junit.jupiter.api.Test

fun elvesToCarolies(input: String) = input
    .split("\n")
    .split("")
    .mapIndexed { index , listOfCarolies ->
        val elfNumber = index + 1
        elfNumber to listOfCarolies.map(String::toInt).sum()
    }.toMap()

fun day1_part1(input: String): Map.Entry<Int, Int> =
    elvesToCarolies(input).maxByOrNull { (_, sumCarolies) -> sumCarolies }!!
fun day1_part2(input: String): Int =
    elvesToCarolies(input).entries
        .sortedByDescending { (_, carolies) -> carolies }
        .take(3)
        .sumOf { (_, carolies) -> carolies }

class Day1 {

    @Test
    fun day1() {
        File("src/2022/Day1.txt").readText().let { input ->
            input.let(::day1_part1).run(::println)
            input.let(::day1_part2).run(::println)
        }
    }

    @Test
    fun day1Test() {
        val input = """
            1000
            2000
            3000

            4000

            5000
            6000

            7000
            8000
            9000

            10000
        """.trimIndent()
        val (elf, amount) = day1_part1(input)
        val part2 = day1_part2(input)

        check(elf == 4 && amount == 24000)
        check(part2 == 45000)
    }
}

fun <T> List<T>.split(delimiter: T) = fold(emptyList<List<T>>()) { lists, currentItem ->
    val currentOrNewList = lists.lastOrNull() ?: emptyList()

    if (currentItem == delimiter) lists + listOf(emptyList())
    else lists.dropLast(1) + listOf(currentOrNewList + currentItem)
}

