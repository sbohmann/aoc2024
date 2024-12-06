package day6

import persistentVector.PersistentVector
import java.io.File

const val inputFileName = "input"

fun main() {
    Solution().run()
}

data class Position(val x: Int, val y: Int)

data class Map(val width: Int, val height: Int, val rows: PersistentVector<List<Boolean>>) {
    fun get(position: Position): Boolean {
        return rows[position.y][position.x]
    }
}

class Solution {
    val map: Map
    val initialGuardPosition: Position

    init {
        val (map, initialGuardPosition) = readInput()
        this.map = map
        this.initialGuardPosition = initialGuardPosition!!
    }

    fun readInput(): Pair<Map, Position?> {
        return File(inputFileName)
            .readLines()
            .fold(Pair(Map(width = 0, height = 0, rows = PersistentVector()), null as Position?))
            { (map, oldGuardPosition), line ->
                val (row, guardPositionX) = parseRow(line)
                val guardPosition = if (guardPositionX != null) {
                    val newGuardPosition = Position(guardPositionX, map.height)
                    if (oldGuardPosition != null) {
                        throw IllegalStateException("Double guard position found - old: $oldGuardPosition, new: $newGuardPosition")
                    }
                    newGuardPosition
                } else {
                    oldGuardPosition
                }
                if (map.height == 0) {
                    Pair(map.copy(width = row.size, height = 1, rows = map.rows + row), guardPosition)
                } else {
                    if (row.size != map.width) {
                        throw IllegalStateException("row size [${row.size}] != map.width [${map.width}")
                    }
                    Pair(map.copy(height = map.height + 1, rows = map.rows + row), guardPosition)
                }
            }
    }

    fun run() {
        val resultA = solveA()
        println("A: $resultA")

        val resultB = solveB()
        println("B: $resultB")
    }

    fun parseRow(line: String): Pair<List<Boolean>, Int?> {
        var guardPosition: Int? = null
        val row = line.withIndex()
            .map {
                if (it.value == '^') {
                    guardPosition = it.index
                }
                when (it.value) {
                    '#' -> true
                    '.', '^' -> false
                    else -> throw IllegalArgumentException("Unparsable character [$it]")
                }
            }
        return Pair(row, guardPosition)
    }

    enum class Direction(val deltaX: Int, val deltaY: Int, val next: () -> Direction) {
        Up(0, -1, { Right }),
        Right(1, 0, { Down }),
        Down(0, 1, { Left }),
        Left(-1, 0, { Up })
    }

    fun solveA(): Int {
        var guardPosition = initialGuardPosition
        var direction = Direction.Up
        val positionsOccupied = mutableSetOf<Position>()
        while (inside(guardPosition, map)) {
            positionsOccupied.add(guardPosition)
            direction = adaptedDirection(guardPosition, direction, map)
            guardPosition = move(guardPosition, direction)
        }
        return positionsOccupied.size
    }

    fun solveB(): Int {
        var loopingPositionsCount = 0
        for (y in 0..<map.height) {
            for (x in 0..<map.width) {
                val position = Position(x, y)
                if (initialGuardPosition != position && !occupied(position, map)) {
                    val modifiedMap = buildMapOccupiedAt(map, y, x)
                    if (looping(modifiedMap)) {
                        ++loopingPositionsCount
                    }
                }
            }
        }
        return loopingPositionsCount
    }

    fun looping(modifiedMap: Map): Boolean {
        var guardPosition = initialGuardPosition
        var direction = Direction.Up
        val knownStates = mutableSetOf<Pair<Position, Direction>>()
        while (inside(guardPosition, modifiedMap)) {
            if (!knownStates.add(Pair(guardPosition, direction))) {
                return true
            }
            direction = adaptedDirection(guardPosition, direction, modifiedMap)
            guardPosition = move(guardPosition, direction)
        }
        return false
    }

    private fun buildMapOccupiedAt(map: Map, y: Int, x: Int): Map {
        val modifiedRows = map.rows.withIndex()
            .map { indexedRow ->
                val (rowIndex, row) = indexedRow
                if (rowIndex == y) {
                    row.withIndex()
                        .map { indexedValue ->
                            val (valueIndex, value) = indexedValue
                            if (valueIndex == x) {
                                true
                            } else {
                                value
                            }
                        }
                } else {
                    row
                }
            }
        val modifiedMap = map.copy(rows = PersistentVector(modifiedRows))
        return modifiedMap
    }

    fun inside(position: Position, map: Map): Boolean {
        return position.x >= 0 && position.y >= 0
                && position.x < map.width && position.y < map.height
    }

    fun adaptedDirection(position: Position, originalDirection: Direction, map: Map): Direction {
        var direction = originalDirection
        while (occupiedInDirection(position, direction, map)) {
            direction = direction.next()
            if (direction == originalDirection) {
                throw IllegalStateException("Logical error: Trapped in position $position")
            }
        }
        return direction
    }

    fun occupiedInDirection(position: Position, direction: Direction, map: Map): Boolean {
        val nextPosition = move(position, direction)
        return occupied(nextPosition, map)
    }

    private fun occupied(nextPosition: Position, map: Map) =
        inside(nextPosition, map) && map.get(nextPosition)

    fun move(position: Position, by: Direction): Position {
        return position.copy(
            x = position.x + by.deltaX,
            y = position.y + by.deltaY
        )
    }
}
