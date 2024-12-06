package day5

import java.io.File

const val inputFileName = "input"

fun main() {
    val (rulesSection, updatesSection) = readSections()
    val rules = parseRules(rulesSection)
    val updates = parseUpdates(updatesSection)
    val groupedUpdates = updates.groupBy { update -> rules.updateIsCorrect(update) }

    val correctUpdates = groupedUpdates[true]!!
    val resultA = correctUpdates.fold(0) { sum, update ->
        if (update.size % 2 != 1) {
            throw IllegalArgumentException("Found an even-length line.")
        }
        sum + update[update.size / 2]
    }
    println("A: $resultA")

    val incorrectUpdates = groupedUpdates[false]!!
    val correctedUpdates = correctUpdate(incorrectUpdates, rules)
    val resultB = correctedUpdates.fold(0) { sum, update ->
        if (update.size % 2 != 1) {
            throw IllegalArgumentException("Found an even-length line.")
        }
        sum + update[update.size / 2]
    }
    println("B: $resultB")
}

private fun readSections(): Pair<List<String>, List<String>> {
    val lines = File(inputFileName)
        .readLines()
    val emptyLineIndex = lines.indexOf("")
    val rulesSection = lines.subList(0, emptyLineIndex)
    val updatesSection = lines.subList(emptyLineIndex + 1, lines.size)
    return Pair(rulesSection, updatesSection)
}

val ruleLineRegex = """(\d+)\|(\d+)""".toRegex()

fun parseRules(lines: List<String>): Rules {
    val relations = lines.map { line ->
        val match = ruleLineRegex.matchEntire(line)!!
        Pair(
            match.groups[1]!!.value.toInt(),
            match.groups[2]!!.value.toInt()
        )
    }
    return Rules(relations)
}

class Rules(relations: List<Pair<Int, Int>>) {
    val earlierPagesForPage = mutableMapOf<Int, MutableSet<Int>>()

    init {
        for (pair in relations) {
            val earlierPages = earlierPagesForPage
                .computeIfAbsent(pair.second) { mutableSetOf() }
            earlierPages += pair.first
        }
    }

    fun updateIsCorrect(update: List<Int>): Boolean {
        for (firstNumberIndex in 0..<update.size - 1) {
            val firstNumber = update[firstNumberIndex]
            val earlierPages = earlierPagesForPage.get(firstNumber)
            if (earlierPages != null) {
                for (secondNumberIndex in firstNumberIndex..<update.size) {
                    val secondNumber = update[secondNumberIndex]
                    if (earlierPages.contains(secondNumber)) {
                        return false
                    }
                }
            }
        }
        return true
    }
}

fun parseUpdates(lines: List<String>): List<List<Int>> {
    return lines.map { line ->
        line.split(',')
            .map { n -> n.toInt() }
    }
}

private fun correctUpdate(
    incorrectUpdates: List<List<Int>>,
    rules: Rules
): List<List<Int>> {
    val correctedUpdates = incorrectUpdates.map { update ->
        val result = update.sortedWith { a, b ->
            when {
                rules.earlierPagesForPage[a]?.contains(b) == true -> 1
                rules.earlierPagesForPage[b]?.contains(a) == true -> -1
                else -> 0
            }
        }
        if (result == update) {
            throw IllegalStateException("Result is equal to update assumed to be incorrect")
        }
        result
    }
    return correctedUpdates
}
