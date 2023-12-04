import kotlin.math.pow

fun main() {
    data class Scratchcard(val cardId: Int, val winningNumbers: Set<Int>, val scratchedNumbers: List<Int>) {
        fun calculatePointsPart1(): Double {
            val size = wins()
            if (size == 0) return 0.0
            return 2.0.pow(size.toDouble() - 1)
        }

        fun wins() = winningNumbers.intersect(scratchedNumbers).size
    }

    fun parseInput(input: List<String>): List<Scratchcard> {
        return input.map { scratchcard ->
            val (cardInfo, numbers) = scratchcard.split(":")
            val cardNumber = cardInfo.replace("Card ", "").trim().toInt()
            val (winningNumberString, ourNumberString) = numbers.split(" | ")
            val winningNumbers = winningNumberString.split(" ").filter { it.isNotBlank() }.map { it.toInt() }
            val ourNumbers = ourNumberString.split(" ").filter { it.isNotBlank() }.map { it.toInt() }
            Scratchcard(cardNumber, winningNumbers.toSet(), ourNumbers)
        }
    }

    fun part1(scratchcards: List<Scratchcard>): Double {
        return scratchcards.sumOf { it.calculatePointsPart1() }
    }

    tailrec fun processScratchcards(scratchcards: List<Scratchcard>, amountOfCard: Int): Int {
        if (scratchcards.isEmpty()) {
            return amountOfCard - 1
        }

        val current = scratchcards.first()
        val cardsToDuplicate = (current.cardId + 1..current.cardId + current.wins())
                .mapNotNull { rangeItem -> scratchcards.find { it.cardId == rangeItem } }

        return processScratchcards(cardsToDuplicate + scratchcards.drop(1), amountOfCard + 1)
    }

    fun part2(input: List<Scratchcard>): Int {
        return processScratchcards(input, 1)
    }

    val input = readInput("Day04")
    val scratchcards = parseInput(input)

    part1(scratchcards).println()
    part2(scratchcards).println()
}