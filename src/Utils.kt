import java.io.File
import java.math.BigInteger
import java.security.MessageDigest
import kotlin.math.abs

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("src", "$name.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5(): String = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16)

data class Point(val x: Int, val y: Int) {
  fun fillPointsBetween(that: Point): Set<Point> =
    (minOf(this.x, that.x)..maxOf(this.x, that.x)).map { newX ->
      (minOf(this.y, that.y)..maxOf(this.y, that.y)).map { newY ->
        Point(newX, newY)
      }
    }.flatten().toSet()

  override fun toString(): String {
    return "($x,$y)"
  }

  fun distanceTo(that: Point): Int = abs(this.x - that.x) + abs(this.y - that.y)

  fun fillInByDistance(that: Point): Set<Point> =
    (this.x - distanceTo(that)..this.x + distanceTo(that)).map { newX ->
      (this.y - distanceTo(that)..this.y + distanceTo(that)).map { newY ->
        Point(newX, newY)
      }
    }.flatten().filter { distanceTo(it) <= distanceTo(that) }.toSet()

  fun fillInByDistance(distance:Int): Set<Point> =
    (this.x - distance..this.x + distance).map { newX ->
      (this.y - distance..this.y + distance).map { newY ->
        Point(newX, newY)
      }
    }.flatten().filter { distanceTo(it) <= distance }.toSet()
}
