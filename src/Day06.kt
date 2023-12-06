fun main() {
    fun parseInput(input: List<String>): List<Pair<Int, Long>> {
        val times = input[0].split(":")[1].trim().split(" ")
                .filter { it.isNotEmpty() }
                .map { it.toInt() }
        val distances = input[1].split(":")[1].trim().split(" ")
                .filter { it.isNotEmpty() }
                .map { it.toLong() }

        return times.zip(distances)
    }

    fun calculateWaysToBeatRecord(time: Int, record: Long): Int {
        var waysToWin = 0
        for (buttonHoldTime in 0 until time) {
            val distance = buttonHoldTime.toLong() * (time - buttonHoldTime)
            if (distance > record) {
                waysToWin++
            }
        }
        return waysToWin
    }

    fun part1(times: List<Pair<Int, Long>>): Long {
        return times.map {
            calculateWaysToBeatRecord(it.first, it.second)
        }.reduce { acc, value -> acc * value }.toLong()
    }

    fun part2(time: Int, record: Long): Int {
        return calculateWaysToBeatRecord(time, record)
    }

    val input = readInput("Day06")
    val timesAndDistances = parseInput(input)


    val combinedTime = timesAndDistances.joinToString("") { it.first.toString() }.toInt()
    val combinedDistance = timesAndDistances.joinToString("") { it.second.toString() }.toLong()
    println(part1(timesAndDistances))
    println(part2(combinedTime, combinedDistance))
}