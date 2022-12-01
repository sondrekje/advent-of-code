package `2021`

import java.io.File

data class Lanternfish(val timer: Int) {
    fun tick(): List<Lanternfish> = when (timer) {
        0 -> listOf(Lanternfish(6), Lanternfish(8))
        else -> listOf(copy(timer = timer - 1))
    }
}

fun simulateForDays(initalFish: List<Lanternfish>, days: Int): Map<Int, List<Lanternfish>> {
    val firstDay = mapOf(0 to initalFish)
    val restDays = mutableMapOf<Int, List<Lanternfish>>()
    (1..days).forEach { day ->
        restDays[day] = restDays.getOrDefault(day - 1, initalFish).flatMap { it.tick() }
    }
    return firstDay + restDays
}

fun main() {
    val input = File("src", "2021/Day6.txt6.txt").readText().split(",").map { it.toInt() }
    val initialFish = input.map { Lanternfish(it) }

    val part1 = simulateForDays(initialFish, 80).also { println(it[80]!!.size) }

    // part 2
    val fish = LongArray(9).apply { input.forEach { this[it]++ } }
    repeat(256) {
        val zero = fish[0]
        (1..8).forEach { index -> fish[index - 1] = fish[index] }

        fish[6] += zero
        fish[8] = zero
    }
    println(fish.sum())
}