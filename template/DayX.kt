fun main() {
  fun part1(input: List<String>): Int =
    input.size

  fun part2(input: List<String>): Int =
    input.size

  // test if implementation meets criteria from the description, like:
  val testInput = readInput("DayX_test")
  val output1 = part1(testInput)
  println(output1)
  check(output1 == 7)
  val output2 = part2(testInput)
  println(output2)
  check(output2 == 5)

  val input = readInput("DayX")
  println(part1(input))
  println(part2(input))
}
