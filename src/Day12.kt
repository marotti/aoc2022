fun buildDoubleArray(input: List<String>): List<List<Char>> =
  input.map { line -> line.map { char -> char } }

fun findPoint(char: Char, map: List<List<Char>>): Node {
  for (y in map.indices) {
    for (x in map[y].indices) {
      if (map[y][x] == char) return Node(x to y)
    }
  }
  throw Exception()
}

fun findAllPoints(char: Char, map: List<List<Char>>): List<Node> {
  val nodes = mutableListOf<Node>()
  for (y in map.indices) {
    for (x in map[y].indices) {
      if (map[y][x] == char) nodes.add(Node(x to y))
    }
  }
  return nodes.toList()
}

fun Char.weight(): Int =
  when (this) {
    'S' -> 'a'.code
    'E' -> 'z'.code
    else -> this.code
  }

fun buildEdges(map: List<List<Char>>): List<Edge> {
  val edges = mutableListOf<Edge>()
  for (y in map.indices) {
    for (x in map[y].indices) {
      if ((y != 0) && (map[y - 1][x].weight()) <= map[y][x].weight() + 1) edges.add(
        Edge(
          Node(x to y),
          Node(x to y - 1),
          map[y - 1][x].weight()
        )
      )
      if ((x != 0) && (map[y][x - 1].weight() <= map[y][x].weight() + 1)) edges.add(
        Edge(
          Node(x to y),
          Node(x - 1 to y),
          map[y][x - 1].weight()
        )
      )
      if ((y != map.size - 1) && (map[y + 1][x].weight()) <= map[y][x].weight() + 1) edges.add(
        Edge(
          Node(x to y),
          Node(x to y + 1),
          map[y + 1][x].weight()
        )
      )
      if ((x != map[y].size - 1) && (map[y][x + 1].weight() <= map[y][x].weight() + 1)) edges.add(
        Edge(
          Node(x to y),
          Node(x + 1 to y),
          map[y][x + 1].weight()
        )
      )
    }
  }
  return edges.toList()
}


fun main() {
  fun part1(input: List<String>): Int {
    val doubleArray = buildDoubleArray(input)
    return findShortestPath(
      buildEdges(doubleArray),
      findPoint('S', doubleArray),
      findPoint('E', doubleArray)
    ).shortestPath().size - 1
  }

  fun part2(input: List<String>): Int {
    val doubleArray = buildDoubleArray(input)
    val startingPoints = listOf(findPoint('S', doubleArray)) + findAllPoints('a', doubleArray)
    val edges = buildEdges(doubleArray)
    val endPoint = findPoint('E', doubleArray)
    return startingPoints.map {
      findShortestPath(
        edges,
        it,
        endPoint
      )
    }.map { it.shortestPath().size - 1 }.minOf { it }
  }

  // test if implementation meets criteria from the description, like:
  val testInput = readInput("Day12_test")
  val output1 = part1(testInput)
  println(output1)
  check(output1 == 31)
  val output2 = part2(testInput)
  println(output2)
  check(output2 == 29)

  val input = readInput("Day12")
  println(part1(input))
  println(part2(input))
}

data class Node(val location: Pair<Int, Int>)

data class Edge(val node1: Node, val node2: Node, val distance: Int)

fun findShortestPath(edges: List<Edge>, source: Node, target: Node): ShortestPathResult {
  val dist = mutableMapOf<Node, Int>()
  val prev = mutableMapOf<Node, Node?>()
  val q = findDistinctNodes(edges)

  q.forEach { v ->
    dist[v] = Integer.MAX_VALUE
    prev[v] = null
  }
  dist[source] = 0

  while (q.isNotEmpty()) {
    val u = q.minByOrNull { dist[it] ?: 0 }
    q.remove(u)

    if (u == target) {
      break // Found shortest path to target
    }
    edges
      .filter { it.node1 == u }
      .forEach { edge ->
        val v = edge.node2
        val alt = (dist[u] ?: 0) + edge.distance
        if (alt < (dist[v] ?: 0)) {
          dist[v] = alt
          prev[v] = u
        }
      }
  }

  return ShortestPathResult(prev, dist, source, target)
}

private fun findDistinctNodes(edges: List<Edge>): MutableSet<Node> {
  val nodes = mutableSetOf<Node>()
  edges.forEach {
    nodes.add(it.node1)
    nodes.add(it.node2)
  }
  return nodes
}

class ShortestPathResult(val prev: Map<Node, Node?>, val dist: Map<Node, Int>, val source: Node, val target: Node) {
  fun shortestPath(from: Node = source, to: Node = target, list: List<Node> = emptyList()): List<Node> {
    val last = prev[to] ?: return if (from == to) {
      list + to
    } else {
      emptyList()
    }
    return shortestPath(from, last, list) + to
  }

  fun shortestDistance(): Int? {
    val shortest = dist[target]
    if (shortest == Integer.MAX_VALUE) {
      return null
    }
    return shortest
  }
}