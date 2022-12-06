package `2022`

import org.junit.jupiter.api.Test
import java.io.File

fun startOfPacketMarker(input: String, distinctChars: Int = 4): Int {
    val match = input.windowed(size = distinctChars, step = 1)
        .first { characters ->
            characters.toCharArray().distinct().size == distinctChars
        }
    return input.indexOf(match) + distinctChars
}


class Day6Test {

    @Test
    fun day6() {
        File("src/2022/Day6.txt").readText().let { input ->
            startOfPacketMarker(input).let(::println)
            startOfPacketMarker(input, 14).let(::println)
        }
    }

    @Test
    fun day6Test() {
        println(startOfPacketMarker("bvwbjplbgvbhsrlpgdmjqwftvncz"))
        println(startOfPacketMarker("nppdvjthqldpwncqszvftbrmjlhg"))
        println(startOfPacketMarker("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg"))
        println(startOfPacketMarker("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw"))
    }

}
