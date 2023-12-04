fun main() {
    data class Number(val coordinateStart: Coordinate, val coordinateEnd: Coordinate, val value: Int)

    fun parse(input: List<String>): Pair<MutableMap<Coordinate, Char>, MutableList<Number>> {
        val symbols = mutableMapOf<Coordinate, Char>();
        val numbers = mutableListOf<Number>()

        var charIndexNumberStart = -1
        var currentNumberAsString = ""
        for ((lineNumber, line) in input.withIndex()) {
            for ((charIndex, char) in line.withIndex()) {
                val lastInLine = charIndex == line.length - 1

                fun addCurrentCollectedNumberToList(xOffset: Int = 0) {
                    numbers.add(
                        Number(
                            Coordinate(charIndexNumberStart, lineNumber),
                            Coordinate(charIndex + xOffset, lineNumber),
                            currentNumberAsString.toInt()
                        )
                    )
                    charIndexNumberStart = -1
                    currentNumberAsString = ""
                }

                if (char.isDigit()) {
                    if (charIndexNumberStart == -1) {
                        charIndexNumberStart = charIndex
                    }

                    currentNumberAsString += char

                    if (lastInLine) {
                        addCurrentCollectedNumberToList()
                    }
                    continue
                }

                if (currentNumberAsString != "") {
                    addCurrentCollectedNumberToList(-1)
                }

                if (!char.isDigit() && char != '.') {
                    symbols[Coordinate(charIndex, lineNumber)] = char
                    continue
                }
            }
        }
        return Pair(symbols, numbers)
    }


    fun checkIfAdjacentToSymbol(number: Number, symbols: MutableMap<Coordinate, Char>): Boolean {
        for (charIndex in number.coordinateStart.x - 1..number.coordinateEnd.x + 1) {
            if (symbols.contains(Coordinate(charIndex, number.coordinateStart.y - 1)) ||
                symbols.contains(Coordinate(charIndex, number.coordinateStart.y)) ||
                symbols.contains(Coordinate(charIndex, number.coordinateStart.y + 1))
            ) {
                return true
            }
        }
        return false
    }

    fun part1(input: List<String>): Int {
        val (symbols, numbers) = parse(input)
        return numbers
            .filter { number -> checkIfAdjacentToSymbol(number, symbols) }
            .sumOf { it.value }
    }

    fun part2(input: List<String>): Int {
        fun Number.getRange() = this.coordinateStart.x..this.coordinateEnd.x
        val (symbols, numbers) = parse(input)

        val numbersPerLine = numbers.groupBy { it.coordinateStart.y }

        return symbols.asSequence()
            .filter { it.value == '*' }
            .map { symbol ->
                val horizontalRange = symbol.key.x - 1..symbol.key.x + 1
                val flatMap = (symbol.key.y - 1..symbol.key.y + 1) // line above, symbol line and below
                    .flatMap { yValue ->
                        // Get the overlapping numbers
                        numbersPerLine[yValue]
                            ?.filter { it.getRange().overlapsWith(horizontalRange) }
                            .orEmpty()
                    }
                flatMap
            }
            .filter { it.size == 2 }
            .map { it[0].value * it[1].value }
            .sum()
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")

    check(part1(testInput).peek() == 4361)

    val input = readInput("Day03")
    part1(input).println()

    check(part2(testInput).peek() == 467835)

    part2(input).println()
}
