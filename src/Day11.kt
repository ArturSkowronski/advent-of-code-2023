import kotlin.math.abs

fun main() {

    data class Point(val x: Int, val y: Int)

    data class Map(val size: Int, val galaxies: List<Point>)

    fun parseInput(input: List<String>): Map {
        val galaxies = mutableListOf<Point>()

        input.forEachIndexed { y, row ->
            row.forEachIndexed { x, column ->
                if (column == '#') {
                    galaxies.add(Point(x, y))
                }
            }
        }
        return Map(input.first().length, galaxies)
    }

    fun part1(map: Map, distanceOfGalaxy: Int = 1): Long {
        val (size, galaxies) = map

        val emptyRows = (0 until size) - galaxies.map { it.x }.toSet()
        val emptyCols = (0 until size) - galaxies.map { it.y }.toSet()

        var sum = 0L

        galaxies.forEachIndexed { i, (y1, x1) ->
            for ((y2, x2) in galaxies.subList(i + 1, galaxies.size)) {
                val distance = abs(y2 - y1) + abs(x2 - x1)
                sum += distance
                emptyRows.forEach { ey ->
                    if (ey in minOf(y1, y2)..maxOf(y1, y2)) {
                        sum += distanceOfGalaxy
                    }
                }
                emptyCols.forEach { ex ->
                    if (ex in minOf(x1, x2)..maxOf(x1, x2)) {
                        sum += distanceOfGalaxy
                    }
                }
            }
        }
        return sum
    }

    val input = readInput("Day11")
    val map = parseInput(input)

    println(part1(map))
    println(part1(map, 999999))
}



