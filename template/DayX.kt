fun main() {
  fun part1(input: List<String>): Int =
    input.size

  fun part2(input: List<String>): Int =
    input.size

  // test if implementation meets criteria from the description, like:
  val testInput = readInput("DayX_test")
  println(part1(testInput))
  check(part1(testInput) == 7)
  println(part2(testInput))
  check(part2(testInput) == 5)

  val input = readInput("DayX")
  println(part1(input))
  println(part2(input))
}
