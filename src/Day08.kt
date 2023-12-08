import java.util.*

enum class DesertDirection {
    L, R
}

fun main() {

    data class DesertNode(val source: String, val left: String, val right: String)

    data class Desert(val directions: List<DesertDirection>, val maps: List<DesertNode>) {
        val index = maps.associateBy { it.source }

        fun generateDirections(): Iterator<DesertDirection> {
            return directionsGenerator(directions).iterator()
        }

        private fun directionsGenerator(directions: List<DesertDirection>): Sequence<DesertDirection> {
            return sequence {
                while (true) {
                    for (direction in directions) {
                        yield(direction)
                    }
                }
            }
        }
    }


    fun parseInput(input: List<String>): Desert {
        val steps = input.first().split("").filter { it.isNotBlank() }.map {
            DesertDirection.valueOf(it)
        }
        val directions = input
            .drop(2)
            .map {
                val (node, LRDirections) = it.split(" = ")
                val (LDir, RDir) = LRDirections.replace("(", "").replace(")", "").split(", ")
                DesertNode(node, LDir, RDir)
            }
        return Desert(steps, directions)
    }



    fun part1(desert: Desert): Long {
        var currentNode = "AAA"
        var steps = 0L
        val directionGenerator = desert.generateDirections()

        while (currentNode != "ZZZ") {
            val direction = directionGenerator.next()
            val node = desert.index[currentNode]!!

            currentNode = if (direction == DesertDirection.L) node.left else node.right
            steps++
        }

        return steps
    }

    fun part2(desert: Desert): Long {
        val startingNodes = desert.maps.filter { it.source.endsWith("A") }.map { it.source }
        val queue: Queue<List<String>> = LinkedList()
        queue.add(startingNodes)

        var steps = 0L
        val directionGenerator = desert.generateDirections()

        while (queue.isNotEmpty()) {
            val size = queue.size
            for (i in 0 until size) {
                val currentPath = queue.poll()
                if (currentPath.all { it.endsWith("Z") }) {
                    return steps
                }

                val nextDirection = directionGenerator.next()
                val nextPath = currentPath.map { node ->
                    val nextNode = desert.index[node]!!
                    if (nextDirection == DesertDirection.L) nextNode.left else nextNode.right
                }

                queue.add(nextPath)
            }
            steps++
        }

        return steps
    }


    val input2 = readInput("Day08")
    val input1 = readInput("Day08")

    println(part1(parseInput(input1)))
    println(part2(parseInput(input2)))
}
