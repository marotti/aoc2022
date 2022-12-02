import Outcome.Companion.translateOutcome
import Roshambo.Companion.translateHandGesture

fun main() {
  fun getTokens(input: List<String>) =
    input.map { line -> line.split(' ') }
      .filter { tokens -> tokens.size == 2 }
      .map { tokens -> tokens[0] to tokens[1] }

  fun part1(input: List<String>): Int =
    getTokens(input)
      .map { (token1, token2) -> translateHandGesture(token1) to translateHandGesture(token2) }
      .sumOf { (fighter1, fighter2) -> score(fighter1, fighter2) }

  fun part2(input: List<String>): Int =
    getTokens(input)
      .map { (token1, token2) -> translateHandGesture(token1) to translateOutcome(token2) }
      .map { (fighter1, result) -> fighter1 to fighter1.whichHandGesture(result) }
      .sumOf { (fighter1, fighter2) -> score(fighter1, fighter2) }

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

enum class Roshambo(val points: Int, val codeList: List<String>) {
  ROCK(1, listOf("A", "X")) {
    override fun fight(fighter: Roshambo): Outcome =
      when (fighter) {
        ROCK -> Outcome.TIE
        PAPER -> Outcome.LOSS
        SCISSORS -> Outcome.WIN
      }

    override fun whichHandGesture(result: Outcome): Roshambo =
      when (result) {
        Outcome.WIN -> PAPER
        Outcome.TIE -> ROCK
        Outcome.LOSS -> SCISSORS
      }
  },
  PAPER(2, listOf("B", "Y")) {
    override fun fight(fighter: Roshambo): Outcome =
      when (fighter) {
        ROCK -> Outcome.WIN
        PAPER -> Outcome.TIE
        SCISSORS -> Outcome.LOSS
      }

    override fun whichHandGesture(result: Outcome): Roshambo =
      when (result) {
        Outcome.WIN -> SCISSORS
        Outcome.TIE -> PAPER
        Outcome.LOSS -> ROCK
      }
  },
  SCISSORS(3, listOf("C", "Z")) {
    override fun fight(fighter: Roshambo): Outcome =
      when (fighter) {
        ROCK -> Outcome.LOSS
        PAPER -> Outcome.WIN
        SCISSORS -> Outcome.TIE
      }

    override fun whichHandGesture(result: Outcome): Roshambo =
      when (result) {
        Outcome.WIN -> ROCK
        Outcome.TIE -> SCISSORS
        Outcome.LOSS -> PAPER
      }
  };

  abstract fun fight(fighter: Roshambo): Outcome
  abstract fun whichHandGesture(result: Outcome): Roshambo

  companion object {
    fun translateHandGesture(input: String): Roshambo =
      when {
        (input in ROCK.codeList) -> ROCK
        (input in PAPER.codeList) -> PAPER
        else -> SCISSORS
      }
  }
}

enum class Outcome(val points: Int, val code: String) {
  WIN(6, "Z"),
  LOSS(0, "X"),
  TIE(3, "Y");

  companion object {
    fun translateOutcome(input: String): Outcome =
      when (input) {
        WIN.code -> WIN
        LOSS.code -> LOSS
        else -> TIE
      }
  }
}

fun score(fighter1: Roshambo, fighter2: Roshambo): Int = fighter2.points + (fighter2.fight(fighter1)).points
