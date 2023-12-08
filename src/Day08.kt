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
        val startingNodes = desert.maps.filter { it.source.endsWith("A") }.map { it.source }.toMutableSet()
        var steps = 0L
        val directionGenerator = desert.generateDirections()

        while (!startingNodes.all { it.endsWith("Z") }) {
            val nextDirection = directionGenerator.next()
            val nextNodes = mutableSetOf<String>()

            for (node in startingNodes) {
                val nextNode = desert.index[node]!!
                nextNodes.add(if (nextDirection == DesertDirection.L) nextNode.left else nextNode.right)
            }

            startingNodes.clear()
            startingNodes.addAll(nextNodes)
            steps++
        }

        return steps
    }

    val input2 = readInput("Day08")
    val input1 = readInput("Day08")

    println(part1(parseInput(input1)))
    println(part2(parseInput(input2)))
}
