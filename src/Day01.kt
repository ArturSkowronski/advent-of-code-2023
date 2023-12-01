fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf {
            val result = it
                    .split("").mapNotNull { it.toIntOrNull() }

            println(result.first() * 10 + result.last())
            result.first() * 10 + result.last()
        }
    }

    fun preprocessHead(it: String): String {
        var watchdog = true
        var inputString = it
        while (watchdog) {
            var head = "";
            var tail = "";
            for (i in 0..<inputString.length+1) {
                head = inputString.take(i)
                        .replace("one", "1")
                        .replace("two", "2")
                        .replace("three", "3")
                        .replace("four", "4")
                        .replace("five", "5")
                        .replace("six", "6")
                        .replace("seven", "7")
                        .replace("eight", "8")
                        .replace("nine", "9")
                        .replace("zero", "0")
                tail = inputString.drop(i)
                if (inputString.length != head.length + tail.length) break
            }
            if (inputString.length == head.length + tail.length) watchdog = false
            else {
                inputString = head + tail
            }
        }
        return inputString;
    }

    fun preprocessTail(it: String): String {
        var watchdog = true
        var inputString = it
        while (watchdog) {
            var head = "";
            var tail = "";
            for (i in 0..<inputString.length+1) {
                tail = inputString.takeLast(i)
                        .replace("one", "1")
                        .replace("two", "2")
                        .replace("three", "3")
                        .replace("four", "4")
                        .replace("five", "5")
                        .replace("six", "6")
                        .replace("seven", "7")
                        .replace("eight", "8")
                        .replace("nine", "9")
                        .replace("zero", "0")
                head = inputString.dropLast(i)
                if (inputString.length != head.length + tail.length) break
            }
            if (inputString.length == head.length + tail.length) watchdog = false
            else {
                inputString = head+tail
            }
        }
        return inputString;
    }

    fun part2(input: List<String>): Int {
         return input.sumOf {
            val resultHead = preprocessHead(it)
                    .split("").mapNotNull { it.toIntOrNull() }

             val resultTail = preprocessTail(it)
                     .split("").mapNotNull { it.toIntOrNull() }

             resultHead.first() * 10 + resultTail.last()
        }
    }

    val input = readInput("Day01")
    // part1(input).println()
    part2(input).println()
}