fun main() {
    fun parseInput(lines: List<String>): List<String> {
        return lines.first().split(',')
    }

    fun hash(input: List<String>): Int {
        var total = 0
        for (step in input) {
            var lineHash = 0
            for (ch in step) {
                lineHash = (lineHash + ch.code) * 17
                lineHash %= 256
            }
            total += lineHash
        }
        return total
    }

    fun part1(input: List<String>): Int {
        return hash(input)
    }

    fun part2(input: List<String>): Int {
        val boxes = Array(256) { mutableListOf<String>() }
        val boxesValues = mutableMapOf<String, Int>()

        input.forEach { step ->
            when {
                '=' in step -> {
                    val (pos, lens) = step.split('=')
                    val index = hash(listOf(pos))
                    boxes[index].takeIf { !it.contains(pos) }?.add(pos)
                    boxesValues[pos] = lens.toInt()
                }
                '-' in step -> {
                    val pos = step.dropLast(1)
                    val index = hash(listOf(pos))
                    boxes[index].remove(pos)
                    boxesValues.remove(pos)
                }
            }
        }

        return boxes.withIndex().sumOf { (i, box) ->
            box.withIndex().sumOf { (j, pos) ->
                (i + 1) * (j + 1) * (boxesValues[pos] ?: 0)
            }
        }
    }

    val fileInput = readInput("Day15")
    val parsedInput = parseInput(fileInput)

    println(part1(parsedInput))
    println(part2(parsedInput))
}
