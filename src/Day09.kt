fun main() {

    fun <T> List<T>.forEachPaired(action: (T, T) -> Unit) {
        for (i in 0..this.size - 2) {
            action(this[i], this[i + 1])
        }
    }

    fun parse(input: List<String>): List<List<Int>> {
        return input.map { line ->
            line.split(' ')
                .map { it.toInt() }
        }
    }

    fun generateChain(history: List<Int>): MutableList<List<Int>> {
        val rows: MutableList<List<Int>> = mutableListOf(history)

        // go on until we have a row with only 0s. Empty list is all 0s as well I guess :)
        while (!rows.last().all { it == 0 }) {
            val newRow = mutableListOf<Int>()
            // get the differences between all values and create the next row
            rows.last().forEachPaired { first, second ->
                newRow.add(second - first)
            }
            rows.add(newRow)
        }
        return rows
    }

    fun part1(input: List<String>): Long {
        return parse(input)
            // Firstly generate the various measurement-difference rows
            .map { history -> generateChain(history) }
            // now we just want the last value from every row
            .map { rows -> rows.map { it.lastOrNull() ?: 0 } }
            // and sum them together to get the next measurement
            .sumOf { it.sum() }.toLong()
    }

    fun part2(input: List<String>): Long {
        return parse(input)
            // just reverse the inputs and do the same as part 1
            .map { line -> line.reversed() }
            .map { history -> generateChain(history) }
            .map { rows -> rows.map { it.lastOrNull() ?: 0 } }
            .sumOf { it.sum() }.toLong()
    }


    // test if implementation meets criteria from the description, like:
    println("Part 1")
    val testInput = readInput("Day09_test")

    check(part1(testInput).peek() == 114L)

    val input = readInput("Day09")

    part1(input).println()

    println("\nPart 2")

    check(part2(testInput).peek() == 2L)

    part2(input).println()



}
