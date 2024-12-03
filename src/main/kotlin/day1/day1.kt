package day1

import java.io.File
import kotlin.math.abs

data class Data(val lhs: List<Int>, val rhs: List<Int>)

fun main() {
    val data = File("input")
        .readLines()
        .fold(Data(lhs = listOf(), rhs = listOf()))
        { accumulator, line ->
            val match = Regex("""(\d+)\s+(\d+)""")
                .find(line)!!
            Data(
                lhs = accumulator.lhs + match.groups[1]!!.value.toInt(),
                rhs = accumulator.rhs + match.groups[2]!!.value.toInt()
            )
        }
    val lhs = data.lhs.sorted()
    val rhs = data.rhs.sorted()
    var distance = 0
    for (index in 0..<lhs.size) {
        distance += abs(lhs[index] - rhs[index])
    }
    println("A: $distance")
    val distinctLhsNumbers = HashSet(lhs)
    fun numberOfOccurrencesInRhs(value: Int): Int {
        return rhs.fold(0) { accumulator, next ->
            accumulator + (if (next == value) 1 else 0)
        }
    }

    val similarity = distinctLhsNumbers
        .fold(0) { accumulator, next ->
            accumulator + (next * numberOfOccurrencesInRhs(next))
        }

    println("B: $similarity")
}
