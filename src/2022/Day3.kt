package `2022`

import org.junit.jupiter.api.Test
import java.io.File

fun priority(char: Char) = (if (char.isUpperCase()) (char - 'A' + 26) else char - 'a') + 1

fun findItemsInCommon(rucksack: String) = rucksack.splitAtMiddle().let { (part1, part2) ->
    part1.toCharArray().intersect(part2.toCharArray().toSet())
}

fun day3_part1(entireRucksack: List<String>) = entireRucksack
    .flatMap(::findItemsInCommon)
    .fold(0) { total, current -> total + priority(current) }

fun day3_part2(entireRucksack: List<String>) = entireRucksack
    .windowed(
        size = 3,
        step = 3,
        partialWindows = false,
    ) { elfGroup ->
        val (first, middle, last) = elfGroup.map(String::toCharArray)
        first.intersect(middle.toSet()).intersect(last.toSet()).map(::priority)
    }.flatten().sum()

class Day3 {

    @Test
    fun day3() {
        File("src/2022/Day3.txt").readLines().let { input ->
            println(day3_part1(input))
            println(day3_part2(input))
        }
    }

    @Test
    fun day3_test() {
        """
            vJrwpWtwJgWrhcsFMMfFFhFp
            jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL
            PmmdzqPrVvPwwTWBwg
            wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn
            ttgJtRGJQctTZtZT
            CrZsJsPPZsGzwwsLwLmpwMDw
        """.trimIndent().split("\n").run {
            println(day3_part1(this))
            println(day3_part2(this))
        }
    }
}

fun String.splitAtMiddle(): Pair<String, String> {
    val middle = length / 2
    return take(middle) to substring(middle)
}
