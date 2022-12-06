package `2021`

import org.junit.jupiter.api.Test
import java.io.File

fun traverseRecursive(
    connectionMap: Map<String, List<String>>,
    predicate: (paths: List<String>) -> (path: String) -> Boolean,
    path: List<String> = listOf("start"),
): List<List<String>> = connectionMap[path.last()]!!
    .filter { to -> to.any { it.isUpperCase() } || to !in path }
    .flatMap {
        if (it != "end") traverseRecursive(connectionMap, predicate, path + it) else listOf(path + it)
    }

class Day12Test {

    @Test
    fun day12() {
        File("src/2021/Day12.txt").readLines().let { input ->
            println(
                traverseRecursive(
                    toConnectionMap(input),
                    predicate = { paths ->
                        { to ->
                            to .any { it.isUpperCase() } || to !in paths
                        }
                    }
                ).size
            )
        }
    }

    @Test
    fun testDay12() {
        val input = """
            start-A
            start-b
            A-c
            A-b
            b-d
            A-end
            b-end
        """.trimIndent().split("\n")

        //val result = traverseRecursive(toConnectionMap(input))
        //println(result)
    }

}

fun toConnectionMap(connections: List<String>): Map<String, List<String>> =
    connections
        .map { it.split("-") }
        .flatMap {
            listOf(
                it.first() to it.last(),
                it.last() to it.first(),
            )
        }.groupBy(
            keySelector = { (from) -> from },
            valueTransform = { (_, to) -> to }
        )
