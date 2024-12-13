package day9

import java.io.File

fun main() {
    val textWithIndex = File("input")
        .readText()
        .filter { !it.isWhitespace() }
        .withIndex()

    val resultA = solveA(textWithIndex)
    println(resultA.reduce { a, b -> a + b })
    println("A: ${checksum(resultA)}")

    val resultB = solveB(textWithIndex)
    println(resultA.reduce { a, b -> a + b })
    println("B: ${checksum(resultB)}")
}

private fun solveA(textWithIndex: Iterable<IndexedValue<Char>>): List<Int> {
    val state = textWithIndex
        .flatMap { (index, c) ->
            val isFile = (index % 2 == 0)
            List((c.code - '0'.code)) { if (isFile) index / 2 else -1 }
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

private fun solveB(textWithIndex: Iterable<IndexedValue<Char>>): List<Int> {
    var state = textWithIndex
        .map { (index, c) ->
            val isFile = (index % 2 == 0)
            val fileIndex = if (isFile) index / 2 else -1
            val length = c.code - '0'.code
            Chunk(length, isFile, fileIndex)
        }
        .toList()

        val maxId = state.map { it -> it.id }.max()
        for (id in maxId downTo 0) {
            val (fileChunkIndex, fileChunk) = state.withIndex().last { (_, it) -> it.id == id }
            val firstEmptySpaceIndex = state.indexOfFirst { !it.isFile && it.length >= fileChunk.length }
            if (firstEmptySpaceIndex == -1 || firstEmptySpaceIndex >= fileChunkIndex) {
                continue
            }
            val emptySpace = state[firstEmptySpaceIndex]
            state = state.withIndex()
                .flatMap { (index, chunk) ->
                    if (index == firstEmptySpaceIndex) {
                        val newEmptyChunkLength = emptySpace.length - fileChunk.length
                        if (newEmptyChunkLength == 0) {
                            listOf(fileChunk)
                        } else {
                            listOf(
                                fileChunk,
                                Chunk(newEmptyChunkLength, false, -1)
                            )
                        }
                    } else if (index == fileChunkIndex) {
                        listOf(Chunk(fileChunk.length, false, -1))
                    } else {
                        listOf(chunk)
                    }
                }
        }
    return state.flatMap { chunk ->
        List(chunk.length) { _ -> chunk.id }
    }
}

private fun checksum(state: List<Int>) =
    state
        .withIndex()
        .fold(0L) { sum, (index, n) ->
            if (n == -1) {
                sum
            } else {
                sum + (index * n)
            }
        }
