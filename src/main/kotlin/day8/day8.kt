package day8

import java.io.File
import kotlin.math.min

class Day8 {
    val antennas = mutableMapOf<Char, MutableList<Position>>()
    var width = 0
    var height = 0
    val resultA = mutableSetOf<Position>()
    val resultB = mutableSetOf<Position>()

    fun solve() {
        val lines = File("input")
            .readLines()
        lines
            .withIndex()
            .forEach(::parseLine)
        height = lines.size

        solveA()
        println("A: ${resultA.size}")

        solveB()
        println("B: ${resultB.size}")
    }

    data class Position(val x: Int, val y: Int)

    fun parseLine(indexedValue: IndexedValue<String>) {
        val (y, line) = indexedValue
        setWidth(y, line)
        line.withIndex()
            .forEach({ (x, c) -> readPosition(Position(x, y), c) })
    }

    private fun setWidth(y: Int, line: String) {
        if (y == 0) {
            width = line.length
        } else {
            if (width != line.length) {
                throw IllegalStateException()
            }
        }
    }

    fun readPosition(position: Position, c: Char) {
        if (c != '.') {
            val positions = antennas.computeIfAbsent(c, { _ -> mutableListOf() })
            positions.add(position)
        }
    }

    fun solveA() {
        for (positions in antennas.values) {
            for (lhsIndex in 0..<positions.size - 1) {
                val lhs = positions[lhsIndex]
                for (rhsIndex in lhsIndex + 1..< positions.size) {
                    val rhs = positions[rhsIndex]
                    val dx = rhs.x - lhs.x
                    val dy = rhs.y - lhs.y
                    addAntinodeA(lhs.x - dx, lhs.y - dy)
                    addAntinodeA(rhs.x + dx, rhs.y + dy)
                }
            }
        }
    }

    private fun addAntinodeA(x: Int, y: Int) {
        val position = Position(x, y)
        if (insideMap(position)) {
            resultA.add(position)
        }
    }

    fun solveB() {
        for (positions in antennas.values) {
            for (lhsIndex in 0..<positions.size - 1) {
                val lhs = positions[lhsIndex]
                for (rhsIndex in lhsIndex + 1..< positions.size) {
                    val rhs = positions[rhsIndex]
                    val (dx, dy) = simplify(rhs, lhs)
                    resultB.add(lhs)
                    trace(lhs, dx, dy)
                    trace(lhs, -dx, -dy)
                }
            }
        }
    }

    private fun simplify(rhs: Position, lhs: Position): Pair<Int, Int> {
        var dx = rhs.x - lhs.x
        var dy = rhs.y - lhs.y
        for (n in 2..min(dx, dy)) {
            while (dx % n == 0 && dy % n == 0) {
                dx /= n
                dy /= n
            }
        }
        return Pair(dx, dy)
    }

    fun trace(pos: Position, dx: Int, dy: Int) {
        var n = 1
        while (addAntinodeB(pos.x + n * dx, pos.y + n * dy)) {
            ++n
        }
    }

    private fun addAntinodeB(x: Int, y: Int): Boolean {
        val position = Position(x, y)
        if (insideMap(position)) {
            resultB.add(position)
            return true
        } else {
            return false
        }
    }

    fun insideMap(position: Position): Boolean {
        return position.x >= 0 &&
                position.y >=0 &&
                position.x < width &&
                position.y < height
    }
}

fun main() = Day8().solve()
