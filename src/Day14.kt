val SAND_ENTRY = Point(500, 0)

data class Point(val x: Int, val y: Int) {
  fun drawStraightLineTo(that: Point): Set<Point> =
    (minOf(this.x, that.x)..maxOf(this.x, that.x)).map { newX ->
      (minOf(this.y, that.y)..maxOf(this.y, that.y)).map { newY ->
        Point(newX, newY)
      }
    }.flatten().toSet()

  override fun toString(): String {
    return "($x,$y)"
  }
}

data class Chamber(val rocks: Set<Point>) {
  val sand = mutableSetOf<Point>()

  val maxDepth: Int = rocks.maxOf { it.y } + 3

  fun addPermanentSand(point: Point) = sand.add(point)

  fun canSandMove(point: Point): Boolean =
    !isOccupied(Point(point.x, point.y + 1)) ||
      !isOccupied(Point(point.x - 1, point.y + 1)) ||
      !isOccupied(Point(point.x + 1, point.y + 1))

  fun moveSand(point: Point): Point =
    when {
      !isOccupied(Point(point.x, point.y + 1)) -> Point(point.x, point.y + 1)
      !isOccupied(Point(point.x - 1, point.y + 1)) -> Point(point.x - 1, point.y + 1)
      !isOccupied(Point(point.x + 1, point.y + 1)) -> Point(point.x + 1, point.y + 1)
      else -> point
    }

  fun willFallForever(point: Point): Boolean = point.y > maxDepth
  fun isStuck(): Boolean = !canSandMove(SAND_ENTRY)
  fun gameOver(point: Point): Boolean = willFallForever(point) || isStuck()

  private fun isOccupied(point: Point): Boolean =
    (point in rocks || point in sand)

  fun addSandParticle(point: Point = SAND_ENTRY): Chamber =
    when {
      gameOver(point) -> this
      canSandMove(point) -> addSandParticle(moveSand(point))
      else -> {
        addPermanentSand(point)
        this
      }
    }
}

val pointRegex = """(\d+),(\d+)""".toRegex()
fun parseRock(input: List<String>): Set<Point> =
  input.map { line ->
    line.split("->")
      .asSequence()
      .filter { it.isNotBlank() }
      .map { pointRegex.find(it) }
      .map { it!!.destructured }
      .map { (x, y) -> Point(x.toInt(), y.toInt()) }
      .windowed(2, 1) { it[0].drawStraightLineTo(it[1]) }
      .flatten()
      .toSet()
  }.flatten()
    .toSet()

fun parseRockWithFloor(input: List<String>): Set<Point> {
  val rocks = parseRock(input)
  val floor = rocks.maxOf { it.y } + 2
  return rocks union Point(500-floor, floor).drawStraightLineTo(Point(500+floor, floor))
}

fun main() {
  fun part1(input: List<String>): Int {
    val chamber = Chamber(parseRock(input))
    do {
      val previousSandSetSize = chamber.sand.size
      chamber.addSandParticle(SAND_ENTRY)
    } while (chamber.sand.size != previousSandSetSize)
    return chamber.sand.size
  }

  fun part2(input: List<String>): Int {
    val chamber = Chamber(parseRockWithFloor(input))
    do {
      val previousSandSetSize = chamber.sand.size
      chamber.addSandParticle(SAND_ENTRY)
    } while (chamber.sand.size != previousSandSetSize)
    return chamber.sand.size+1
  }

  // test if implementation meets criteria from the description, like:
  val testInput = readInput("Day14_test")
  val output1 = part1(testInput)
  println(output1)
  check(output1 == 24)
  val output2 = part2(testInput)
  println(output2)
  check(output2 == 93)

  val input = readInput("Day14")
  println(part1(input))
  println(part2(input))
}
