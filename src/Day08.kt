fun buildMap(input: List<String>) = input.map { string -> string.toList().map { it.digitToInt() } }

fun countVisible(rowNum: Int, colNum: Int, map: List<List<Int>>): Int =
  if (listOf(
      isVisibleLeft(rowNum, colNum, map),
      isVisibleRight(rowNum, colNum, map),
      isVisibleUp(rowNum, colNum, map),
      isVisibleDown(rowNum, colNum, map)
    ).any { it }
  ) 1 else 0

fun isVisibleLeft(rowNum: Int, colNum: Int, map: List<List<Int>>): Boolean =
  !map.subList(0, rowNum).any { it[colNum] >= map[rowNum][colNum] }

fun isVisibleRight(rowNum: Int, colNum: Int, map: List<List<Int>>): Boolean =
  !map.subList(rowNum + 1, map.size).any { it[colNum] >= map[rowNum][colNum] }

fun isVisibleUp(rowNum: Int, colNum: Int, map: List<List<Int>>): Boolean =
  !map[rowNum].subList(0, colNum).any { it >= map[rowNum][colNum] }

fun isVisibleDown(rowNum: Int, colNum: Int, map: List<List<Int>>): Boolean =
  !map[rowNum].subList(colNum + 1, map[rowNum].size).any { it >= map[rowNum][colNum] }

fun <T> Iterable<T>.takeWhileInclusive(predicate: (T) -> Boolean): List<T> {
  var shouldContinue = true
  return takeWhile {
    val result = shouldContinue
    shouldContinue = predicate(it)
    result
  }
}

fun countVisibleLeft(rowNum: Int, colNum: Int, map: List<List<Int>>): Int =
  map.subList(0, rowNum).reversed().takeWhileInclusive { it[colNum] < map[rowNum][colNum] }.count()

fun countVisibleRight(rowNum: Int, colNum: Int, map: List<List<Int>>): Int =
  map.subList(rowNum + 1, map.size).takeWhileInclusive { it[colNum] < map[rowNum][colNum] }.count()

fun countVisibleUp(rowNum: Int, colNum: Int, map: List<List<Int>>): Int =
  map[rowNum].subList(0, colNum).reversed().takeWhileInclusive { it < map[rowNum][colNum] }.count()

fun countVisibleDown(rowNum: Int, colNum: Int, map: List<List<Int>>): Int =
  map[rowNum].subList(colNum + 1, map[rowNum].size).takeWhileInclusive { it < map[rowNum][colNum] }.count()


fun main() {
  fun part1(input: List<String>): Int {
    val map = buildMap(input)
    return map.size * 2 + map[0].size * 2 - 4 + map.mapIndexed { rowNum, row ->
      row.mapIndexed { colNum, col ->
        if (rowNum != 0 && rowNum != map.size - 1 && colNum != 0 && colNum != map.size - 1) countVisible(
          rowNum,
          colNum,
          map
        ) else 0
      }.sum()
    }.sum()
  }

  fun part2(input: List<String>): Int {
    val map = buildMap(input)
    return map.mapIndexed { rowNum, row ->
      row.mapIndexed { colNum, col ->
        countVisibleLeft(rowNum, colNum, map) *
          countVisibleRight(rowNum, colNum, map) *
          countVisibleUp(rowNum, colNum, map) *
          countVisibleDown(rowNum, colNum, map)
      }.maxOf { it }
    }.maxOf { it }
  }

  // test if implementation meets criteria from the description, like:
  val testInput = readInput("Day08_test")
  val output1 = part1(testInput)
  println(output1)
  check(output1 == 21)
  val output2 = part2(testInput)
  println(output2)
  check(output2 == 8)

  val input = readInput("Day08")
  println(part1(input))
  println(part2(input))
}
