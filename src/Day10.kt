enum class PipeKind {
    VERTICAL, HORIZONTAL, NE, NW, SW, SE, EMPTY, START
}

fun main() {
    data class Point(val x: Int, val y: Int)

    data class MazePipe(val position: Point, val pipeKind: PipeKind)

    data class Maze(val pipes: List<MazePipe>) {
        val reverseIndex: Map<Point, MazePipe> = pipes.associateBy { it.position }
    }

    fun parseInput(lines: List<String>): Maze {
        val pipes = mutableListOf<MazePipe>()

        for (y in lines.indices) {
            for (x in lines[y].indices) {
                val char = lines[y][x]
                val pipeKind = when (char) {
                    '|' -> PipeKind.VERTICAL
                    '-' -> PipeKind.HORIZONTAL
                    'L' -> PipeKind.NE
                    'J' -> PipeKind.NW
                    '7' -> PipeKind.SW
                    'F' -> PipeKind.SE
                    '.' -> PipeKind.EMPTY
                    'S' -> PipeKind.START
                    else -> null
                }
                pipes.add(MazePipe(Point(x, y), pipeKind!!))
            }
        }

        return Maze(pipes)
    }

    fun getNextPipe(currentPipe: MazePipe, maze: Maze, previousPipe: MazePipe? = null): MazePipe? {
        val (x, y) = currentPipe.position
        val possibleNextPositions = when (currentPipe.pipeKind) {
            PipeKind.VERTICAL -> listOf(Point(x, y - 1), Point(x, y + 1))
            PipeKind.HORIZONTAL -> listOf(Point(x - 1, y), Point(x + 1, y))
            PipeKind.NE -> listOf(Point(x, y - 1), Point(x + 1, y))
            PipeKind.NW -> listOf(Point(x, y - 1), Point(x - 1, y))
            PipeKind.SW -> listOf(Point(x, y + 1), Point(x - 1, y))
            PipeKind.SE -> listOf(Point(x, y + 1), Point(x + 1, y))
            PipeKind.START -> listOf(Point(x, y - 1), Point(x, y + 1), Point(x - 1, y), Point(x + 1, y))
            PipeKind.EMPTY -> return null
        }

        return possibleNextPositions.asSequence()
                .filter { it != previousPipe?.position }
                .mapNotNull { maze.reverseIndex[it] }
                .firstOrNull()
    }


    fun loopLength(current: MazePipe, startPipe: MazePipe, maze: Maze): Int {
        var length = 0
        var pipe = current
        var previousPipe: MazePipe? = null

        while (true) {
            length++
            val nextPipe = getNextPipe(pipe, maze, previousPipe)
            if (nextPipe == null || nextPipe == startPipe) break
            previousPipe = pipe
            pipe = nextPipe
        }

        return length
    }


    fun part1(maze: Maze): Int {
        val start = maze.pipes.find { it.pipeKind == PipeKind.START }!!

        val startPoints = listOf(
                Point(start.position.x, start.position.y - 1),
                Point(start.position.x, start.position.y + 1),
                Point(start.position.x - 1, start.position.y),
                Point(start.position.x + 1, start.position.y)
        )

        val loopLengths = startPoints.mapNotNull { point ->
            maze.reverseIndex[point]?.let { nextPipe ->
                if (nextPipe.pipeKind != PipeKind.EMPTY) {
                    loopLength(nextPipe, start, maze)
                } else {
                    null
                }
            }
        }

        return (loopLengths.max() + 1) / 2
    }

    val fileInput = readInput("Day10")

    println(part1(parseInput(fileInput)))
}