fun main() {

    fun parseInput(lines: List<String>): List<List<Int>> {
        return lines.map { line ->
            line.split(" ").map { it.toInt() }
        }.toList()
    }

    fun iterateOverArray(array: MutableList<Int>): List<Int> {
        val newArray = mutableListOf<Int>()
        for (i in 0 until array.size - 1) {
            newArray.add(array[i + 1] - array[i])
        }
        if (newArray.toSet().size == 1) {
            array.add(array.last() + newArray.first())
            array.add(0, array.first() - newArray.first())
        } else {
            val diff = iterateOverArray(newArray)
            array.add(array.last() + diff.last())
            array.add(0, array.first() - diff.first())
        }
        return array
    }

    fun part1(input: List<List<Int>>): Int {
        val res = input.fold(Pair(mutableListOf<Int>(), mutableListOf<Int>())) { acc, element ->
            acc.second.add(iterateOverArray(element.toMutableList()).last())
            acc
        }

        return res.second.sum()
    }

    fun part2(input: List<List<Int>>): Int {
        val res = input.fold(Pair(mutableListOf<Int>(), mutableListOf<Int>())) { acc, element ->
            acc.first.add(iterateOverArray(element.toMutableList()).first())
            acc
        }

        return res.first.sum()
    }

    val fileInput = readInput("Day09")
    println(part1(parseInput(fileInput)))
    println(part2(parseInput(fileInput)))
}