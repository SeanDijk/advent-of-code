import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines

const val DEBUG = true

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

fun Any?.peek() = this.apply { println(this) }



data class Coordinate(val x: Int, val y: Int)


fun debug(block: () -> Unit) {
    if (DEBUG) block()
}

fun Int.isEven() = this % 2 == 0
fun Int.isOdd() = !isEven()
fun Long.isEven() = this % 2L == 0L
fun Long.isOdd() = !isEven()
