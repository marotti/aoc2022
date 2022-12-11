import kotlin.math.abs

enum class Direction(val abbreviation: String) {
  UP("U") {
    override fun moveHead(head: Coordinate) = Coordinate(head.x, head.y + 1)
    override fun moveTailAccordingToHead(head: Coordinate, tail: Coordinate) = Coordinate(head.x, tail.y + 1)
  },
  DOWN("D") {
    override fun moveHead(head: Coordinate) = Coordinate(head.x, head.y - 1)
    override fun moveTailAccordingToHead(head: Coordinate, tail: Coordinate) = Coordinate(head.x, tail.y - 1)
  },
  LEFT("L") {
    override fun moveHead(head: Coordinate) = Coordinate(head.x - 1, head.y)
    override fun moveTailAccordingToHead(head: Coordinate, tail: Coordinate) = Coordinate(tail.x - 1, head.y)
  },
  RIGHT("R") {
    override fun moveHead(head: Coordinate) = Coordinate(head.x + 1, head.y)
    override fun moveTailAccordingToHead(head: Coordinate, tail: Coordinate) = Coordinate(tail.x + 1, head.y)
  },
  DIAG_UP_RIGHT("") {
    override fun moveHead(head: Coordinate) = Coordinate(head.x + 1, head.y + 1)
    override fun moveTailAccordingToHead(head: Coordinate, tail: Coordinate) = Coordinate(tail.x + 1, tail.y + 1)
  },
  DIAG_DOWN_RIGHT("") {
    override fun moveHead(head: Coordinate) = Coordinate(head.x + 1, head.y - 1)
    override fun moveTailAccordingToHead(head: Coordinate, tail: Coordinate) = Coordinate(tail.x + 1, tail.y - 1)
  },
  DIAG_UP_LEFT("") {
    override fun moveHead(head: Coordinate) = Coordinate(head.x - 1, head.y + 1)
    override fun moveTailAccordingToHead(head: Coordinate, tail: Coordinate) = Coordinate(tail.x - 1, tail.y + 1)
  },
  DIAG_DOWN_LEFT("") {
    override fun moveHead(head: Coordinate) = Coordinate(head.x - 1, head.y - 1)
    override fun moveTailAccordingToHead(head: Coordinate, tail: Coordinate) = Coordinate(tail.x - 1, tail.y - 1)
  };

  abstract fun moveHead(head: Coordinate): Coordinate
  abstract fun moveTailAccordingToHead(head: Coordinate, tail: Coordinate): Coordinate

  companion object {
    fun getDirection(abbreviation: String) = values().find { it.abbreviation == abbreviation }
  }
}

data class Coordinate(val x: Int, val y: Int)

fun shouldTailMove(headCoordinate: Coordinate, tailCoordinate: Coordinate): Boolean {
  if (abs(headCoordinate.x - tailCoordinate.x) <= 1 && abs(headCoordinate.y - tailCoordinate.y) <= 1) return false
  return true
}

fun whichDirectionMoved(headCoordinate: Coordinate, tailCoordinate: Coordinate): Direction =
  if ((headCoordinate.x - tailCoordinate.x) == 2 && (headCoordinate.y - tailCoordinate.y) == 2) Direction.DIAG_UP_RIGHT
  else if ((headCoordinate.x - tailCoordinate.x) == -2 && (headCoordinate.y - tailCoordinate.y) == 2) Direction.DIAG_UP_LEFT
  else if ((headCoordinate.x - tailCoordinate.x) == -2 && (headCoordinate.y - tailCoordinate.y) == -2) Direction.DIAG_DOWN_LEFT
  else if ((headCoordinate.x - tailCoordinate.x) == 2 && (headCoordinate.y - tailCoordinate.y) == -2) Direction.DIAG_DOWN_RIGHT
  else if ((headCoordinate.x - tailCoordinate.x) > 1) Direction.RIGHT
  else if ((headCoordinate.y - tailCoordinate.y) > 1) Direction.UP
  else if ((tailCoordinate.x - headCoordinate.x) > 1) Direction.LEFT
  else if ((tailCoordinate.y - headCoordinate.y) > 1) Direction.DOWN
  else throw Exception()


fun main() {
  fun part1(input: List<String>): Int {
    val coordinates = mutableListOf(Coordinate(0, 0), Coordinate(0, 0))
    val tailVisits = mutableSetOf(coordinates[1])
    for (line: String in input) {
      val direction = Direction.getDirection(line.split(" ")[0])!!
      val magnitude = line.split(" ")[1].toInt()
      for (i in 0 until magnitude) {
        coordinates[0] = direction.moveHead(coordinates[0])
        if (shouldTailMove(coordinates[0], coordinates[1])) {
          coordinates[1] = direction.moveTailAccordingToHead(coordinates[0], coordinates[1])
        }
        tailVisits.add(coordinates[1])
      }
    }
    return tailVisits.size
  }

  fun part2(input: List<String>): Int {
    val coordinates = mutableListOf(
      Coordinate(0, 0),
      Coordinate(0, 0),
      Coordinate(0, 0),
      Coordinate(0, 0),
      Coordinate(0, 0),
      Coordinate(0, 0),
      Coordinate(0, 0),
      Coordinate(0, 0),
      Coordinate(0, 0),
      Coordinate(0, 0)
    )//goofy
    val tailVisits = mutableSetOf(coordinates[9])
    for (line: String in input) {
      val direction = Direction.getDirection(line.split(" ")[0])!!
      val magnitude = line.split(" ")[1].toInt()
      for (i in 0 until magnitude) {
        coordinates[0] = direction.moveHead(coordinates[0])
        for (y in 1 until coordinates.size) {
          if (shouldTailMove(coordinates[y - 1], coordinates[y])) {
            val changeDirection = whichDirectionMoved(coordinates[y - 1], coordinates[y])
            coordinates[y] = changeDirection.moveTailAccordingToHead(coordinates[y - 1], coordinates[y])
          }
        }
        tailVisits.add(coordinates[9])
      }
    }
    return tailVisits.size
  }

// test if implementation meets criteria from the description, like:
  val testInput = readInput("Day09_test")
  val output1 = part1(testInput)
  println(output1)
  check(output1 == 13)
  val output2 = part2(testInput)
  println(output2)
  check(output2 == 1)

  val input = readInput("Day09")
  println(part1(input))
  println(part2(input))
}
