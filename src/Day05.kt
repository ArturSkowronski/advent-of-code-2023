import java.math.BigDecimal

fun main() {
    data class Range(val destinationRangeStart: Double, val sourceRangeStart: Double, val rangeLength: Double) {
        fun findDestination(source: Double): Double? {
            return if (sourceInRange(source)) {
                source - (sourceRangeStart - destinationRangeStart)
            } else null
        }

        fun sourceInRange(source: Double): Boolean {
            return (source >= sourceRangeStart && source < sourceRangeStart + rangeLength)
        }
    }
    data class Almanac(
            val seeds: List<Double>,
            val seedsToSoil_1: List<Range>,
            val soilToFertilizer_2: List<Range>,
            val fertilizerToWater_3: List<Range>,
            val waterToLight_4: List<Range>,
            val lightToTemperature_5: List<Range>,
            val temperatureToHumidity_6: List<Range>,
            val humidityToLocation_7: List<Range>,
    )

    fun parseTransition(iterator: Iterator<String>): List<Range> {
        val ranges = mutableListOf<Range>()
        iterator.next()
        while (iterator.hasNext()) {
            val line = iterator.next()
            if (line.isBlank()) break
            val (destination, start, length) = line.split(" ").map { it.toDouble() }
            ranges.add(Range(destination, start, length))
        }
        return ranges
    }

    fun parseInput(input: List<String>): Almanac {
        val iterator = input.iterator()

        val seeds = iterator.next().removePrefix("seeds:").trim().split(" ").map { it.toDouble() }

        iterator.next()

        val seedsToSoil_1 = parseTransition(iterator)
        val soilToFertilizer_2 = parseTransition(iterator)
        val fertilizerToWater_3 = parseTransition(iterator)
        val waterToLight_4 = parseTransition(iterator)
        val lightToTemperature_5 = parseTransition(iterator)
        val temperatureToHumidity_6 = parseTransition(iterator)
        val humidityToLocation_7 = parseTransition(iterator)

        return Almanac(
                seeds,
                seedsToSoil_1,
                soilToFertilizer_2,
                fertilizerToWater_3,
                waterToLight_4,
                lightToTemperature_5,
                temperatureToHumidity_6,
                humidityToLocation_7
        )
    }

    fun seedsToLocation(seeds: Sequence<Double>, almanac: Almanac): Double? {
        fun stateTransitions(sources: Sequence<Double>, ranges: List<Range>): Sequence<Double> {
            return sources.mapNotNull { source ->
                ranges.firstOrNull { range -> range.sourceInRange(source) }
                        ?.findDestination(source)
            }
        }

        val soils = stateTransitions(seeds, almanac.seedsToSoil_1)
        val fertilizers = stateTransitions(soils, almanac.soilToFertilizer_2)
        val waters = stateTransitions(fertilizers, almanac.fertilizerToWater_3)
        val lights = stateTransitions(waters, almanac.waterToLight_4)
        val temperatures = stateTransitions(lights, almanac.lightToTemperature_5)
        val humidities = stateTransitions(temperatures, almanac.temperatureToHumidity_6)
        val locations = stateTransitions(humidities, almanac.humidityToLocation_7)

        return locations.minOrNull()
    }

    fun generateLazyRangeList(start: Double, end: Double): Sequence<Double> {
        return sequence {
            var current = start
            while (current <= end) {
                yield(current)
                current += 1
            }
        }
    }

    fun part2(almanac: Almanac): String {
        val seedsSequence = sequence {
            yieldAll(generateLazyRangeList(almanac.seeds[0], almanac.seeds[0] + almanac.seeds[1]))
            yieldAll(generateLazyRangeList(almanac.seeds[2], almanac.seeds[2] + almanac.seeds[3]))
        }

        val smallestLocation =  seedsToLocation(seedsSequence, almanac)
        return BigDecimal(smallestLocation ?: 0.0).toPlainString()

    }

    val input = readInput("Day05")
    val almanac = parseInput(input)

    println(part2(almanac))
}