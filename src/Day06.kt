fun String.getIndexOfNonrepeat(chunk: Int): Int = this.windowed(chunk).indexOfFirst { it.toSet().count() == it.count() }

fun main() {
  fun part1(input: List<String>): Int =
    input.first().getIndexOfNonrepeat(4) + 4

  fun part2(input: List<String>): Int =
    input.first().getIndexOfNonrepeat(14) + 14

// test if implementation meets criteria from the description, like:
  val testInput = readInput("Day06_test")
  val output1 = part1(testInput)
  println(output1)
  check(output1 == 7)
  val output2 = part2(testInput)
  println(output2)
  check(output2 == 19)

  val input = readInput("Day06")
  println(part1(input))
  println(part2(input))
}
