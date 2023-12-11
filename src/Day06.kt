fun main() {
    val spaceChunkRegex = Regex("\\s+")

    data class Race(val durationInMilliseconds: Long, val recordInMillimeters: Long)

    fun parse1(input: List<String>): List<Race> {
        val (durations, distances) = input.map {
            it.split(':')[1]
                .trim()
                .replace(spaceChunkRegex, " ")
                .split(' ')
        }
        return durations.mapIndexed { index, duration ->
            val distance = distances[index]
            Race(duration.toLong(), distance.toLong())
        }
    }

    fun parse2(input: List<String>): Race {
        val (duration, distance) = input.map {
            it.split(':')[1]
                .trim()
                .replace(spaceChunkRegex, "")
                .toLong()
        }
        return Race(duration, distance)
    }


    // totalTime = race.durationInMilliseconds
    // windupTime = the time taken (ms) to increase the boat speed (1mm/ms)
    // (speed == windupTime)
    // travelTime = (totalTime - windupTime)    (the amount of time left in the race to travel
    // distance = travelTime * windupTime
    // windupTime = distance / travelTime

    fun debugRaces(races: List<Race>) {
        races.forEach { race ->
            println(race)
            for (windupTime in 0..(race.durationInMilliseconds)) {
                val travelTime = race.durationInMilliseconds - windupTime
                val distance = travelTime * windupTime
                println("windupTime $windupTime * travelTime $travelTime = distance $distance")
            }
            println()
        }
    }

    fun calculateScore(duration: Long, windupTime: Long) = windupTime * (duration - windupTime)

    fun calculateRace(race: Race): Long {
        // Since our formula results in a parabola, we can visit the first half. We can also skip 0, since x * 0 = 0
        val halfwayPoint = race.durationInMilliseconds / 2L
        return (1..halfwayPoint).asSequence()
            .map { windUpTime -> windUpTime * (race.durationInMilliseconds - windUpTime) }
            .filter { it > race.recordInMillimeters }
            .count().let {
                // Since we only visit the first half, we need to multiply by 2
                // If we have an odd race.durationInMilliseconds we do not have a clean cut half way point.
                // To get the correct value, remove 1 at the end.
                (if (race.durationInMilliseconds.isOdd()) it * 2L else it * 2L - 1L)
            }.apply { println("$race: $this") }
    }

    fun part1(input: List<String>): Long {
        val races = parse1(input)
        debug { debugRaces(races) }
        return races.map { race ->
            calculateRace(race)
        }.reduce { acc, i -> acc * i }
    }

    fun part2(input: List<String>): Long {
        // Took way less time to brute force than to relearn secondary school maths... oh well.
        return calculateRace(parse2(input));
    }


    // test if implementation meets criteria from the description, like:
    println("Part 1")
    val testInput = readInput("Day06_test")

    check(part1(testInput).peek() == 288L)

    val input = readInput("Day06")
    part1(input).println()

    println("\nPart 2")

    check(part2(testInput).peek() == 71503L)

    part2(input).println()
}
