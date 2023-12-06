import kotlin.math.max
import kotlin.math.min

fun main() {

    data class MapRecord(val destinationRangeStart: Long, val sourceRangeStart: Long, val rangeLength: Long) {
        fun getSourceRange() = sourceRangeStart until (sourceRangeStart + rangeLength)
        fun getDestinationRange() = destinationRangeStart until (destinationRangeStart + rangeLength)
        fun mapToDestinationRange(range: LongRange) =
            (range.start - sourceRangeStart + destinationRangeStart)..(range.endInclusive - sourceRangeStart + destinationRangeStart)
    }

    data class SourceToDestinationMap(val name: String, val records: List<MapRecord>)
    data class Scenario(val seedsToPlant: List<Long>, val sourceToDestinationMaps: List<SourceToDestinationMap>)

    fun parse(input: List<String>): Scenario {
        val seedsToPlant = input[0].removePrefix("seeds: ")
            .split(' ')
            .map { it.toLong() }
            .toList()

        val sourceToDestinationMaps = mutableListOf<SourceToDestinationMap>()

        var currentName = ""
        val currentMapRecords = mutableListOf<MapRecord>()

        fun pushNewValue() {
            sourceToDestinationMaps.add(SourceToDestinationMap(currentName, currentMapRecords.toList()))
            currentName = ""
            currentMapRecords.clear()
        }

        input.subList(2, input.size).forEach { line ->
            if (line.isBlank()) {
                pushNewValue()
            } else if (line.first().isDigit()) {
                line.split(' ')
                    .map { it.toLong() }
                    .let { splitValues ->
                        currentMapRecords.add(MapRecord(splitValues[0], splitValues[1], splitValues[2]))
                    }
            } else {
                currentName = line.removeSuffix(" map:")
            }
        }
        if (currentName.isNotBlank())
            pushNewValue()

        return Scenario(seedsToPlant, sourceToDestinationMaps)
    }

    fun SourceToDestinationMap.getDestinationForSource(source: Long): Long {
        return records.firstOrNull { source in (it.sourceRangeStart..it.sourceRangeStart + it.rangeLength) }
            ?.let { source - it.sourceRangeStart + it.destinationRangeStart }
            ?: source
    }

    fun part1(input: List<String>): Long {
        val scenario = parse(input)
        return scenario.seedsToPlant.minOf { seedToPlant ->
            var current = seedToPlant
            scenario.sourceToDestinationMaps
                .forEach { sourceToDestinationMap ->
                    debug { print(sourceToDestinationMap.name.split('-')[0] + " $current, ") }
                    current = sourceToDestinationMap.getDestinationForSource(current)
                }
            debug { print("location $current\n") }
            current
        }
    }

    fun transformRanges(input: LongRange, sourceToDestinationMap: SourceToDestinationMap): List<LongRange> {
        val ranges = mutableListOf<LongRange>()
        val rangesToCheck = ArrayDeque<LongRange>()

        rangesToCheck.add(input)

        while (rangesToCheck.isNotEmpty()) {
            val current = rangesToCheck.removeFirst()
            var hasNoIntersections = true
            sourceToDestinationMap.records.forEach { mapRecord ->

                val intersection = intersectRanges(current, mapRecord.getSourceRange())
                if (intersection == LongRange.EMPTY) {
                    return@forEach
                }
                ranges.add(mapRecord.mapToDestinationRange(intersection))
                hasNoIntersections = false

                // We might have a range that is cut off at the front and/or at the end.
                if (current.first < mapRecord.getSourceRange().first) {
                    rangesToCheck.add(current.first..min(current.last, mapRecord.getSourceRange().first - 1))
                }
                if (current.last > mapRecord.getSourceRange().last) {
                    rangesToCheck.add(max(current.first, mapRecord.getSourceRange().last + 1)..current.last)
                }
            }
            if (hasNoIntersections) {
                ranges.add(current)
            }
        }

        return ranges
    }

    fun part2(input: List<String>): Long {
        val scenario = parse(input)//.copy(seedsToPlant = listOf(82, 1))
        val seedRanges = scenario.seedsToPlant.chunked(2).map { it[0] until (it[0] + it[1]) }

        return seedRanges
            .flatMap { seedRange ->
                var current = listOf(seedRange)
                val newValues = mutableSetOf<LongRange>()
                scenario.sourceToDestinationMaps.forEach { sourceToDestinationMap ->
                    newValues += current.flatMap { transformRanges(it, sourceToDestinationMap) }

                    debug {
                        println(
                            "${sourceToDestinationMap.name.split('-')[0]} $current turns into ${
                                sourceToDestinationMap.name.split(
                                    '-'
                                )[2]
                            } $newValues"
                        )
                    }
                    current = squashRanges(newValues.toList())
                    newValues.clear()
                }
                debug { println("Final locations are $current\n") }

                current
            }
            .minOf { it.first }

    }


    // test if implementation meets criteria from the description, like:
    println("Part 1")
    val testInput = readInput("Day05_test")

    check(part1(testInput).peek() == 35L)

    val input = readInput("Day05")
    part1(input).println()

    println("\nPart 2")

    check(part2(testInput).peek() == 46L)

    part2(input).println()
}
