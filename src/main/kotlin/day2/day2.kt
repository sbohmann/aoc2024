package day2

import java.io.File
import java.util.ArrayList
import kotlin.math.abs

fun main() {
    val rows = File("input")
        .readLines()
        .map { line ->
            line.split(Regex("""\s+"""))
                .map(String::toInt)
        }
    val resultA = rows.fold(0)
        { accumulator, row ->
            accumulator + (if (isSafe(row)) 1 else 0)
        }
    println("A: $resultA")
    val resultB = rows.withIndex().fold(0)
    { accumulator, row ->
        accumulator + (if (isSafe(row.value, true)) 1 else 0)
    }
    println("B: $resultB")
}

fun isSafe(numbers: List<Int>, applyDampener: Boolean = false): Boolean {
    var ascending: Boolean = false
    for (index in 0..<numbers.size - 1) {
        val nextNumberIsHigher = numbers[index + 1] > numbers[index]
        if (index == 0 && nextNumberIsHigher) {
            ascending = true
        }
        if (applyDampener && index == 1 && nextNumberIsHigher != ascending) {
            if (isSafeWithoutIndex(numbers, 0)) {
                return true
            }
        }
        if (index > 0 && nextNumberIsHigher != ascending) {
            return if (applyDampener) isSafeWithDampenerApplied(numbers, index) else false
        }
        val distance = abs(numbers[index + 1] - numbers[index])
        if (distance < 1 || distance > 3) {
            return if (applyDampener) isSafeWithDampenerApplied(numbers, index) else false
        }
    }
    return true
}

fun isSafeWithDampenerApplied(numbers: List<Int>, index: Int): Boolean {
    if (isSafeWithoutIndex(numbers, index)) {
        return true
    }
    if (index < numbers.size - 1 && isSafeWithoutIndex(numbers, index + 1)) {
        return true
    }
    return false
}

private fun isSafeWithoutIndex(numbers: List<Int>, index: Int) =
    isSafe(numbersWithoutIndex(numbers, index), false)

fun numbersWithoutIndex(numbers: List<Int>, excludedIndex: Int): List<Int> {
    val result = ArrayList<Int>(numbers.size - 1)
    for (index in numbers.indices) {
        if (index != excludedIndex) {
            result += numbers[index]
        }
    }
    return result
}
