fun Char.getCharValue(): Int {
  val charList = ('a'..'z').toMutableList() + ('A'..'Z').toMutableList()
  return charList.indexOf(this) + 1
}

fun main() {
  fun part1(input: List<String>): Int =
    input
      .map { it.chunked(it.length / 2) }
      .sumOf { (compartment1, compartment2) -> compartment1.toList().first { it in compartment2 }.getCharValue() }

  fun part2(input: List<String>): Int =
    input
      .chunked(3)
      .sumOf { group -> group.first().first { check -> group.all { check in it } }.getCharValue() }

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
