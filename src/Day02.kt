fun main() {
  fun getTokens(input: List<String>) =
    input.map { line -> line.split(' ') }
      .filter { tokens -> tokens.size == 2 }
      .map { tokens -> Pair(tokens[0], tokens[1])}

  fun part1(input: List<String>): Int =
    getTokens(input)
      .map { (token1, token2) -> Pair(RPS.getRPS(token1), RPS.getRPS(token2)) }
      .sumOf { (fighter1, fighter2) -> score(fighter1, fighter2) }

  fun part2(input: List<String>): Int =
    getTokens(input)
      .map { (token1, token2) -> Pair(RPS.getRPS(token1), WLT.getWLT(token2)) }
      .map { (fighter, result) -> Pair(fighter, fighter.findRPS(result)) }
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

enum class RPS(val points: Int, val codeList: List<String>) {
  ROCK(1, listOf("A", "X")) {
    override fun fight(fighter: RPS): WLT =
      when (fighter) {
        ROCK -> WLT.TIE
        PAPER -> WLT.LOSS
        SCISSORS -> WLT.WIN
      }

    override fun findRPS(result: WLT): RPS =
      when (result) {
        WLT.WIN -> PAPER
        WLT.TIE -> ROCK
        WLT.LOSS -> SCISSORS
      }
  },
  PAPER(2, listOf("B", "Y")) {
    override fun fight(fighter: RPS): WLT =
      when (fighter) {
        ROCK -> WLT.WIN
        PAPER -> WLT.TIE
        SCISSORS -> WLT.LOSS
      }

    override fun findRPS(result: WLT): RPS =
      when (result) {
        WLT.WIN -> SCISSORS
        WLT.TIE -> PAPER
        WLT.LOSS -> ROCK
      }
  },
  SCISSORS(3, listOf("C", "Z")) {
    override fun fight(fighter: RPS): WLT =
      when (fighter) {
        ROCK -> WLT.LOSS
        PAPER -> WLT.WIN
        SCISSORS -> WLT.TIE
      }

    override fun findRPS(result: WLT): RPS =
      when (result) {
        WLT.WIN -> ROCK
        WLT.TIE -> SCISSORS
        WLT.LOSS -> PAPER
      }
  };

  abstract fun fight(fighter: RPS): WLT
  abstract fun findRPS(result: WLT): RPS

  companion object {
    fun getRPS(input: String): RPS =
      when {
        (input in ROCK.codeList) -> ROCK
        (input in PAPER.codeList) -> PAPER
        else -> SCISSORS
      }
  }
}

enum class WLT(val points: Int, val code: String) {
  WIN(6, "Z"),
  LOSS(0, "X"),
  TIE(3, "Y");

  companion object {
    fun getWLT(input: String): WLT =
      when (input) {
        WIN.code -> WIN
        LOSS.code -> LOSS
        else -> TIE
      }
  }
}

fun score(fighter1: RPS, fighter2: RPS): Int = fighter2.points + (fighter2.fight(fighter1)).points
