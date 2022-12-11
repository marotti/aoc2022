enum class Instruction {
  NOOP, ADDX
}

data class Operation(val instruction: Instruction, val value: Int)

fun main() {
  fun getOperations(input: List<String>): MutableList<Operation> {
    val operations = mutableListOf<Operation>()
    for (line: String in input) {
      if ("noop" in line) operations.add(Operation(Instruction.NOOP, 0))
      if ("addx" in line) operations.add(Operation(Instruction.ADDX, line.split(" ")[1].toInt()))
    }
    return operations
  }

  fun part1(input: List<String>): Int {
    val operations = getOperations(input)
    var register = 1
    var opPointer = 0
    var pauseCycle = true
    val signalStrength = mutableListOf<Int>()

    for (cycle in 1..220) {
      if (cycle in listOf(20, 60, 100, 140, 180, 220)) signalStrength.add(cycle * register)

      if (operations[opPointer].instruction == Instruction.NOOP) opPointer += 1
      else if (pauseCycle) pauseCycle = false
      else {
        register += operations[opPointer].value
        opPointer += 1
        pauseCycle = true
      }
    }
    return signalStrength.sum()
  }

  fun part2(input: List<String>): MutableList<MutableList<Char>> {
    val operations = getOperations(input)
    var register = 1
    var opPointer = 0
    var pauseCycle = true
    var cycleposition = 0
    var line = mutableListOf<Char>()
    val image = mutableListOf<MutableList<Char>>()

    for (cycle in 1..240) {
      if (cycle in listOf(41, 81, 121, 161, 201)) {
        cycleposition = 0
        image.add(line)
        line = mutableListOf()
      }
      println("$cycle: $register ${cycleposition-1}..${cycleposition+1}")
      if (register in listOf(cycleposition - 1, cycleposition, cycleposition + 1)) line.add('#') else line.add('.')
      if (operations[opPointer].instruction == Instruction.NOOP) opPointer += 1
      else if (pauseCycle) pauseCycle = false
      else {
        register += operations[opPointer].value
        opPointer += 1
        pauseCycle = true
      }
      cycleposition += 1
    }
    image.add(line)
    return image
  }

  // test if implementation meets criteria from the description, like:
  val testInput = readInput("Day10_test")
  val output1 = part1(testInput)
  println(output1)
  check(output1 == 13140)
  val output2 = part2(testInput)
  output2.forEach { println(it.joinToString("")) }
  println(output2)
//  check(output2 == 5)

  val input = readInput("Day10")
  println(part1(input))
  part2(input).forEach { println(it.joinToString("")) }
}
