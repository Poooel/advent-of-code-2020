package com.github.advent.of.code.days

import com.github.advent.of.code.Executable

class Day10AdapterArray: Executable {
    override fun executePartOne(input: String): Any {
        val joltAdapters = input.trim().lines().map { it.toInt() }
        val joltDifferences = findJoltDifferences(joltAdapters)
        return joltDifferences[1]!! * joltDifferences[3]!!
    }

    override fun executePartTwo(input: String): Any {
        val joltAdapters = input.trim().lines().map { it.toInt() }
        return findHowManyDifferentArrangementsToGetToLast(joltAdapters)
    }

    private fun findJoltDifferences(joltAdapters: List<Int>): Map<Int, Int> {
        val joltDifferences = mutableListOf<Int>()
        val completeJoltAdapters = joltAdapters + 0 + (joltAdapters.maxOrNull()!! + 3)
        val sortedJoltAdapters = completeJoltAdapters.sorted()

        for (i in 1 until sortedJoltAdapters.size) {
            joltDifferences.add(sortedJoltAdapters[i] - sortedJoltAdapters[i - 1])
        }

        return joltDifferences.groupBy { it }.mapValues { it.value.size }
    }

    private fun findHowManyDifferentArrangementsToGetToLast(joltAdapters: List<Int>): Long {
        val longJoltAdapters = joltAdapters.map { it.toLong() }
        val completeJoltAdapters = longJoltAdapters + (longJoltAdapters.maxOrNull()!! + 3)
        val sortedJoltAdapters = completeJoltAdapters.sorted()

        val arrangements = mutableMapOf(0L to 1L)

        sortedJoltAdapters.forEach { joltAdapter ->
            arrangements[joltAdapter] = (arrangements[joltAdapter - 1] ?: 0) + (arrangements[joltAdapter - 2] ?: 0) + (arrangements[joltAdapter - 3] ?: 0)
        }

        return arrangements[arrangements.maxOf { it.key }]!!
    }
}
