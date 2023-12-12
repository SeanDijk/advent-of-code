fun main() {

    data class Game(val hand: String, val bid: Long)


    fun compareHandByCharScore(cardComparator: Comparator<Char>): (a: String, b: String) -> Int {
        return { hand1, hand2 ->
            hand1.asSequence()
                .mapIndexed { index, char -> cardComparator.compare(char, hand2[index]) }
                .firstOrNull { it != 0 } ?: 0
        }
    }


    fun parse(input: List<String>): Sequence<Game> {
        return input.asSequence()
            .map { it.split(' ') }
            .map { Game(it[0], it[1].toLong()) }
    }

    fun part1(input: List<String>): Long {

        val cardComparator: Comparator<Char> = Comparator.comparing {
            when (it) {
                'A' -> 14
                'K' -> 13
                'Q' -> 12
                'J' -> 11
                'T' -> 10
                else -> Character.getNumericValue(it)
            }
        }

        val handComparator: Comparator<String> = Comparator.comparing<String?, Int?> { hand ->
            val charCounts = hand.groupingBy { it }.eachCount()
            val highestOfAKind = charCounts.maxOf { it.value }

            return@comparing when (highestOfAKind) {
                5 -> 5 // five of a kind
                4 -> 4 // four of a kind
                3 -> {
                    if (charCounts.containsValue(2)) {
                        3 // full house
                    } else {
                        2 // three of a kind
                    }
                }

                2 -> {
                    if (charCounts.count { it.value == 2 } == 2) {
                        1 // two pair
                    } else {
                        0 // one pair
                    }
                }

                else -> -1 // high card
            }
        }.thenComparator(compareHandByCharScore(cardComparator))


        return parse(input)
            .sortedWith(Comparator.comparing(Game::hand, handComparator))
            .mapIndexed { index: Int, game: Game -> (index + 1) * game.bid }
            .sum()
    }

    fun part2(input: List<String>): Long {
        val cardComparator: Comparator<Char> = Comparator.comparing {
            when (it) {
                'A' -> 14
                'K' -> 13
                'Q' -> 12
                'J' -> 1
                'T' -> 10
                else -> Character.getNumericValue(it)
            }
        }

        val handComparator: Comparator<String> = Comparator.comparing<String?, Int?> { hand ->
            val charCounts = hand.groupingBy { it }.eachCount()
            val charCountsWithoutJoker = charCounts.filter { it.key != 'J' }
            val jokerCount = charCounts['J'] ?: 0
            val highest = charCountsWithoutJoker
                .maxByOrNull { it.value }
                ?.toPair()
                ?: ('x' to 0)
            val secondHighestValue =
                charCountsWithoutJoker.filter { it.key != highest.first }
                    .maxOfOrNull { it.value }
                    ?: 0

            return@comparing when (highest.second + jokerCount) {
                5 -> 5 // five of a kind
                4 -> 4 // four of a kind
                3 -> {
                    if (secondHighestValue == 2) {
                        3 // full house
                    } else {
                        2 // three of a kind
                    }
                }

                2 -> {
                    if (charCounts.count { it.value == 2 } == 2) {
                        1 // two pair
                    } else {
                        0 // one pair
                    }
                }

                else -> -1 // high card
            }
        }.thenComparator(compareHandByCharScore(cardComparator))


        return parse(input)
            .sortedWith(Comparator.comparing(Game::hand, handComparator))
            .mapIndexed { index: Int, game: Game -> (index + 1) * game.bid }
            .sum()
    }


    // test if implementation meets criteria from the description, like:
    println("Part 1")
    val testInput = readInput("Day07_test")

    check(part1(testInput).peek() == 6440L)

    val input = readInput("Day07")
    part1(input).println()

    println("\nPart 2")

//    check(part2(testInput).peek() == 5905L)

    part2(input).println()
}
