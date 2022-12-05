fun getStackNumbers(input: List<String>): List<List<Char>> =
  input
    .asSequence()
    .filter { it.contains('[') }
    .map { line -> line.chunked(4).mapIndexed { index, char -> index to char[1] } }
    .map { it.filter { charPair -> charPair.second.isLetter() } }.flatten()
    .groupBy { it.first }
    .map { it.key to it.value.map { pair -> pair.second } }
    .sortedBy { it.first }
    .map { it.second.reversed() }

val moveRegex = """move (\d+) from (\d+) to (\d+)""".toRegex()
fun getMoves(input: List<String>): List<Move> =
  input.filter { it.contains("move") }.map { moveRegex.find(it) }.map {
    val (amount, from, into) = it!!.destructured
    Move(amount.toInt(), into.toInt() - 1, from.toInt() - 1)
  }

data class Move(val amount: Int, val into: Int, val from: Int)

fun main() {
  fun part1(input: List<String>): String =
    getMoves(input)
      .fold(getStackNumbers(input).toMutableList()) { acc, move ->
        acc[move.into] = acc[move.into].plus(acc[move.from].takeLast(move.amount).reversed())
        acc[move.from] = acc[move.from].dropLast(move.amount)
        acc
      }
      .map { it.last() }.joinToString("")

  fun part2(input: List<String>): String =
    getMoves(input)
      .fold(getStackNumbers(input).toMutableList()) { acc, move ->
        acc[move.into] = acc[move.into].plus(acc[move.from].takeLast(move.amount))
        acc[move.from] = acc[move.from].dropLast(move.amount)
        acc
      }
      .map { it.last() }.joinToString("")

  // test if implementation meets criteria from the description, like:
  val testInput = readInput("Day05_test")
  println(part1(testInput))
  check(part1(testInput) == "CMZ")
  println(part2(testInput))
  check(part2(testInput) == "MCD")

  val input = readInput("Day05")
  println(part1(input))
  println(part2(input))
}
