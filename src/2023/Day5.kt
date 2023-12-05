package `2023`

import org.junit.jupiter.api.Test
import java.io.File

data class RangeMapping(
    val destinationStart: Long,
    val sourceStart: Long,
    val rangeLength: Long,
)

fun RangeMapping.mapSeed(seed: Long) =
    if (seed in sourceStart until sourceStart + rangeLength)
        destinationStart + (seed - sourceStart)
    else null

data class Almanac(
    val seeds: List<Long>,
    val mappings: Map<String, List<RangeMapping>>,
) {
    companion object {
        fun parse(input: String): Almanac {
            val sections = input.trim().split("\n\n").associate {
                val (title, content) = it.split(":")
                title.trim() to content.trim()
            }

            val seeds = sections["seeds"]?.split(" ")?.map(String::toLong) ?: emptyList()

            val mappings = sections
                .filterKeys { it != "seeds" }
                .mapKeys { (key, _) -> key.substringBefore(" ") }
                .mapValues { (_, value) ->
                    value.lines().map { line ->
                        val (destinationStart, sourceStart, length) = line.split(" ").map(String::toLong)
                        RangeMapping(destinationStart, sourceStart, length)
                    }
                }

            return Almanac(seeds, mappings)
        }
    }
}

data class SeedRange(
    val start: Long,
    val length: Long,
)

data class Almanac2(
    val seedRanges: List<SeedRange>,
    val mappings: Map<String, List<RangeMapping>>,
) {
    companion object {
        fun parse(input: String): Almanac2 {
            val sections = input.trim().split("\n\n").associate {
                val (title, content) = it.split(":")
                title.trim() to content.trim()
            }

            val seedStrings = sections["seeds"]?.split(" ") ?: emptyList()
            val seeds = seedStrings.chunked(2).map { (start, length) ->
                SeedRange(start.toLong(), length.toLong())
            }

            val mappings = sections
                .filterKeys { it != "seeds" }
                .mapKeys { (key, _) -> key.substringBefore(" ") }
                .mapValues { (_, value) ->
                    value.lines().map { line ->
                        val (destinationStart, sourceStart, length) = line.split(" ").map(String::toLong)
                        RangeMapping(destinationStart, sourceStart, length)
                    }
                }

            return Almanac2(seeds, mappings)
        }
    }
}

class Day5 {

    @Test
    fun day5() {
        val parsed = Almanac.parse(File("./src/2023/Day5.txt").readText())

        val part1 = parsed.seeds.minOfOrNull { seed ->
            parsed.mappings.values.fold(seed) { acc, mapper ->
                mapper.firstNotNullOfOrNull { it.mapSeed(acc) } ?: acc
            }
        }
        check(part1 == 662197086L)
        println(part1)
        val parsed2 = Almanac2.parse(File("./src/2023/Day5.txt").readText())
        val part2 = parsed2.seedRanges.asSequence().flatMap { seedRange ->
            seedRange.start until (seedRange.start + seedRange.length)
        }.map { seed ->
            parsed2.mappings.values.fold(seed) { acc, mapper ->
                mapper.firstNotNullOfOrNull { it.mapSeed(acc) } ?: acc
            }
        }.minOrNull()
        check(part2 == 52510809L)
        println(part2)
    }

    @Test
    fun test() {
        val input = """
            seeds: 79 14 55 13

            seed-to-soil map:
            50 98 2
            52 50 48

            soil-to-fertilizer map:
            0 15 37
            37 52 2
            39 0 15

            fertilizer-to-water map:
            49 53 8
            0 11 42
            42 0 7
            57 7 4

            water-to-light map:
            88 18 7
            18 25 70

            light-to-temperature map:
            45 77 23
            81 45 19
            68 64 13

            temperature-to-humidity map:
            0 69 1
            1 0 69

            humidity-to-location map:
            60 56 37
            56 93 4
        """.trimIndent()

        val parsed = Almanac.parse(input)

        val seedsToSoils = parsed.seeds.associateWith { seed ->
            parsed.mappings["seed-to-soil"]!!.firstNotNullOfOrNull { it.mapSeed(seed) } ?: seed
        }
        println(seedsToSoils)
        val mapped = parsed.seeds.map { seed ->
            parsed.mappings.values.fold(seed) { acc, mapper ->
                mapper.firstNotNullOfOrNull { it.mapSeed(acc) } ?: acc
            }
        }
        check(mapped.min() == 35L)

        println(mapped)
    }
}