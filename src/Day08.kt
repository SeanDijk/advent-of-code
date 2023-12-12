fun main() {
    data class NodeData(val name: String, val left: String, val right: String) {
        val endsWithA: Boolean by lazy { name.endsWith('A') }
        val endsWithZ: Boolean by lazy { name.endsWith('Z') }
        override fun toString() = name
    }

    data class Game(val instructions: String, val nodes: Map<String, NodeData>)

    fun parse(input: List<String>): Game {
        return input.subList(2, input.size).asSequence()
            .map { NodeData(it.substring(0, 3), it.substring(7, 10), it.substring(12, 15)) }
            .associateBy { it.name }
            .let { Game(input.first(), it) }
    }

    fun part1(input: List<String>): Long {
        val (instructions, nodes) = parse(input)

        val infiniteSequence: Sequence<Char> = generateSequence { instructions.asSequence() }.flatMap { it }

        var counter = 0L
        var currentNode = nodes["AAA"]!!
        val endNode = nodes["ZZZ"]!!

        infiniteSequence.takeWhile { currentNode != endNode }.forEach { instruction ->
            counter++
            currentNode = when (instruction) {
                'L' -> nodes[currentNode.left]!!
                'R' -> nodes[currentNode.right]!!
                else -> throw IllegalStateException()
            }
        }
        return counter
    }

    data class SequenceFormula(val aToZ: Long, val zToZ: Long)

    fun getSequenceFormulas(input: List<String>): List<SequenceFormula> {
        val (instructions, nodes) = parse(input)
        val startNodes = nodes
            .map { it.value }
            .filter { it.endsWithA }

        // Every A-Node has 1 Corresponding Z-Node.
        // The Z-Node can always loop to itself.
        // This means we can create a formula like: x = steps from A to Z + (steps from Z to Z * y)
        // This formula can be used to generate a sequence.
        // We can intersect all these sequences to get the lowest result

        val formulas = startNodes.map { startNode ->
            var currentNode = startNode
            val values = mutableListOf<Long>()

            generateSequence { instructions.asSequence() }.flatMap { it }.withIndex()
                .takeWhile { values.size < 2 }.forEach { (index, instruction) ->
                    currentNode = when (instruction) {
                        'L' -> nodes[currentNode.left]!!
                        'R' -> nodes[currentNode.right]!!
                        else -> throw IllegalStateException()
                    }
                    if (currentNode.endsWithZ) {
                        values.add(index.toLong() + 1) // we have done 1 more instruction than our index is at
                    }
                }
            SequenceFormula(values[0], values[1] - values[0])
        }
        return formulas
    }

    fun part2(input: List<String>): Long {
        val formulas = getSequenceFormulas(input)

        var counter = 0
        val largestFormula = formulas.maxBy { it.aToZ } // apparently aToZ and zToA are always the same
        val orderedByLargest = formulas.sortedBy { it.aToZ }.reversed().subList(1, formulas.size)
        while (true) {
            val biggest = largestFormula.let { it.aToZ + it.zToZ * counter }

            val solved = orderedByLargest.asSequence()
                .map { (biggest) % it.zToZ }
                .all { it == 0L }
            if (solved) {
                return biggest
            }
            counter++
        }
    }

    fun part2Performant(input: List<String>): Long {
        val formulas = getSequenceFormulas(input)

        /**
         * Computing Greatest Common Divisor (GCD). This is used when computing LCM.
         */
        fun gcd(n1: Long, n2: Long): Long = if (n2 == 0L) n1 else gcd(n2, n1 % n2)

        /**
         * Computing Lowest Common Multiple (LCM).
         */
        fun lcm(n1: Long, n2: Long): Long = n1 * (n2 / gcd(n1, n2))

        var counter = 1
        val largestFormula = formulas.maxBy { it.aToZ }
        val step = formulas.map { it.zToZ }.reduce(::lcm)

        while (true) {
            val biggest = step * counter
            val solved = formulas.asSequence()
                .map { biggest % it.zToZ }
                .all { it == 0L }

            if (solved) return biggest
            counter++
        }
    }


    // test if implementation meets criteria from the description, like:
    println("Part 1")
    val testInput = readInput("Day08_test")

    check(part1(testInput).peek() == 6L)

    val input = readInput("Day08")
    part1(input).println()

    println("\nPart 2")
    val testInput2 = readInput("Day08_test_part_2")

    check(part2Performant(testInput2).peek() == 6L)
    check(part2(testInput2).peek() == 6L)

    part2Performant(input).println()
    part2(input).println()
}
