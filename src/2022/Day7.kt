package `2022`

import org.junit.jupiter.api.Test

class Day7Test {

    @Test
    fun day7() {
        java.io.File("src/2022/Day7.txt").readLines().let(::parseTerminalOutputToFileSystem).let { fileSystem ->
            fileSystem.let(::part1).let(::println)
            fileSystem.let(::part2).let(::println)
        }
    }

    @Test
    fun day7Test() {
        """
            ${'$'} cd /
            ${'$'} ls
            dir a
            14848514 b.txt
            8504156 c.dat
            dir d
            ${'$'} cd a
            ${'$'} ls
            dir e
            29116 f
            2557 g
            62596 h.lst
            ${'$'} cd e
            ${'$'} ls
            584 i
            ${'$'} cd ..
            ${'$'} cd ..
            ${'$'} cd d
            ${'$'} ls
            4060174 j
            8033020 d.log
            5626152 d.ext
            7214296 k
        """.trimIndent().split("\n").let { input ->
            val fileSystem = parseTerminalOutputToFileSystem(input)
            check(part1(fileSystem) == 95437L)
            check(part2(fileSystem) == 24933642L)
        }
    }
}

fun part1(fileSystem: Directory) = fileSystem.allDirectories.filter { it.size < 100000 }.sumOf(Directory::size)

fun part2(fileSystem: Directory): Long {
    val totalDiskSpaceAvailable = 70_000_000
    val diskSpaceLeft = totalDiskSpaceAvailable - fileSystem.size
    val diskSpaceNeeded = 30_000_000 - diskSpaceLeft

    val biggerDirectories = fileSystem.allDirectories
        .filter { it.size > diskSpaceNeeded }
        .minByOrNull { it.size }!!

    return biggerDirectories.size
}

fun parseTerminalOutputToFileSystem(lines: List<String>): Directory {
    val dirStack = ArrayDeque<Directory>()
    lines.forEach { line ->
        when {
            line.startsWith("$") && "cd" !in line -> {}
            line.startsWith("$") && "cd" in line && ".." in line -> dirStack.removeLast()
            line.startsWith("$") && "cd" in line -> dirStack.addLast(
                dirStack.lastOrNull()?.traverse(line.substringAfter("cd ")) ?: Directory(line.substringAfter("cd "))
            )

            else -> {
                val (sizeOrType, name) = line.split(" ")
                if (sizeOrType == "dir") return@forEach
                val file = File(name, sizeOrType.toLong())
                dirStack.last() += file
            }
        }
    }
    return dirStack.first()
}

sealed interface FileSystemEntry {
    val name: String
}

data class Directory(
    override val name: String,
    var entries: List<FileSystemEntry> = emptyList(),
) : FileSystemEntry {
    fun traverse(path: String): Directory = entries.firstOrNull { it.name == path } as Directory? ?: run {
        val new = Directory(path)
        entries += new
        new
    }
    val size: Long get() = allFiles.sumOf(File::size)

    val allDirectories: List<Directory> get() = listOf(this) + getAllEntriesRecursive(entries).filterIsInstance<Directory>()
    val allFiles: List<File> get() = getAllEntriesRecursive(entries).filterIsInstance<File>()
}

operator fun Directory.plusAssign(file: File) {
    entries += file
}

private fun getAllEntriesRecursive(entries: List<FileSystemEntry>): List<FileSystemEntry> = entries
    .flatMap { entry ->
        if (entry is Directory) listOf(entry) + getAllEntriesRecursive(entry.entries)
        else listOf(entry)
    }

data class File(override val name: String, val size: Long) : FileSystemEntry
