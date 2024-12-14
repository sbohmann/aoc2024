package day10

import java.io.File

const val inputPath = "input"

fun main() {
    val map = readMapData()
    println("A: ${A(map).result}")
    println("B: ${B(map).result}")
}

class A(val map: Map) {
    val result: Int

    init {
        result = map.fields()
            .filter { it.elevation == 0 }
            .fold(0) { sum, field ->
                sum + endPositionsReachable(field)
            }
    }

    fun endPositionsReachable(field: Field): Int {
        var state = setOf(field)
        for (elevation in field.elevation..<9) {
            val reachableEndFields = state.flatMap { HashSet(map.neighboringPositionsElevatedByOne(it)) }
            state = HashSet(reachableEndFields)
        }
        return state.size
    }
}

class B(val map: Map) {
    val result: Int

    init {
        result = map.fields()
            .filter { it.elevation == 0 }
            .fold(0) { sum, field ->
                sum + numberOfPathsToEndPositions(field)
            }
    }

    fun numberOfPathsToEndPositions(field: Field): Int {
        return pathsToEndPositions(field, setOf(listOf())).size
    }

    fun pathsToEndPositions(field: Field, pathsToField: Set<List<Field>>): Set<List<Field>> {
        val result = pathsToField.map { path -> path + field }
        if (field.elevation == 9) {
            return result.toSet()
        } else {
            return map.neighboringPositionsElevatedByOne(field)
                .flatMap { neighboringField -> pathsToEndPositions(neighboringField, result.toSet()) }
                .toSet()
        }
    }
}

private fun readMapData(): Map {
    val mapData = File(inputPath)
        .readLines()
        .withIndex()
        .map { (y, line) -> parseLine(y, line) }
    val width = mapData.first().size
    val height = mapData.size
    return Map(width, height, mapData)
}

fun parseLine(y: Int, line: String): List<Field> {
    return line.withIndex()
        .map { (x, c) ->
            val elevation = c - '0'
            Field(
                Position(x, y), elevation
            )
        }
}

class Map(val width: Int, val height: Int, private val data: List<List<Field>>) {
    fun fields(): List<Field> {
        return data.flatten()
    }

    fun neighboringPositionsElevatedByOne(field: Field): List<Field> {
        return neighboringFields(field.position)
            .filter { it.elevation == field.elevation + 1 }
    }

    fun neighboringFields(position: Position): List<Field> {
        return Direction.entries
            .map { Position(position.x + it.dx, position.y + it.dy) }
            .filter(this::inside)
            .map(this::fieldForPosition)
    }

    private fun inside(position: Position): Boolean {
        return inside(position.x, position.y)
    }

    private fun inside(x: Int, y: Int): Boolean {
        return x >= 0 && y >= 0 &&
                x < width && y < height
    }

    private fun fieldForPosition(position: Position): Field {
        return data[position.y][position.x]
    }
}

class Field(val position: Position, val elevation: Int)

data class Position(val x: Int, val y: Int)

enum class Direction(val dx: Int, val dy: Int) {
    Up(0, -1),
    Down(0, 1),
    Left(-1, 0),
    Right(1, 0)
}
