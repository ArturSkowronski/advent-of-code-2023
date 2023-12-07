fun main() {

    val cardStrength = mapOf(
            "A" to 14, "K" to 13, "Q" to 12, "J" to 11, "T" to 10,
            "9" to 9, "8" to 8, "7" to 7, "6" to 6, "5" to 5,
            "4" to 4, "3" to 3, "2" to 2
    )


    val cardStrengthPartTwo = mapOf(
            "J" to 1, "A" to 14, "K" to 13, "Q" to 12, "T" to 10,
            "9" to 9, "8" to 8, "7" to 7, "6" to 6, "5" to 5,
            "4" to 4, "3" to 3, "2" to 2
    )

    data class Hand(val cards: List<String>, val bid: Int) {

        val handType: Int
        val handTypeJoker: Int

        init {
            val cardCounts = cards.groupingBy { it }.eachCount()
            val numberOfJokers = cardCounts["J"] ?: 0
            val nonJokerCounts = cardCounts.filterKeys { it != "J" }

            handType = when {
                cardCounts.any { it.value == 5 } -> 1
                cardCounts.any { it.value == 4 } -> 2
                cardCounts.size == 2 -> 3
                cardCounts.any { it.value == 3 } -> 4
                cardCounts.size == 3 -> 5
                cardCounts.size == 4 -> 6
                else -> 7
            }

            handTypeJoker = when {
                numberOfJokers == 5 -> 1
                nonJokerCounts.any { it.value + numberOfJokers >= 5 } -> 1
                nonJokerCounts.any { it.value + numberOfJokers == 4 } -> 2
                nonJokerCounts.any { it.value == 3 } ||
                        (nonJokerCounts.any { it.value == 2 } && numberOfJokers >= 1) ||
                        (nonJokerCounts.size == 1 && nonJokerCounts.values.first() + numberOfJokers == 3) -> 3
                nonJokerCounts.any { it.value + numberOfJokers == 3 } ||
                        (nonJokerCounts.size == 2 && nonJokerCounts.values.count { it + numberOfJokers == 2 } >= 1) -> 4
                nonJokerCounts.size >= 2 && nonJokerCounts.values.any { it + numberOfJokers >= 2 } -> 5
                nonJokerCounts.size >= 1 && numberOfJokers >= 1 -> 6
                else -> 7
            }
        }
    }

    fun parseInput(input: List<String>): List<Hand> {
        return input.map {
            val (hand, bid) = it.split(" ")
            Hand(hand.map { it.toString() }, bid.toInt())
        }
    }

    fun part1(hands: List<Hand>): Long {
        val sortedHands = hands.sortedWith(Comparator { h1, h2 ->
            if (h1.handType != h2.handType) {
                h2.handType.compareTo(h1.handType)
            } else {
                val h1CardsSorted = h1.cards.map { cardStrength[it]!! }
                val h2CardsSorted = h2.cards.map { cardStrength[it]!! }
                for (i in h1CardsSorted.indices) {
                    val compResult = h1CardsSorted[i].compareTo(h2CardsSorted[i])
                    if (compResult != 0) {
                        return@Comparator compResult
                    }
                }
                0
            }
        })

        var totalWinnings = 0L
        for ((rank, hand) in sortedHands.withIndex()) {
            totalWinnings += hand.bid * (rank + 1)
        }

        return totalWinnings
    }

    fun part2(hands: List<Hand>): Long {
        val sortedHands = hands.sortedWith(Comparator { h1, h2 ->
            if (h1.handTypeJoker != h2.handTypeJoker) {
                h2.handTypeJoker.compareTo(h1.handTypeJoker)
            } else {
                val h1CardsSorted = h1.cards.map { cardStrengthPartTwo[it]!! }
                val h2CardsSorted = h2.cards.map { cardStrengthPartTwo[it]!! }
                for (i in h1CardsSorted.indices) {
                    val compResult = h1CardsSorted[i].compareTo(h2CardsSorted[i])
                    if (compResult != 0) {
                        return@Comparator compResult
                    }
                }
                0
            }
        })

        var totalWinnings = 0L
        for ((rank, hand) in sortedHands.withIndex()) {
            totalWinnings += hand.bid * (rank + 1)
        }

        return totalWinnings
    }


    val input = readInput("Day07")
    val hands = parseInput(input)

    println(part1(hands))
    println(part2(hands))
}
