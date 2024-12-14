package day12

import java.io.File

fun main() {
    val solution = Day12()
    println("A: ${solution.solveA()}")
    println("B: ${solution.solveB()}")
}

class Day12 {
    val data: List<String> =
        File("input")
            .readLines()
    val width: Int = data[0].length
    val height: Int = data.size

    val groupForField = HashMap<Position, Group>()
    val groups = ArrayList<Group>()

    init {
        readGroups()
        println(groupForField.size)
        println(groups.size)
    }

    fun readGroups() {
        for (y in 0..<height) {
            for (x in 0..<width) {
                val pos = Position(x, y)
                var group = groupForField[pos]
                if (group == null) {
                    group = Group(name(x, y))
                    groups.add(group)
                    fillGroup(group, pos)
                }
            }
        }
    }

    fun fillGroup(group: Group, pos: Position) {
        if (name(pos) != group.name) {
            return
        }
        if (groupForField[pos] != null) {
            return
        }
        groupForField[pos] = group
        group.fields.add(pos)
        neighbors(pos)
            .forEach { neighbor -> fillGroup(group, neighbor) }
    }

    fun neighbors(pos: Position): List<Position> {
        return listOf(
            Position(pos.x, pos.y - 1),
            Position(pos.x, pos.y + 1),
            Position(pos.x - 1, pos.y),
            Position(pos.x + 1, pos.y)
        ).filter(this::inside)
    }

    fun inside(pos: Position): Boolean {
        return pos.x >= 0 && pos.y >= 0 &&
                pos.x < width && pos.y < height
    }

    fun solveA(): Int {
        return groups.fold(0) { result, group ->
            val area = group.fields.size
            val circumference = calculateCircumference(group)
            result + area * circumference
        }
    }

    private fun calculateCircumference(group: Group): Int {
        return group.fields
            .fold(0) { sum, pos ->
                val foreignNeighbors =
                    neighbors(pos).filter { name(it) != group.name }
                val edges = countFieldEdges(pos)
                sum + foreignNeighbors.size + edges
            }
    }

    fun countFieldEdges(pos: Position): Int {
        var result = 0
        if (pos.x == 0) ++result
        if (pos.y == 0) ++result
        if (pos.x == width - 1) ++result
        if (pos.y == height - 1) ++result
        return result
    }

    fun solveB(): Int {
        return groups.fold(0) { result, group ->
            val area = group.fields.size
            val edges = edges(group)
            result + area * edges
        }
    }

    fun edges(group: Group): Int {
        return Direction.entries
            .fold(0) { sum, direction ->
                sum + edgesInDirection(group, direction)
            }
    }

    fun edgesInDirection(group: Group, dir: Direction): Int {
        val fields = group.fields.toSet()
        return group.fields
            .filter { !fields.contains(it.plus(dir)) }
            .groupBy(dir.edge.groupBy)
            .values
            .map { it.map(dir.edge.sortBy) }
            .map { it.sorted() }
            .fold(0) { sum, g -> sum + runs(g) }
    }

    fun runs(list: List<Int>): Int {
        if (list.size < 2) {
            return list.size
        } else {
            var result = 1
            var last = list.first()
            for (index in 1..<list.size) {
                if (list[index] != last + 1) {
                    ++result
                }
                last = list[index]
            }
            return result
        }
    }

    data class Position(val x: Int, val y: Int) {
        fun plus(dir: Direction): Position {
            return Position(x + dir.dx, y + dir.dy)
        }
    }

    class Group(val name: Char, val fields: MutableList<Position> = ArrayList())

    enum class Direction(val dx: Int, val dy: Int, val edge: Orientation) {
        Up(0, -1, Orientation.Horizontal),
        Down(0, 1, Orientation.Horizontal),
        Left(-1, 0, Orientation.Vertical),
        Right(1, 0, Orientation.Vertical)
    }

    enum class Orientation(val sortBy: (Position) -> Int, val groupBy: (Position) -> Int) {
        Horizontal({ it.x }, { it.y }),
        Vertical({ it.y }, { it.x })
    }

    private fun name(pos: Position) = name(pos.x, pos.y)
    private fun name(x: Int, y: Int) = data[y][x]
}
