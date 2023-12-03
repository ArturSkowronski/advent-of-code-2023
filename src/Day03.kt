fun main() {
    data class Element(val value: String, val type: String, val positions: List<Pair<Int, Int>>) {

        fun closeToSymbol(elements: Map<Pair<Int, Int>, Element>): Boolean {
            return positions.any {
                (elements[Pair(it.first - 1, it.second)]?.type == "G" || elements[Pair(it.first - 1, it.second)]?.type == "S") ||
                (elements[Pair(it.first + 1, it.second)]?.type == "G" || elements[Pair(it.first + 1, it.second)]?.type == "S") ||
                (elements[Pair(it.first, it.second - 1)]?.type == "G" || elements[Pair(it.first, it.second - 1)]?.type == "S") ||
                (elements[Pair(it.first, it.second + 1)]?.type == "G" || elements[Pair(it.first, it.second + 1)]?.type == "S") ||
                (elements[Pair(it.first + 1, it.second + 1)]?.type == "G" || elements[Pair(it.first + 1, it.second + 1)]?.type == "S") ||
                (elements[Pair(it.first + 1, it.second - 1)]?.type == "G" || elements[Pair(it.first + 1, it.second - 1)]?.type == "S") ||
                (elements[Pair(it.first - 1, it.second + 1)]?.type == "G" || elements[Pair(it.first - 1, it.second + 1)]?.type == "S") ||
                (elements[Pair(it.first - 1, it.second - 1)]?.type == "G" || elements[Pair(it.first - 1, it.second - 1)]?.type == "S")
            }
        }

        fun partNumbersInNeighbourhood(elements: Map<Pair<Int, Int>, Element>): MutableSet<Element?> {
            val it = positions.first()
            val nb = mutableSetOf<Element?>()
            if (elements[Pair(it.first - 1, it.second)]?.type == "N") nb.add(elements[Pair(it.first - 1, it.second)])
            if (elements[Pair(it.first + 1, it.second)]?.type == "N") nb.add(elements[Pair(it.first + 1, it.second)])
            if (elements[Pair(it.first, it.second - 1)]?.type == "N") nb.add(elements[Pair(it.first, it.second - 1)])
            if (elements[Pair(it.first, it.second + 1)]?.type == "N") nb.add(elements[Pair(it.first, it.second + 1)])
            if (elements[Pair(it.first + 1, it.second + 1)]?.type == "N") nb.add(elements[Pair(it.first + 1, it.second + 1)])
            if (elements[Pair(it.first + 1, it.second - 1)]?.type == "N") nb.add(elements[Pair(it.first + 1, it.second - 1)])
            if (elements[Pair(it.first - 1, it.second + 1)]?.type == "N") nb.add(elements[Pair(it.first - 1, it.second + 1)])
            if (elements[Pair(it.first - 1, it.second - 1)]?.type == "N") nb.add(elements[Pair(it.first - 1, it.second - 1)])
            return nb
        }

    }

    data class Schematics(val listOfElements: List<Element>) {
        fun reverseIndex(): Map<Pair<Int, Int>, Element> {
            return listOfElements.flatMap { element -> element.positions.map { it to element } }.toMap()
        }
    }

    fun parseLine(inputString: String, lineNumber: Int): List<Element> {
        val result = mutableListOf<Element>()
        var currentSegment = ""
        var positions = mutableListOf<Pair<Int, Int>>()

        inputString.forEachIndexed { index, char ->
            if (char.isDigit()) {
                currentSegment += char
                positions.add(Pair(index, lineNumber))
            } else {
                if (currentSegment.isNotEmpty()) {
                    result.add(Element(currentSegment, "N", positions))
                    currentSegment = ""
                    positions = mutableListOf()
                }
                if (char != '.') {
                    result.add(Element(char.toString(), if (char == '*') "G" else "S", listOf(Pair(index, lineNumber))))
                }

            }
        }

        if (currentSegment.isNotEmpty()) {
            result.add(Element(currentSegment, "N", positions))
        }

        return result
    }

    fun parseInput(input: List<String>): Schematics {
        val rawLines = input.flatMapIndexed() { y, line -> parseLine(line, y) }
        return Schematics(rawLines)
    }

    fun part1(schematics: Schematics): Int {
        val reverseIndex = schematics.reverseIndex()
        val validElements = schematics.listOfElements.filter { it.type == "N" }.filter { it.closeToSymbol(reverseIndex) }
        return validElements.sumOf { it.value.toInt() }
    }

    fun part2(schematics: Schematics): Int {
        val reverseIndex = schematics.reverseIndex()
        val validElements = schematics.listOfElements.filter { it.type == "G" }
        val el = validElements.map { it to it.partNumbersInNeighbourhood(reverseIndex) }.filter { it.second.size > 1 }
        return el.sumOf {
            it.second.map { it!!.value.toInt() }[0] * it.second.map { it!!.value.toInt() }[1]
        };
    }

    val input = readInput("Day03")
    val schematics = parseInput(input)

    part1(schematics).println()
    part2(schematics).println()
}