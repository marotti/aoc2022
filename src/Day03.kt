val charList = ('a'..'z').toMutableList() + ('A'..'Z').toMutableList()

fun Char.getAlphabetValue(): Int = charList.indexOf(this) + 1
fun String.findCommonChar(string: String): Char = findCommonChar(setOf(string))
fun String.findCommonChar(stringSet: Set<String>): Char = this.first { check -> stringSet.all { check in it } }
fun getItemSet(rucksackList: List<Rucksack>): Set<String> = rucksackList.map { it.items }.toSet()

data class Rucksack(val items: String) {
  val compartment1 = items.substring(0, items.length / 2)
  val compartment2 = items.substring(items.length / 2)
}

fun main() {
  fun part1(input: List<String>): Int =
    input
      .map { Rucksack(it) }
      .sumOf { rucksack -> rucksack.compartment1.findCommonChar(rucksack.compartment2).getAlphabetValue() }

  fun part2(input: List<String>): Int =
    input
      .map { Rucksack(it) }
      .chunked(3)
      .sumOf { elves -> elves.first().items.findCommonChar(getItemSet(elves)).getAlphabetValue() }

  // test if implementation meets criteria from the description, like:
  val testInput = readInput("Day03_test")
  println(part1(testInput))
  check(part1(testInput) == 157)
  println(part2(testInput))
  check(part2(testInput) == 70)

  val input = readInput("Day03")
  println(part1(input))
  println(part2(input))
}
