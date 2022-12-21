const val DIVIDER_PATTERN_ONE = "[[2]]"
const val DIVIDER_PATTERN_TWO = "[[6]]"

sealed interface Token : Comparable<Token>

data class IntegerToken(val value: Int) : Token {
  override fun compareTo(other: Token): Int =
    when (other) {
      is IntegerToken -> value.compareTo(other.value)
      is ListToken -> ListToken(listOf(this)).compareTo(other)
    }

  override fun toString(): String {
    return value.toString()
  }
}

data class ListToken(val value: List<Token>) : Token {
  override fun compareTo(other: Token): Int = when (other) {
    is IntegerToken -> this.compareTo(ListToken(listOf(other)))
    is ListToken -> value.zip(other.value)
      .map { it.first compareTo it.second }
      .firstOrNull { it != 0 } ?: (value.size compareTo other.value.size)
  }

  override fun toString(): String {
    return value.joinToString(",", "[", "]")
  }
}

fun buildTokenList(inputList: List<Token>, index: Int, input: String): ListToken =
  when (input[index]) {
    ',' -> buildTokenList(inputList, index + 1, input)
    '[' -> {
      val endOfListIndex = findListEnd(input, index)
      val token = buildTokenList(listOf(), 1, input.substring(index, endOfListIndex + 1))
      buildTokenList(inputList.plus(token), endOfListIndex + 1, input)
    }
    ']' -> ListToken(inputList)
    else -> {
      val substr = input.substring(index)
      val number = if (substr.indexOf(",") > 0) substr.substring(0 until substr.indexOf(","))
      else if (substr.indexOf("]") > 0) substr.substring(0 until substr.indexOf("]"))
      else substr
      buildTokenList(inputList.plus(IntegerToken(number.toInt())), index + 1, input)
    }
  }

fun findListEnd(input: String, initialIndex: Int): Int =
  input.substring(initialIndex + 1).runningFold(1) { acc, c ->
    when (c) {
      '[' -> acc + 1
      ']' -> acc - 1
      else -> acc
    }
  }.indexOf(0) + initialIndex

fun parseInputTokenList(input: String): ListToken =
  buildTokenList(listOf(), 1, input)

fun main() {
  fun part1(input: List<String>): Int =
    input.chunked(3).filter { it.isNotEmpty() }.mapIndexed { index: Int, strings: List<String> ->
      val leftList = parseInputTokenList(strings[0])
      val rightList = parseInputTokenList(strings[1])
      if (leftList <= rightList) index + 1
      else 0
    }.sum()

  fun part2(input: List<String>): Int {
    val sorted = input
      .asSequence()
      .plus(DIVIDER_PATTERN_ONE)
      .plus(DIVIDER_PATTERN_TWO)
      .filter { it.isNotEmpty() }
      .map { parseInputTokenList(it) }
      .sorted()
      .toList()
    val indexOfFirstDivider = sorted.indexOf(parseInputTokenList(DIVIDER_PATTERN_ONE)) + 1
    val indexOfSecondDivider = sorted.indexOf(parseInputTokenList(DIVIDER_PATTERN_TWO)) + 1
    return  indexOfFirstDivider * indexOfSecondDivider
  }

  // test if implementation meets criteria from the description, like:
  val testInput = readInput("Day13_test")
  val output1 = part1(testInput)
  println(output1)
  check(output1 == 13)
  val output2 = part2(testInput)
  println(output2)
  check(output2 == 140)

  val input = readInput("Day13")
  println(part1(input))
  println(part2(input))
}
