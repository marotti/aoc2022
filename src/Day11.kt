import kotlin.math.floor

data class Monkey(
  val num: Int,
  val items: MutableList<ULong>,
  val operation: (ULong) -> ULong,
  val testDivisible: Int,
  val operationTrueMonkey: Int,
  val operationFalseMonkey: Int,
  var itemsInspected: Long = 0.toLong()
)

fun loadMonkeys(input: List<String>): List<Monkey> =
  input.chunked(7).map { input ->
    val monkeyNumber = input[0].split(" ")[1].split(":")[0].toInt()
    val startingItems = input[1].split(":")[1].split(",").map { it.replace(" ", "").toULong() }.toMutableList()
    val equation = input[2].split("=")[1]
    var operation: (ULong) -> ULong = if (equation.contains("+")) {
      { it + if (equation.split("+ ")[1] == "old") it else equation.split("+ ")[1].toULong() }
    } else {
      { it * if (equation.split("* ")[1] == "old") it else equation.split("* ")[1].toULong() }
    }
    val testDivisible = input[3].split("by ")[1].toInt()
    val operationTrueMonkey = input[4].split("monkey ")[1].toInt()
    val operationFalseMonkey = input[5].split("monkey ")[1].toInt()
    Monkey(monkeyNumber, startingItems, operation, testDivisible, operationTrueMonkey, operationFalseMonkey)
  }

fun main() {
  fun part1(input: List<String>): Long {
    val monkeys = loadMonkeys(input)
    for (i in 1..20) {
      for (monkey in monkeys) {
        for (item in monkey.items) {
          val newItem = floor(monkey.operation(item).toDouble() / 3).toULong()
          if ((newItem % monkey.testDivisible.toULong()) == 0.toULong()) {
            monkeys.find { it.num == monkey.operationTrueMonkey }!!.items.add(newItem)
          } else {
            monkeys.find { it.num == monkey.operationFalseMonkey }!!.items.add(newItem)
          }
          monkey.itemsInspected += 1
        }
        monkey.items.clear()
      }
    }
    return monkeys.sortedBy { it.itemsInspected }.takeLast(2).fold(1.toLong()) { acc, monkey -> acc * monkey.itemsInspected }
  }

  fun part2(input: List<String>): Long {
    val monkeys = loadMonkeys(input)
    for (i in 1..10000) {
      for (monkey in monkeys) {
        for (item in monkey.items) {
          val newItem = monkey.operation(item) % monkeys.fold(1.toULong()){ acc, monkey -> acc * monkey.testDivisible.toULong() }
          if ((newItem % monkey.testDivisible.toULong()) == 0.toULong()) {
            monkeys.find { it.num == monkey.operationTrueMonkey }!!.items.add(newItem)
          } else {
            monkeys.find { it.num == monkey.operationFalseMonkey }!!.items.add(newItem)
          }
          monkey.itemsInspected += 1
        }
        monkey.items.clear()
      }
    }
    return monkeys.sortedBy { it.itemsInspected }.takeLast(2)
      .fold(1.toLong()) { acc, monkey -> acc * monkey.itemsInspected }
  }

  // test if implementation meets criteria from the description, like:
  val testInput = readInput("Day11_test")
  val output1 = part1(testInput)
  println(output1)
  check(output1 == 10605.toLong())
  val output2 = part2(testInput)
  println(output2)
  check(output2 == 2713310158.toLong())

  val input = readInput("Day11")
  println(part1(input))
  println(part2(input))
}
