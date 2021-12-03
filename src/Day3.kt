fun calculateGammaRate(binaryNums: List<String>): String =
    (0 until binaryNums.first().length).joinToString(separator = "") { index ->
        binaryNums.map { it[index].toString() }.groupingBy { it }.eachCount().maxByOrNull { it.value }!!.key
    }

fun calculateEpsilonRate(binaryNums: List<String>): String =
    (0 until binaryNums.first().length).joinToString(separator = "") { index ->
        binaryNums.map { it[index].toString() }.groupingBy { it }.eachCount().minByOrNull { it.value }!!.key
    }

fun findRating(binaryNums: List<String>, cmpCharacter: FindCharacterFn): String =
    (0 until binaryNums.first().length).fold(binaryNums) { acc, column ->
        val numForIndex = acc.map { it[column].toString() }
        val (ones, zeros) = numForIndex.partition { it == "1" }.let { (ones, zeros) -> ones.size to zeros.size }

        if (acc.size == 1) return@fold acc
        acc.filter { it[column] == cmpCharacter(ones, zeros) }
    }.single()
typealias FindCharacterFn = (ones: Int, zeroes: Int) -> Char

val findOxygenRatingCmpFn: FindCharacterFn = { ones, zeros -> if (zeros > ones) '0' else '1' }
val findCo2RatingCmpFn: FindCharacterFn = { ones, zeros -> if (zeros > ones) '1' else '0' }

fun calculatePowerConsumption(binaryNums: List<String>) =
    calculateEpsilonRate(binaryNums).toInt(2) * calculateGammaRate(binaryNums).toInt(2)
fun calculateLifeSupport(binaryNums: List<String>) =
    findRating(binaryNums, findOxygenRatingCmpFn).toInt(2) * findRating(binaryNums, findCo2RatingCmpFn).toInt(2)

fun main() {
    val part1 = calculatePowerConsumption(readInput("Day3")).also { println(it) }
    val part2 = calculateLifeSupport(readInput("Day3")).also { println(it) }
}