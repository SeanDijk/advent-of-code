
fun main() {
    fun part1(input: List<String>): Int {
        return input.asSequence()
            .map { line -> line.first { it.isDigit() }.toString() + line.reversed().first { it.isDigit() }.toString() }
            .map { it.toInt() }
            .sum()
    }

    fun part2(input: List<String>): Int {
        return part1(input.map {
            // Lets start creating hacky code :)
            // We want to change to number to digits, but what do we do if there is something like twoone in the text?
            // Then just wrap the digit in the text! 🤮
              it.replace("one", "one1one")
                .replace("two", "two2two")
                .replace("three", "three3three")
                .replace("four", "four4four")
                .replace("five", "five5five")
                .replace("six", "six6six")
                .replace("seven", "seven7seven")
                .replace("eight", "eight8eight")
                .replace("nine", "nine9nine")
        })
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 142)

    val input = readInput("Day01")
    part1(input).println()

    val testInput2 = readInput("Day01_test_part_2")
    check(part2(testInput2) == 281)

    part2(input).println()
}
