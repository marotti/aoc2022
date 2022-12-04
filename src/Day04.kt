fun main() {
  fun part1(input: List<String>): Int =
    input.map { it.split(',') }.map { it[0].split('-') to it[1].split('-') }
      .map { it.first[0].toInt()..it.first[1].toInt() to it.second[0].toInt()..it.second[1].toInt() }
      .map {
        if (((it.first.first <= it.second.first) && (it.first.last >= it.second.last)) ||
          ((it.second.first <= it.first.first) && (it.second.last >= it.first.last))
        )
          1  else 0
      }.sum()


  fun part2(input: List<String>): Int =
    input.map { it.split(',') }.map { it[0].split('-') to it[1].split('-') }
      .map { it.first[0].toInt()..it.first[1].toInt() to it.second[0].toInt()..it.second[1].toInt() }
      .map { ranges ->
        if (ranges.first.any { it in ranges.second })
          1 else 0
      }.sum()

  // test if implementation meets criteria from the description, like:
  val testInput = readInput("Day04_test")
  println(part1(testInput))
  check(part1(testInput) == 2)
  println(part2(testInput))
  check(part2(testInput) == 4)

  val input = readInput("Day04")
  println(part1(input))
  println(part2(input))
}
