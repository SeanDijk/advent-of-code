import kotlin.math.max

fun main() {
    data class Grab(val red: Int, val green: Int, val blue: Int)
    data class Game(val id: Int, val grabs: List<Grab>)

    fun parse(input: List<String>): List<Game> {
        return input.map { line ->
            val (gameChunk, grabChunk) = line.split(':')
            val gameId = gameChunk.removePrefix("Game ").toInt()
            val grabs = grabChunk.split(';').map { grab ->
                grab.split(',').associate { amountAndColor ->
                    val (amount, color) = amountAndColor.trim().split(' ')
                    color to amount.toInt()
                }.let {
                    Grab(
                        it.getOrDefault("red", 0),
                        it.getOrDefault("green", 0),
                        it.getOrDefault("blue", 0)
                    )
                }
            }
            Game(gameId, grabs)
        }
    }

    fun part1(input: List<String>): Int {
        return parse(input)
            .filter { game ->
                game.grabs.none { grab ->
                    grab.red > 12 || grab.green > 13 || grab.blue > 14
                }
            }
            .sumOf { game -> game.id }
    }

    fun part2(input: List<String>): Int {
        fun Grab.maxValues(other: Grab) = Grab(max(red, other.red), max(green, other.green), max(blue, other.blue))
        return parse(input).sumOf { game ->
            game.grabs.reduce { acc, grab -> acc.maxValues(grab) }.let {
                it.red * it.blue * it.green
            }
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")

    check(part1(testInput).peek() == 8)

    val input = readInput("Day02")
    part1(input).println()

    check(part2(testInput).peek() == 2286)

    part2(input).println()
}
