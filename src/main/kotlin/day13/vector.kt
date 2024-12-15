package day13

import kotlin.math.min

val ZeroVector = Vector(0L, 0L)

data class Vector(val x: Long, val y: Long) {
    operator fun plus(other: Vector): Vector {
        return Vector(x + other.x, y + other.y)
    }
    operator fun minus(other: Vector): Vector {
        return Vector(x - other.x, y - other.y)
    }

    operator fun times(n: Long): Vector {
        return Vector(n * x, n * y)
    }

    operator fun div(other: Vector): DivisionResult {
        val factor = min(x / other.x, y / other.y)
        val remainder = this - other * factor
        return DivisionResult(factor, remainder)
    }

    val length = x + y

    val isNegative = x < 0L || y < 0L

    val isZero = x == 0L && y == 0L
}

operator fun Long.times(vector: Vector): Vector {
    return vector * this
}

operator fun Int.times(vector: Vector): Vector {
    return vector * this.toLong()
}

data class DivisionResult(val quotient: Long, val remainder: Vector)
