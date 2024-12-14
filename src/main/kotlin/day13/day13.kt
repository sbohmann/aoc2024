package day13

import java.io.File

val newline = Regex("""\r?\n""")
val doubleNewline = Regex("""\r?\n\r?\n""")
val setupRegex = Regex(
    """Button A: X\+(\d+), Y\+(\d+)\r?\n""" +
            """Button B: X\+(\d+), Y\+(\d+)\r?\n""" +
            """Prize: X=(\d+), Y=(\d+)\s*"""
)

fun main() {
    val setups = File("input")
        .readText()
        .split(doubleNewline)
        .map { parseSetup(it) }
    println("A: ${solveA(setups)}")
    println("B: ${solveB(setups)}")
}

fun parseSetup(input: String): Setup {
    val match = setupRegex.matchEntire(input)!!
    return Setup(
        Step(match.groups[1]!!.value.toLong(), match.groups[2]!!.value.toLong()),
        Step(match.groups[3]!!.value.toLong(), match.groups[4]!!.value.toLong()),
        Position(match.groups[5]!!.value.toLong(), match.groups[6]!!.value.toLong())
    )
}

fun solveA(setups: List<Setup>): Long {
    return setups
        .fold(0) { sum, next -> sum + minimumTokensForSetup(next) }
}

fun solveB(setups: List<Setup>): Long {
    return setups
        .map { setup ->
            setup.copy(prize = prizeAdjustedForBSolution(setup.prize))
        }
        .fold(0) { sum, next -> sum + minimumTokensForSetup(next) }
}

fun prizeAdjustedForBSolution(prize: Position): Position {
    return Position(
        prize.x + 10000000000000L,
        prize.y + 10000000000000L
    )
}

fun minimumTokensForSetup(setup: Setup): Long {
    var aButtonCount = 0
    val results = mutableListOf<Long>()
    while (true) {
        val xOffset = aButtonCount * setup.a.dx
        val yOffset = aButtonCount * setup.a.dy
        if (xOffset > setup.prize.x || yOffset > setup.prize.y) {
            break
        }
        val bButtonCount = findBButtonCount(setup, xOffset, yOffset)
        if (bButtonCount != null) {
            results.add(3 * aButtonCount + bButtonCount)
        }
        ++aButtonCount
    }
    return results.minOrNull() ?: 0
}

fun findBButtonCount(setup: Setup, xOffset: Long, yOffset: Long): Long? {
    var bButtonCount = 0L
    while (true) {
        val dx = bButtonCount * setup.b.dx
        val dy = bButtonCount * setup.b.dy
        val x = xOffset + dx
        val y = yOffset + dy
        if (x == setup.prize.x && y == setup.prize.y) {
            return bButtonCount
        }
        if (x > setup.prize.x || y > setup.prize.y) {
            return null
        }
        ++bButtonCount
    }
}

data class Setup(val a: Step, val b: Step, val prize: Position)

data class Position(val x: Long, val y: Long)

data class Step(val dx: Long, val dy: Long)
