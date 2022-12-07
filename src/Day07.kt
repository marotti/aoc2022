data class Directory(
  val name: String,
  val parentDirectory: Directory?,
  var subDirectories: List<Directory>,
  var files: List<Pair<Int, String>>,
) {
  fun size(): Int =
    files.sumOf { it.first } + subDirectories.sumOf { it.size() }
}

fun String.isCommand() = this.contains('$')
fun String.isChangeDirectoryCommand() = isCommand() && this.contains("cd ")
fun String.isListCommand() = isCommand() && this.contains("ls")
fun String.getChangedDirectoryName() = this.split(" ")[2]
fun String.isChangeDirectoryToRoot() = this == "$ cd /"
fun String.isChangeToParentDirectory() = this == "$ cd .."
fun String.getFileInformation() = Pair(this.split(" ")[0].toInt(), this.split(" ")[1])
fun String.isDirectory() = !isCommand() && this.contains("dir")
fun String.getDirectoryName() = this.split(" ")[1]


fun buildDirectoryStructure(input: List<String>): Directory {
  val rootDirectory = Directory("/", null, listOf(), listOf())
  var directory = rootDirectory
  for (line in input) {
    if (line.isChangeDirectoryToRoot()) {
      directory = rootDirectory
    } else if (line.isChangeToParentDirectory()) {
      directory = directory.parentDirectory ?: rootDirectory
    } else if (line.isChangeDirectoryCommand()) {
      directory = directory.subDirectories.find { it.name == line.getChangedDirectoryName() }!!
    } else if (line.isListCommand()) {
      // ignore
    } else if (line.isDirectory()) {
      directory.subDirectories =
        directory.subDirectories.plus(Directory(line.getDirectoryName(), directory, listOf(), listOf()))
    } else {
      directory.files = directory.files.plus(line.getFileInformation())
    }
  }
  return rootDirectory
}

fun countDirectoriesUnder100k(directory: Directory): Int {
  var total = 0
  if (directory.size() < 100000) total += directory.size()
  return total + directory.subDirectories.map { countDirectoriesUnder100k(it) }.sum()
}

fun getAllDirectoriesWithEnoughToFreeSpace(directory: Directory, neededSpace: Int): List<Int> {
  var total = 0
  if (directory.size() > neededSpace) total += directory.size()
  return listOf(total).plus(directory.subDirectories.map { getAllDirectoriesWithEnoughToFreeSpace(it, neededSpace) }
    .flatten().filter { it != 0 })
}

fun main() {
  fun part1(input: List<String>): Int {
    val rootDirectory = buildDirectoryStructure(input)
    return countDirectoriesUnder100k(rootDirectory)
  }


  fun part2(input: List<String>): Int {
    val rootDirectory = buildDirectoryStructure(input)
    val freespace = 70000000 - rootDirectory.size()
    val neededSpace = 30000000 - freespace
    return getAllDirectoriesWithEnoughToFreeSpace(rootDirectory, neededSpace).minOf { it }
  }

  // test if implementation meets criteria from the description, like:
  val testInput = readInput("Day07_test")
  val output1 = part1(testInput)
  println(output1)
  check(output1 == 95437)
  val output2 = part2(testInput)
  println(output2)
  check(output2 == 24933642)

  val input = readInput("Day07")
  println(part1(input))
  println(part2(input))
}
