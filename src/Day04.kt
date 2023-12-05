import kotlin.time.measureTime

fun main() {
    data class Card(val winningNumber: Set<Int>, val myNumbers: Set<Int>)

    fun parse(input: List<String>): List<Card> {
        return input.map { line ->
            val (winningNumbers, myNumbers) = line.split(':', '|')
                .subList(1, 3) // ignore the chunk before the colon.
                .map { chunk ->
                    chunk.trim()
                        .split(' ') // split into chunks containing the numbers
                        .filter { it.isNotEmpty() } // ignore empty strings that may occur due to single numbers
                        .map { it.toInt() }
                }
            Card(winningNumbers.toSet(), myNumbers.toSet())
        }
    }


    fun part1(input: List<String>): Int {
        return parse(input).asSequence()
            .map { it.winningNumber.intersect(it.myNumbers) }
            .filter { it.isNotEmpty() }
            .map { 1 shl (it.size - 1) }
            .sum()
    }

    fun part2(input: List<String>): Int {
        val cardMap = parse(input).withIndex().associate { Pair(it.index + 1, it.value) }
        val resultMap = mutableMapOf<Int, Set<Pair<Int, Card>>>()

        val queue = ArrayDeque<Pair<Int, Card>>()
        var counter = 0;
        queue.addAll(cardMap.toList())

        while (queue.isNotEmpty()) {
            counter++
            val (currentCardNumber, card) = queue.removeFirst()
            val cardsToAdd = resultMap.computeIfAbsent(currentCardNumber) {
                val amountOfWinningNumbers = card.winningNumber.intersect(card.myNumbers).size
                (currentCardNumber + 1..currentCardNumber + amountOfWinningNumbers)
                    .mapNotNull { index -> cardMap[index]?.let { index to it } }
                    .toSet()
            }
            queue.addAll(cardsToAdd)
        }

        return counter
    }

    fun part2Performant(input: List<String>): Int {
        val cardMap = parse(input).withIndex().associate { Pair(it.index + 1, it.value) }
        val resultMap = mutableMapOf<Int, Int>()

        val queue = ArrayDeque<Int>()
        queue.addAll(cardMap.keys)

        fun getCardResult(cardIndex: Int): Int {
            resultMap[cardIndex]?.let { return it }

            val card = cardMap[cardIndex] ?: run {
                resultMap[cardIndex] = 0
                return 0
            }

            val amountOfWinningNumbers = card.winningNumber.intersect(card.myNumbers).size
            val result = (cardIndex + 1..cardIndex + amountOfWinningNumbers).sumOf { getCardResult(it) } + amountOfWinningNumbers
            resultMap[cardIndex] = result
            return result
        }

        var counter = queue.size
        while (queue.isNotEmpty()) {
            counter += getCardResult(queue.removeFirst())
        }

        return counter
    }


    // test if implementation meets criteria from the description, like:
    println("Part 1")
    val testInput = readInput("Day04_test")

    check(part1(testInput).peek() == 13)

    val input = readInput("Day04")
    part1(input).println()

    println("\nPart 2")
    check(part2(testInput).peek() == 30)

    part2(input).println()

    println("\nPart 2 performant")
    check(part2Performant(testInput).peek() == 30)

    part2Performant(input).println()
}
