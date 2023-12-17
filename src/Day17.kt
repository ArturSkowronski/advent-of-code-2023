import java.util.*
import kotlin.collections.HashMap

data class Direction(val x: Int, val y: Int)

val EAST = Direction(1, 0)
val WEST = Direction(-1, 0)
val NORTH = Direction(0, -1)
val SOUTH = Direction(0, 1)
sealed class Position(
    val x: Int,
    val y: Int,
    val direction: Direction,
    val run: Int
) {
    open fun neighbors(width: Int, height: Int): Sequence<Position> = sequence {
        val excludedDirections = mutableListOf(Direction(-direction.x, -direction.y))
        if (run == 3) {
            excludedDirections.add(direction)
        }
        listOf(EAST, WEST, NORTH, SOUTH).filterNot { it in excludedDirections }.forEach {
            yieldAll(newPosition(it, width, height))
        }
    }

    fun newPosition(newDirection: Direction, width: Int, height: Int): Sequence<Position> = sequence {
        val newRun = if (newDirection == direction) run + 1 else 1
        val newX = x + newDirection.x
        val newY = y + newDirection.y
        if (newX in 0 until width && newY in 0 until height) {
            yield(RegularPosition(newX, newY, newDirection, newRun))
        }
    }

    open val minRun: Int
        get() = 0

    class RegularPosition(x: Int, y: Int, direction: Direction, run: Int) : Position(x, y, direction, run)

    class UltraCruciblesPositions(x: Int, y: Int, direction: Direction, run: Int) : Position(x, y, direction, run) {
        override fun neighbors(width: Int, height: Int): Sequence<Position> = sequence {
            val directions = listOf(EAST, WEST, NORTH, SOUTH)
            val excludedDirections = mutableListOf(Direction(-direction.x, -direction.y))
            if (run == 10) {
                excludedDirections.add(direction)
            }
            val allowedDirections = if (run in 1..3) listOf(direction) else directions
            allowedDirections.filterNot { it in excludedDirections }.forEach {
                yieldAll(newPosition(it, width, height))
            }
        }

        override val minRun: Int
            get() = 4
    }
}

fun main() {




    fun part1(heatLossMap: List<List<Int>>, start: Position): Int {
        val width = heatLossMap[0].size
        val height = heatLossMap.size
        val goal = Pair(width - 1, height - 1)
        val priorityQueue = PriorityQueue<Triple<Int, Position, List<Position>>>(
            compareBy { it.first }
        )
        priorityQueue.add(Triple(0, start, listOf()))
        val totalHeatLossMap = HashMap<Position, Int>().withDefault { Int.MAX_VALUE }

        while (priorityQueue.isNotEmpty()) {
            val (heatLoss, position, path) = priorityQueue.poll()
            if (Pair(position.x, position.y) == goal && position.run >= position.minRun) {
                return heatLoss
            }
            for (newPosition in position.neighbors(width, height)) {
                val newHeatLoss = heatLoss + heatLossMap[newPosition.y][newPosition.x]
                if (newHeatLoss < totalHeatLossMap.getValue(newPosition)) {
                    val newPath = path + position
                    totalHeatLossMap[newPosition] = newHeatLoss
                    priorityQueue.add(Triple(newHeatLoss, newPosition, newPath))
                }
            }
        }
        return Int.MAX_VALUE
    }

    fun parseInput(input: List<String>): List<List<Int>> {
        return input.map { it.map(Char::digitToInt) }
    }

    val input = readInput("Day17")
    val heatLossMap = parseInput(input)
    println(part1(heatLossMap, Position.RegularPosition(0, 0, EAST, 0)))
    println(part1(heatLossMap, Position.UltraCruciblesPositions(0, 0, EAST, 0)))
}