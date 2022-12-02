// This is garbage and I'm embarrassed. Will fix tomorrow when I'm awake.
const val ROCK=1
const val PAPER=2
const val SCISSORS=3
const val WIN=6
const val TIE=3
const val LOSE=0

fun main() {
  fun part1(input: List<String>): Int =
    input.sumOf { fight ->
      val fighters = fight.split(' ')
      when (fighters[0]) {
        "A" -> when (fighters[1]) {
          "X" -> TIE + ROCK
          "Y" -> WIN + PAPER
          "Z" -> LOSE + SCISSORS
          else -> LOSE
        }
        "B" -> when (fighters[1]) {
          "X" -> LOSE + ROCK
          "Y" -> TIE + PAPER
          "Z" -> WIN + SCISSORS
          else -> LOSE
        }
        "C" -> when (fighters[1]) {
          "X" -> WIN + ROCK
          "Y" -> LOSE + PAPER
          "Z" -> TIE + SCISSORS
          else -> LOSE
        }
        else -> LOSE
      }
    }

  fun part2(input: List<String>): Int =
    part1(input.map { fight ->
      val fighters = fight.split(' ')
      when (fighters[0]) {
        "A" -> when (fighters[1]) {
          "X" -> "A Z"
          "Y" -> "A X"
          "Z" -> "A Y"
          else -> "A"
        }
        "B" -> when (fighters[1]) {
          "X" -> "B X"
          "Y" -> "B Y"
          "Z" -> "B Z"
          else -> "A"
        }
        "C" -> when (fighters[1]) {
          "X" -> "C Y"
          "Y" -> "C Z"
          "Z" -> "C X"
          else -> "A"
        }
        else -> "A"
      }
    })

  // test if implementation meets criteria from the description, like:
  val testInput = readInput("Day02_test")
  println(part1(testInput))
  check(part1(testInput) == 15)
  println(part2(testInput))
  check(part2(testInput) == 12)

  val input = readInput("Day02")
  println(part1(input))
  println(part2(input))
}
