fun Char.getCharValue(): Int {
  val charList = ('a'..'z').toMutableList() + ('A'..'Z').toMutableList()
  return charList.indexOf(this) + 1
}

fun main() {
  fun part1(input: List<String>): Int =
    input.map { line -> line.substring(0, line.length / 2) to line.substring(line.length / 2) }
      .map { (compartment1, compartment2) -> compartment1.toList().first { it in compartment2 } }
      .sumOf { it.getCharValue() }

  fun part2(input: List<String>): Int =
    input.chunked(3).map { threeLines -> threeLines[0].toList().first { it in threeLines[1] && it in threeLines[2] } }
      .sumOf { it.getCharValue() }

  // test if implementation meets criteria from the description, like:
  val testInput = readInput("Day03_test")
  println(part1(testInput))
  check(part1(testInput) == 157)
  println(part2(testInput))
  check(part2(testInput) == 70)

  val input = readInput("Day03")
  println(part1(input))
  println(part2(input))
}
