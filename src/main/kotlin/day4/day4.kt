package day4

import persistentVector.PersistentVector
import java.io.File

data class Map(val width: Int, val height: Int, val data: List<String>)

data class Direction(val deltaX: Int, val deltaY: Int)

fun main() {
    val map = File("input")
        .readLines()
        .fold(Map(width = 0, height = 0, data = PersistentVector()))
        { map, line ->
            if (map.width != 0 && line.length != map.width) {
                throw IllegalStateException()
            }
            map.copy(
                width = line.length,
                height = map.height + 1,
                data = map.data + line
            )
        }
    val word = "XMAS"
    val find = Find(word, map)
    var resultA = 0
    var resultB = 0
    for (y in 0..<map.height) {
        for (x in 0..<map.width) {
            val c = map.data[y][x]
            if (c == word[0]) {
                resultA += find.findWords(x, y)
            }
            if (c == 'A' && find.findCross(x, y)) {
                ++resultB
            }
        }
    }
    println("A: $resultA")
    println("B: $resultB")
}

class Find(val word: String, val map: Map) {
    val directions = PersistentVector(
        Direction(1, 0),
        Direction(1, 1),
        Direction(0, 1),
        Direction(-1, 1),
        Direction(-1, 0),
        Direction(-1, -1),
        Direction(0, -1),
        Direction(1, -1),
    )

    val crossDirections = PersistentVector(
        Pair(Direction(-1, 1), Direction(1, -1)),
        Pair(Direction(-1, -1), Direction(1, 1))
    )

    val crossLetters = setOf('M', 'S')

    fun findWords(xStart: Int, yStart: Int): Int {
        return directions.fold(0) { sum, direction ->
            sum + if (findWord(xStart, yStart, direction)) 1 else 0
        }
    }

    fun findWord(xStart: Int, yStart: Int, direction: Direction): Boolean {
        var x = xStart
        var y = yStart
        for (index in 1..<word.length) {
            x += direction.deltaX
            y += direction.deltaY
            if (x < 0 || x >= map.width
                || y < 0 || y >= map.height
                || map.data[y][x] != word[index]
            ) {
                return false
            }
        }
        return true
    }

    fun findCross(x: Int, y: Int): Boolean {
        if (x == 0 || y == 0 || x == map.width - 1 || y == map.height - 1) {
            return false
        }
        for (direction in crossDirections) {
            val chars = Pair(
                map.data[y + direction.first.deltaY][x + direction.first.deltaX],
                map.data[y + direction.second.deltaY][x + direction.second.deltaX])
            if (chars.first == chars.second) {
                return false
            }
            if (!crossLetters.contains(chars.first) || !crossLetters.contains(chars.second)) {
                return false
            }
        }
        return true
    }
}
