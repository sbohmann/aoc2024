package day13

import java.io.File

val newline = Regex("""\r?\n""")
val doubleNewline = Regex("""\r?\n\r?\n""")
val setupRegex = Regex(
    """Button A: X\+(\d+), Y\+(\d+)\r?\n""" +
            """Button B: X\+(\d+), Y\+(\d+)\r?\n""" +
            """Prize: X=(\d+), Y=(\d+)\s*"""
)
const val solutionBOffset = 10000000000000L

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
        Vector(match.groups[1]!!.value.toLong(), match.groups[2]!!.value.toLong()),
        Vector(match.groups[3]!!.value.toLong(), match.groups[4]!!.value.toLong()),
        Vector(match.groups[5]!!.value.toLong(), match.groups[6]!!.value.toLong())
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
        .fold(0) { sum, next ->
            return sum + searchForPrize(next)
        }
}

fun prizeAdjustedForBSolution(prize: Vector): Vector {
    return Vector(
        prize.x + solutionBOffset,
        prize.y + solutionBOffset
    )
}

data class AdjustedOffset(val position: Vector, val aButtonCount: Long, val bButtonCount: Long)

fun searchForPrize(setup: Setup): Long {
    val result =
        approximate(true, setup.a, setup.b, 0, 0, setup.prize)
            ?: return 0L
    return 3 * result.aButtonCount + result.bButtonCount
}

fun approximate(
    vectorA: Boolean,
    currentVector: Vector,
    nextVector: Vector,
    aVectorSteps: Long,
    bVectorSteps: Long,
    distance: Vector
): AdjustedOffset? {
    val steps = distance / currentVector
    if (steps.quotient == 0L && !steps.remainder.isZero) {
        return null
    }
    val relativeOffset = steps.quotient * currentVector
    if (relativeOffset.isNegative) {
        throw IllegalStateException("Overshot prize")
    }
    if (steps.remainder.isZero) {
        return AdjustedOffset(
            relativeOffset,
            aVectorSteps + if (vectorA) steps.quotient else 0L,
            bVectorSteps + if (!vectorA) steps.quotient else 0L,
        )
    }
    return approximate(
        !vectorA,
        nextVector,
        currentVector,
        aVectorSteps + if (vectorA) steps.quotient else 0L,
        bVectorSteps + if (!vectorA) steps.quotient else 0L,
        distance - relativeOffset
    )
}

fun minimumTokensForSetup(setup: Setup): Long {
    var aButtonCount = 0
    val results = mutableListOf<Long>()
    while (true) {
        val xOffset = aButtonCount * setup.a.x
        val yOffset = aButtonCount * setup.a.y
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
        val dx = bButtonCount * setup.b.x
        val dy = bButtonCount * setup.b.y
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

data class Setup(val a: Vector, val b: Vector, val prize: Vector)
