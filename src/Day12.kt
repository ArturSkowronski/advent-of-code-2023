fun main() {

    data class BrokenSpring(val rawPattern: String, val pattern: List<String>, val repairInstruction: List<Int>) {
        private fun generateCombinations(pattern: String): List<String> {
            return recur(pattern, 0, listOf())
        }

        private fun recur(pattern: String, index: Int, results: List<String>): List<String> {
            if (index == pattern.length) {
                return results + pattern
            }

            return if (pattern[index] == '?') {
                recur(pattern.replaceRange(index, index + 1, "."), index + 1, results) +
                recur(pattern.replaceRange(index, index + 1, "#"), index + 1, results)
            } else {
                recur(pattern, index + 1, results)
            }
        }

        fun isValid(combination: String): Boolean {
            var currentIndex = 0
            var instructionIndex = 0

            while (currentIndex < combination.length && instructionIndex < repairInstruction.size) {
                val currentGroupSize = repairInstruction[instructionIndex]
                var count = 0

                while (currentIndex < combination.length && combination[currentIndex] == '.') {
                    currentIndex++
                }

                while (currentIndex < combination.length && combination[currentIndex] == '#') {
                    count++
                    currentIndex++
                }

                if (count != currentGroupSize) return false

                instructionIndex++
            }

            while (currentIndex < combination.length) {
                if (combination[currentIndex] == '#') return false
                currentIndex++
            }

            return instructionIndex == repairInstruction.size
        }

        fun getValidCombinations(): List<String> {
            return generateCombinations(rawPattern).filter { isValid(it) }
        }
    }

    fun takeConsecutiveChars(input: String): List<String> {
        return input.fold(listOf()) { acc, char ->
            if (acc.isEmpty() || acc.last().last() != char)
                acc + char.toString()
            else
                acc.dropLast(1) + (acc.last() + char)
        }
    }

    fun parseInput(input: List<String>): List<BrokenSpring> {
        return input.map {
            val (springs, instruction) = it.split(" ")
            val takeConsecutiveChars = takeConsecutiveChars(springs)
            val repairInstruction = instruction.split(",").map { it.toInt() }
            BrokenSpring(springs, takeConsecutiveChars, repairInstruction)
        }
    }


    fun part1(brokenSprings: List<BrokenSpring>): Int {
        return brokenSprings.map { it.getValidCombinations() }.map {
            it.size
        }.sumOf { it }
    }

    fun part2(brokenSprings: List<BrokenSpring>): Long {
        return 0L
    }

    val input = readInput("Day12")
    val brokenSprings = parseInput(input)

    println(part1(brokenSprings))
}



