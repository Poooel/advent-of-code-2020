package com.github.advent.of.code.days

import com.github.advent.of.code.Executable
import kotlin.math.ceil
import kotlin.math.floor

class Day05BinaryBoarding: Executable {
    override fun executePartOne(input: String): Any {
        return input.trim().split("\n").maxOf {
            val row = binarySearch(0, 127, 'F', 'B', it.take(7))
            val column = binarySearch(0, 7, 'L', 'R', it.drop(7))

            (row * 8) + column
        }
    }

    override fun executePartTwo(input: String): Any {
        val seat = input.trim().split("\n").map {
            val row = binarySearch(0, 127, 'F', 'B', it.take(7))
            val column = binarySearch(0, 7, 'L', 'R', it.drop(7))

            row to column
        }
            .groupBy { it.first }
            .map { it.key to it.value.sumBy { it.second } }
            .first { it.second != 28 }

        return (seat.first * 8) + (28 - seat.second)
    }

    private fun binarySearch(startValue: Int, endValue: Int, lower: Char, upper: Char, sequence: String): Int {
        val middleValue = (startValue.toDouble() + endValue.toDouble()) / 2

        return if (sequence.first() == lower) {
            if (sequence.length == 1) {
                startValue
            } else {
                binarySearch(startValue, floor(middleValue).toInt(), lower, upper, sequence.drop(1))
            }
        } else if (sequence.first() == upper) {
            if (sequence.length == 1) {
                endValue
            } else {
                binarySearch(ceil(middleValue).toInt(), endValue, lower, upper, sequence.drop(1))
            }
        } else {
            -1
        }
    }
}
