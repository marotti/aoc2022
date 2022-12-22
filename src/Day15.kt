val inputRegex = """Sensor at x=(-*\d+), y=(-*\d+): closest beacon is at x=(-*\d+), y=(-*\d+)""".toRegex()

data class Radar(val sensorBeacons: Set<Pair<Point, Point>>) {
  val sensorSet = sensorBeacons.map { it.first }.toSet()
  val beaconSet = sensorBeacons.map { it.second }.toSet()

  val distanceSet: Set<Pair<Point, Int>> =
    sensorBeacons.map { (sensor, beacon) -> Pair(sensor, sensor.distanceTo(beacon)) }.toSet()

  fun createSensorCoverageMapForRow(row: Int): Set<Point> =
    sensorBeacons
      .asSequence()
      .filter { (sensor, beacon) -> row in sensor.y - sensor.distanceTo(beacon)..sensor.y + sensor.distanceTo(beacon) }
      .map { (sensor, beacon) ->
        (sensor.x - sensor.distanceTo(beacon)..sensor.x + sensor.distanceTo(beacon))
          .map { Point(it, row) }
          .filter { sensor.distanceTo(it) <= sensor.distanceTo(beacon) }
      }
      .flatten().toSet()
      .minus(beaconSet)
      .minus(sensorSet)
      .toSet()

  fun findMissingCoverage(minRange: Int, maxRange: Int): Point {
    for (x in minRange..maxRange)
      for (y in minRange..maxRange)
        if (distanceSet.all { (sensor, distance) -> sensor.distanceTo(Point(x,y)) > distance })
          return Point(x,y)
    throw Exception()
  }
}

fun main() {
  fun getSensorsBeacons(input: List<String>) = input
    .map { inputRegex.find(it) }
    .map { it!!.destructured }
    .map { (sensorX, sensorY, beaconX, beaconY) ->
      Pair(
        Point(sensorX.toInt(), sensorY.toInt()),
        Point(beaconX.toInt(), beaconY.toInt())
      )
    }.toSet()

  fun part1(input: List<String>, row: Int): Int =
    Radar(getSensorsBeacons(input)).createSensorCoverageMapForRow(row).count { it.y == row }

  fun part2(input: List<String>, minRange: Int, maxRange: Int): Int {
    val point = Radar(getSensorsBeacons(input)).findMissingCoverage(minRange, maxRange)
    return (point.x*4000000)+point.y
  }

  // test if implementation meets criteria from the description, like:
  val testInput = readInput("Day15_test")
  val output1 = part1(testInput, 10)
  println(output1)
  check(output1 == 26)
  val output2 = part2(testInput, 0, 20)
  println(output2)
  check(output2 == 56000011)

  val input = readInput("Day15")
  println(part1(input, 2000000))
  println(part2(input, 0, 4000000))
}
