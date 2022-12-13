import java.util.LinkedList
import kotlin.collections.HashMap
import kotlin.collections.HashSet

fun buildDoubleArray(input: List<String>): List<List<Char>> =
  input.map { line -> line.map { char -> char } }

fun findPoint(char: Char, map: List<List<Char>>): Node {
  for (y in map.indices) {
    for (x in map[y].indices) {
      if (map[y][x] == char) return Node(x to y, char)
    }
  }
  throw Exception()
}

fun findAllPoints(char: Char, map: List<List<Char>>): List<Node> {
  val nodes = mutableListOf<Node>()
  for (y in map.indices) {
    for (x in map[y].indices) {
      if (map[y][x] == char) nodes.add(Node(x to y, char))
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

data class Node(val location: Pair<Int, Int>, val data: Char) {
  var weight = 1
  var distance = Int.MAX_VALUE
}

class Graph {
  val adjacencyMap: HashMap<Node, HashSet<Node>> = HashMap()

  fun addEdge(sourceVertex: Node, destinationVertex: Node) {
    adjacencyMap
      .computeIfAbsent(sourceVertex) { HashSet() }
      .add(destinationVertex)
  }
}

fun buildGraph(map: List<List<Char>>): Graph {
  val graph = Graph()
  for (y in map.indices) {
    for (x in map[y].indices) {
      if ((y != 0) && (map[y-1][x].weight() - map[y][x].weight() <= 1)) graph.addEdge(
        Node(x to y, map[y][x]),
        Node(x to y - 1, map[y-1][x])
      )
      if ((x != 0) && (map[y][x-1].weight() - map[y][x].weight() <= 1)) graph.addEdge(
        Node(x to y, map[y][x]),
        Node(x - 1 to y, map[y][x-1])
      )
      if ((y != map.size - 1) && (map[y + 1][x].weight() - map[y][x].weight() <= 1)) graph.addEdge(
        Node(x to y, map[y][x]),
        Node(x to y + 1, map[y+1][x])
      )
      if ((x != map[y].size - 1) && (map[y][x + 1].weight() - map[y][x].weight() <= 1)) graph.addEdge(
        Node(x to y, map[y][x]),
        Node(x + 1 to y, map[y][x+1])
      )
    }
  }
  return graph
}

fun breadthFirstSearchDistance(
  graph: Graph,
  start: Node, end: Node
): Int {
  val queue = LinkedList<Node>()
  val visited = HashSet<Node>()

  visited.add(start)
  start.distance=0
  queue.add(start)

  while (queue.isNotEmpty()) {
    val point = queue.remove()
    for (node in graph.adjacencyMap[point] ?: emptyList()) {
      if (!visited.contains(node)) {
        visited.add(node)
        node.distance = point.distance+node.weight
        queue.add(node)
        if (node == end) return node.distance
      }
    }
  }
  return Int.MAX_VALUE
}

fun main() {
  fun part1(input: List<String>): Int {
    val doubleArray = buildDoubleArray(input)
    val graph = buildGraph(doubleArray)
    val startPoint = findPoint('S', doubleArray)
    val endPoint = findPoint('E', doubleArray)
    return breadthFirstSearchDistance(
      graph,
      startPoint,
      endPoint
    )
  }

  fun part2(input: List<String>): Int {
    val doubleArray = buildDoubleArray(input)
    val graph = buildGraph(doubleArray)
    val startingPoints = listOf(findPoint('S', doubleArray)) + findAllPoints('a', doubleArray)
    val endPoint = findPoint('E', doubleArray)
    return startingPoints.map {
      breadthFirstSearchDistance(
        graph,
        it,
        endPoint
      )
    }.minOf { it }
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

