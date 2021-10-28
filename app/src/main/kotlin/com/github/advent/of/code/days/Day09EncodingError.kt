package com.github.advent.of.code.days

import com.github.advent.of.code.Executable
import com.google.common.collect.Sets
import java.lang.IllegalArgumentException

class Day09EncodingError: Executable {
    override fun executePartOne(input: String): Any {
        val numbers = input.trim().lines().map { it.toLong() }
        return findFirstIncorrectNumber(numbers = numbers, preambleSize = 25, windowSize = 25)
    }

    override fun executePartTwo(input: String): Any {
        val numbers = input.trim().lines().map { it.toLong() }
        val incorrectNumber = findFirstIncorrectNumber(numbers = numbers, preambleSize = 25, windowSize = 25)
        val contiguousSetForSum = findContiguousSetForSum(target = incorrectNumber, numbers = numbers)
        return contiguousSetForSum.minOrNull()!! + contiguousSetForSum.maxOrNull()!!
    }

    private fun findFirstIncorrectNumber(numbers: List<Long>, preambleSize: Int, windowSize: Int): Long {
        val windowsToEvaluate = numbers.windowed(windowSize + 1, 1, false)

        windowsToEvaluate.forEach { window ->
            val windowToEvaluate = window.subList(fromIndex = 0, toIndex = window.size - 1)
            val target = window.last()

            if (!sumOfTwo(target = target, numbers = windowToEvaluate)) {
                return target
            }
        }

        throw IllegalArgumentException("Couldn't find incorrect number in list.")
    }

    private fun sumOfTwo(target: Long, numbers:List<Long>): Boolean {
        val combinations = Sets.combinations(numbers.toSet(), 2)
        return combinations.any { it.sum() == target }
    }

    private fun findContiguousSetForSum(target: Long, numbers:List<Long>): List<Long> {
        var windowSize = 5

        while(true) {
            val windowsToEvaluate = numbers.windowed(windowSize, 1, false)

            windowsToEvaluate.forEach{ window ->
                val windowSum = window.sum()

                if (windowSum == target) {
                    return window
                }
            }

            windowSize++
        }
    }
}
