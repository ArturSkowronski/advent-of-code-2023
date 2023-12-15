data class Platform(val rows: List<String>) {

    fun load(): Int {
        return rows.sumOf { row ->
            row.reversed().withIndex().sumOf { (i, c) -> if (c == 'O') i + 1 else 0 }
        }
    }

    fun tilt(): Platform {
        val tilted = rows.map { row ->
            row.split("#").joinToString("#") { group ->
                "O".repeat(group.count { it == 'O' }) + ".".repeat(group.count { it == '.' })
            }
        }
        return Platform(tilted)
    }

    fun rotate(): Platform {
        val rotated = mutableListOf<String>()
        for (i in rows[0].indices) {
            var newRow = ""
            for (line in rows.asReversed()) {
                newRow += line[i]
            }
            rotated.add(newRow)
        }
        return Platform(rotated)
    }
}

fun main() {
    fun parseInput(lines: List<String>): Platform {
        val platform = mutableListOf<String>()
        if (lines.isNotEmpty()) {
            val rowLength = lines[0].length
            for (i in 0 until rowLength) {
                var column = ""
                for (line in lines) {
                    if (i < line.length) {
                        column += line[i]
                    }
                }
                platform.add(column)
            }
        }
        return Platform(platform)
    }

    fun part1(platform: Platform): Int {
        return platform.tilt().load()
    }

    fun part2(platform: Platform): Int {
        val target = 1000000000
        val seenStates = mutableMapOf<Platform, Int>()
        var currentPlatform = platform
        var cycles = 0

        while (!seenStates.containsKey(currentPlatform)) {
            seenStates[currentPlatform] = cycles
            currentPlatform = currentPlatform.rotate().tilt()
            cycles++
        }

        val cycleStart = seenStates[currentPlatform]!!
        val cycleLength = cycles - cycleStart
        val remainingCycles = (target - cycleStart) % cycleLength

        for (i in 0 until remainingCycles) {
            currentPlatform = currentPlatform.rotate().tilt()
        }

        return currentPlatform.load()
    }


    val lines = readInput("Day14")
    val platform = parseInput(lines)

    println(part1(platform))
    println(part2(platform))
}