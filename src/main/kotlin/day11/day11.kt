package day11

import java.io.File

fun main() {
    val initialState = File("input")
        .readText()
        .trim()
        .split(' ')
        .map { it.toLong() }

    println("A: ${solveA(initialState).size}")
    println("B: ${solveB(initialState)}")
}

fun solveA(initialState: List<Long>): List<Long> {
    var state = initialState
    for (index in 0..<25) {
        state = state.flatMap { n ->
            change(n)
        }
    }
    return state
}

val knownResults = HashMap<Pair<Int,Long>, Long>()

fun solveB(initialState: List<Long>): Long {
    return initialState.fold(0) { sum, next ->
        sum + count(75, next)
    }
}

fun count(rounds: Int, n: Long): Long {
    if (rounds == 0) {
        return 1
    }
    val knownResult = knownResults[Pair(rounds, n)]
    if (knownResult != null) {
        return knownResult
    }
    val after = change(n)
    val result: Long = after.fold(0) { sum, next -> sum + count(rounds - 1, next) }
    knownResults.putIfAbsent(Pair(rounds, n), result)
    return result
}

private fun change(n: Long) =
    if (n == 0L) {
        listOf(1L)
    } else {
        val s = n.toString()
        if (s.length % 2 == 0) {
            listOf(
                s.substring(0, s.length / 2).toLong(),
                s.substring(s.length / 2).toLong()
            )
        } else {
            listOf(n * 2024)
        }
    }
