package day7

import java.io.File
import kotlin.math.pow
import kotlin.math.roundToInt

fun main() {
    val equations = File("input")
        .readLines()
        .map(::parseEquation)

    var resultA = 0L
    val operatorsForA = Operator.entries.subList(0, 2)
    for (equation in equations) {
        if (possible(equation, operatorsForA)) {
            resultA += equation.result
        }
    }
    println("A: $resultA")

    var resultB = 0L
    for (equation in equations) {
        if (possible(equation, Operator.entries)) {
            resultB += equation.result
        }
    }
    println("B: $resultB")
}

data class Equation(val result: Long, val input: List<Long>)

fun parseEquation(line: String): Equation {
    val (resultText, inputText) = line.split(": ")
    val result = resultText.toLong()
    val input = inputText.split(" ")
        .map({ it.toLong() })
    return Equation(result, input)
}

enum class Operator(val apply: (Long,Long) -> Long) {
    Addition({ lhs, rhs -> lhs + rhs }),
    Multiplication({ lhs, rhs -> lhs * rhs}),
    Concatenation({ lhs, rhs -> (lhs.toString() + rhs.toString()).toLong() })
}

class operatorChains(val availableOperators: List<Operator>, val length: Int): Iterator<List<Operator>> {
    val period = availableOperators.size.toDouble().pow(length.toDouble()).roundToInt()
    var state = 0

    override fun hasNext(): Boolean {
        return state < period
    }

    override fun next(): List<Operator> {
        val result = mutableListOf<Operator>()
        var chunk = state
        for (chunkIndex in 0..<length) {
            val operatorIndex = chunk % availableOperators.size
            chunk /= availableOperators.size
            result.add(availableOperators[operatorIndex])
        }
        ++state
        return result
    }
}

fun possible(equation: Equation, operators: List<Operator>): Boolean {
    for (chain in operatorChains(operators, equation.input.size - 1)) {
        if (correct(equation, chain)) {
            return true
        }
    }
    return false
}

fun correct(equation: Equation, chain: List<Operator>): Boolean {
    var result = equation.input[0]
    for (index in 0..<equation.input.size - 1) {
        val operator = chain[index]
        val nextInputValue = equation.input[index + 1]
        result = operator.apply(result, nextInputValue)
    }
    return result == equation.result
}
