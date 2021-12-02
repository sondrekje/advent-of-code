// part 1
fun parseInstruction(instruction: String): Pair<Int, Int> {
    val amount = instruction.substringAfter(" ").toInt()
    return when {
        instruction.startsWith("forward") -> 0 to amount
        instruction.startsWith("down") -> amount to 0
        instruction.startsWith("up") -> -amount to 0
        else -> error("Unrecognized instruction $instruction")
    }
}
fun calculatePosition(instructions: List<String>) = instructions.fold(0 to 0) { acc, cur ->
    val parsed = parseInstruction(cur)
    (acc.first + parsed.first) to (acc.second + parsed.second)
}.let { (depth, horizontal) -> depth * horizontal }
// part 2
data class SubmarinePos(val aim: Int = 0, val horizonal: Int = 0, val depth: Int = 0)

fun parseInstruction2(currentAim: Int, instruction: String): SubmarinePos {
    val amount = instruction.substringAfter(" ").toInt()
    return when {
        instruction.startsWith("forward") -> SubmarinePos(0, amount, amount * currentAim)
        instruction.startsWith("down") -> SubmarinePos(amount)
        instruction.startsWith("up") -> SubmarinePos(-amount)
        else -> error("Unrecognized instruction $instruction")
    }
}

operator fun SubmarinePos.plus(other: SubmarinePos) = copy(
    aim = aim + other.aim,
    horizonal = horizonal + other.horizonal,
    depth = depth + other.depth,
)

fun calculatePosition2(instruction: List<String>) = instruction.fold(SubmarinePos()) { acc, cur ->
    acc + parseInstruction2(acc.aim, cur)
}.let { (_, horizontal, depth) -> horizontal * depth }

fun main() {
    val part1 = calculatePosition(readInput("Day02")).also { println(it) }
    val part2 = calculatePosition2(readInput("Day02")).also { println(it) }
}