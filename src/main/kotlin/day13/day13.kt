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
}

fun parseSetup(input: String): Setup {
    val match = setupRegex.matchEntire(input)!!
    return Setup(
        Step(match.groups[1]!!.value.toInt(), match.groups[2]!!.value.toInt()),
        Step(match.groups[3]!!.value.toInt(), match.groups[4]!!.value.toInt()),
        Position(match.groups[3]!!.value.toInt(), match.groups[4]!!.value.toInt())
    )
}

fun solveA(setups: List<Setup>): Int {
    return setups
        .fold(0) { sum, next -> sum + minimumTokensForSetup(next) }
}

fun minimumTokensForSetup(setup: Setup): Int {
    return 0
}

data class Setup(val a: Step, val b: Step, val prize: Position)

data class Position(val x: Int, val y: Int)

data class Step(val dx: Int, val dy: Int)
