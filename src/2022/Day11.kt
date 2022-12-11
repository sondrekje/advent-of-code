package `2022`

import org.junit.jupiter.api.Test
import java.io.File

class Day11Test {

    @Test
    fun day11() {
        File("src/2022/Day11.txt").readLines().splitAt(String::isEmpty).let { input ->
            val parsed = parseGameInitialization(input)
            val round20 = evaluteRounds(20, parsed)
            check(round20.part1() == 120_056L)

            val modBy = parsed.map { it.test.divisibleBy }.reduce { acc, cur -> acc * cur }
            val part2 = evaluteRounds(10_000, parsed, handleWorryLevel = { x -> x % modBy })
            println(part2.part1())
        }
    }

    @Test
    fun day11Test() {
        """
            Monkey 0:
              Starting items: 79, 98
              Operation: new = old * 19
              Test: divisible by 23
                If true: throw to monkey 2
                If false: throw to monkey 3

            Monkey 1:
              Starting items: 54, 65, 75, 74
              Operation: new = old + 6
              Test: divisible by 19
                If true: throw to monkey 2
                If false: throw to monkey 0

            Monkey 2:
              Starting items: 79, 60, 97
              Operation: new = old * old
              Test: divisible by 13
                If true: throw to monkey 1
                If false: throw to monkey 3

            Monkey 3:
              Starting items: 74
              Operation: new = old + 3
              Test: divisible by 17
                If true: throw to monkey 0
                If false: throw to monkey 1
        """.trimIndent().split("\n").splitAt(String::isEmpty).let { input ->
            val parsed = parseGameInitialization(input)
            println(input)
            val round1 = evaluteRounds(1, parsed)
            check(round1.monkeyToItems[0] == listOf(20, 23, 27, 26).map(Int::toLong))
            check(round1.monkeyToItems[1] == listOf(2080, 25, 167, 207, 401, 1046).map(Int::toLong))
            val round2 = evaluteRounds(2, parsed)
            check(round2.monkeyToItems[0] == listOf(695, 10, 71, 135, 350).map(Int::toLong))
            check(round2.monkeyToItems[1] == listOf(43, 49, 58, 55, 362).map(Int::toLong))
            val round20 = evaluteRounds(20, parsed)
            check(round20.part1() == 10605L)
            val round15 = evaluteRounds(15, parsed)
            check(round15.monkeyToItems[0] == listOf(83, 44, 8, 184, 9, 20, 26, 102).map(Int::toLong))
            check(round15.monkeyToItems[1] == listOf(110, 36).map(Int::toLong))

            val modBy = parsed.map { it.test.divisibleBy }.reduce { acc, cur -> acc * cur }
            val part2 = evaluteRounds(10_000, parsed, handleWorryLevel = { it % modBy })
            check(part2.part1() == 2_713_310_158L)
        }
    }
}

fun PendingGameState.part1() = monkeyInspectCount.entries
    .sortedByDescending(Map.Entry<Long, Long>::value)
    .take(2)
    .fold(1L) { acc, multiply -> acc * multiply.value }

private fun evaluteRounds(
    rounds: Long,
    setupForMonkey: List<SetupForMonkey> = emptyList(),
    handleWorryLevel: (current: Long) -> Long = { it / 3 },
    initialState: PendingGameState = PendingGameState(
        monkeyToItems = setupForMonkey.associate { it.id to it.startingItems },
        monkeyInspectCount = setupForMonkey.associate { it.id to 0 },
    ),
) = (0 until rounds).fold(initialState) { state, _ -> evaluateRound(setupForMonkey, handleWorryLevel, state) }

private fun evaluateRound(
    setupForMonkey: List<SetupForMonkey>,
    handleWorryLevel: (current: Long) -> Long = { it / 3 },
    initialState: PendingGameState,
) = initialState.monkeyToItems.keys.fold(initialState) { statePerMonkey, monkey ->
    statePerMonkey.monkeyToItems[monkey]!!.fold(statePerMonkey) { state, itemThrownWorryLevel ->
        val setup = setupForMonkey.single { it.id == monkey }

        val newWorryLevel = handleWorryLevel(setup.operation(itemThrownWorryLevel))
        val newOwnerId = if ((newWorryLevel % setup.test.divisibleBy) == 0L)
            setup.test.throwIdIfTrue else setup.test.throwIdIfFalse

        state.copy(
            monkeyToItems = state.monkeyToItems + mapOf(
                monkey to (state.monkeyToItems[monkey]!!).drop(1),
                newOwnerId to (state.monkeyToItems[newOwnerId]!! + listOf(newWorryLevel))
            ),
            monkeyInspectCount = state.monkeyInspectCount + mapOf(
                monkey to (state.monkeyInspectCount[monkey]!! + 1)
            ),
        )
    }
}

data class PendingGameState(
    val monkeyToItems: Map<Long, List<Long>>,
    val monkeyInspectCount: Map<Long, Long> = emptyMap(),
)

private fun parseGameInitialization(monkeyInitializers: List<List<String>>) = monkeyInitializers.map { initializer ->
    check(initializer.size == 6) { "expect monkey initialization block but got=$initializer" }
    SetupForMonkey(
        id = initializer.first().filter(Char::isDigit).toLong(),
        startingItems = initializer.drop(1).first().filter { it.isDigit() || it.isWhitespace() }.split(" ")
            .filter(String::isNotEmpty).map(String::toLong),
        operation = initializer.drop(2).first().let { operationLine ->
            if ("*" in operationLine) {
                val (_, right) = operationLine.substringAfter("= ").split("*").map(String::trim)
                val optMultiplyWith = if (right.all(Char::isDigit)) right.toLong() else null
                { x -> x * (optMultiplyWith ?: x) }
            } else if ("+" in operationLine) {
                val (_, right) = operationLine.substringAfter("= ").split("+").map(String::trim)
                val optAdd = if (right.all(Char::isDigit)) right.toLong() else null
                { x -> x + (optAdd ?: x) }
            } else error("Couldn't map operationLine=$operationLine for initializer=$initializer")
        },
        test = MonkeyTest(
            divisibleBy = initializer.drop(3).first().filter(Char::isDigit).toLong(),
            throwIdIfTrue = initializer.drop(4).first().filter(Char::isDigit).toLong(),
            throwIdIfFalse = initializer.drop(5).first().filter(Char::isDigit).toLong(),
        )
    )
}

data class MonkeyTest(
    val divisibleBy: Long,
    val throwIdIfTrue: Long,
    val throwIdIfFalse: Long,
)

data class SetupForMonkey(
    val id: Long,
    val startingItems: List<Long>,
    val operation: (old: Long) -> Long,
    val test: MonkeyTest,
)
