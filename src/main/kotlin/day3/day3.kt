package day3

import java.io.File
import java.time.LocalDate
import java.util.ArrayList
import kotlin.math.abs

fun main() {
    val sb = StringBuilder()
    sb.append("a")
    val input = File("input")
        .readText()
    val regex = """do\(\)|don't\(\)|mul\((\d+),(\d+)\)""".toRegex()
    val result = regex.findAll(input)
        .fold(State()) { state, match ->
            when (match.groups[0]!!.value) {
                "do()" -> state.copy(active = true)
                "don't()" -> state.copy(active = false)
                else -> {
                    val mulResult = parseMul(match)
                    state.copy(
                        resultA = state.resultA + mulResult,
                        resultB = if (state.active) {
                            state.resultB + mulResult
                        } else {
                            state.resultB
                        })
                }
            }
        }
    println("A: ${result.resultA}")
    println("B: ${result.resultB}")
}

data class State(
    val active: Boolean = true,
    val resultA: Int = 0,
    val resultB: Int = 0
)

fun parseMul(match: MatchResult): Int {
    val lhs = match.groups[1]!!.value.toInt()
    val rhs = match.groups[2]!!.value.toInt()
    return lhs * rhs
}
