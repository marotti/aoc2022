fun getListOfCalories(input: List<String>): List<Int> =
  input.fold(mutableListOf(0)) { acc, s ->
    if (s.isEmpty()) {
      acc.plus(0)
      acc
    } else {
      acc[acc.lastIndex] += s.toInt()
      acc
    }
  }.toList()

fun main() {
  fun part1(input: List<String>): Int =
    getListOfCalories(input).maxOrNull()!!


  fun part2(input: List<String>): Int =
    getListOfCalories(input).sortedDescending().slice(0..2).sum()

  // test if implementation meets criteria from the description, like:
  val testInput = readInput("Day01_test")
  println(part1(testInput))
  check(part1(testInput) == 24000)
  println(part2(testInput))
  check(part2(testInput) == 45000)

  val input = readInput("Day01")
  println(part1(input))
  println(part2(input))
}
