package day9

import java.io.File

fun main() {
    val textWithIndex = File("input")
        .readText()
        .withIndex()

    val resultA = solveA(textWithIndex)
    println(resultA.reduce { a, b -> a + b })
    println("A: ${checksum(resultA)}")

    val resultB = solveA(textWithIndex)
    println(resultA.reduce { a, b -> a + b })
    println("B: ${checksum(resultB)}")
}

private fun solveA(textWithIndex: Iterable<IndexedValue<Char>>): MutableList<Int> {
    val state = textWithIndex
        .flatMap { (index, c) ->
            if (c.isWhitespace()) {
                listOf()
            } else {
                val isFile = (index % 2 == 0)
                List((c.code - '0'.code)) { if (isFile) index / 2 else -1 }
            }
        }
        .toMutableList()
    println(state.reduce { a, b -> a + b })
    println("${checksum(state)}")

    while (true) {
        val firstEmptySlot = state.indexOfFirst { it == -1 }
        val lastFileSlot = state.indexOfLast { it != -1 }
        if (firstEmptySlot != -1 && lastFileSlot != -1 && firstEmptySlot < lastFileSlot) {
            state[firstEmptySlot] = state[lastFileSlot]
            state[lastFileSlot] = -1
        } else {
            break
        }
    }
    return state
}

private data class Chunk(val length: Int, val isFile: Boolean, val id: Int)

private fun solveB(textWithIndex: Iterable<IndexedValue<Char>>): MutableList<Int> {
    val state = textWithIndex
        .filter { (_, c) ->
            !c.isWhitespace()
        }
        .map { (index, c) ->
            val isFile = (index % 2 == 0)
            val fileIndex = if (isFile) index / 2 else -1
            Chunk((c.code - '0'.code), isFile, fileIndex)
        }
        .toMutableList()

    while (true) {
        val firstEmptySlot = state.indexOfFirst { it == -1 }
        val lastFileSlot = state.indexOfLast { it != -1 }
        if (firstEmptySlot != -1 && lastFileSlot != -1 && firstEmptySlot < lastFileSlot) {
            state[firstEmptySlot] = state[lastFileSlot]
            state[lastFileSlot] = -1
        } else {
            break
        }
    }
    return state
}

private fun checksum(state: MutableList<Int>) =
    state
        .withIndex()
        .fold(0L) { sum, (index, n) ->
            if (n == -1) {
                sum
            } else {
                sum + (index * n)
            }
        }
