fun main() {
    data class CubeSet(val red: Int, val blue: Int, val green: Int) {
        fun invalidGame(maxRed: Int, maxGreen: Int, maxBlue: Int): Boolean {
            return red > maxRed || green > maxGreen || blue > maxBlue
        }
    }

    data class Game(val id: Int, val cubeSets: List<CubeSet>) {
        fun validatePart1(): Boolean {
            val MAX_RED = 12;
            val MAX_GREEN = 13;
            val MAX_BLUE = 14;

            return cubeSets.none { it.invalidGame(MAX_RED, MAX_GREEN, MAX_BLUE) }
        }

        fun findPower(): Int {
            return cubeSets.maxOf { it.red } *
                    cubeSets.maxOf { it.blue } *
                    cubeSets.maxOf { it.green }
        }
    }

    fun parseInput(input: List<String>): List<Game> {
        return input.map { game ->
            val gameId = game.split(":")[0].trim().replace("Game ", "").toInt()
            val cubeSets = game.split(":")[1]
                    .split(";")
                    .map { cubeSet ->
                        val cubes = cubeSet.split(",")
                                .map { it.trim() }
                                .associate {
                                    it.split(" ")[1] to it.split(" ")[0].toInt()
                                }

                        CubeSet(
                                cubes.getOrElse("red") { 0 },
                                cubes.getOrElse("blue") { 0 },
                                cubes.getOrElse("green") { 0 },
                        )
                    }
            Game(gameId, cubeSets)
        }
    }

    fun part1(games: List<Game>): Int {
        return games.filter { it.validatePart1() }.sumOf { it.id }
    }

    fun part2(games: List<Game>): Int {
        return games.sumOf { it.findPower() }
    }


    val input = readInput("Day02")
    val games = parseInput(input)

    part1(games).println()
    part2(games).println()
}