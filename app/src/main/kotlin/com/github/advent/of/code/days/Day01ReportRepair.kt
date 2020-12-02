package com.github.advent.of.code.days

import com.github.advent.of.code.Executable

class Day01ReportRepair: Executable {
    override fun executePartOne(input: String): Any {
        val arrayOfInts = input.trim().split("\n").map { it.toInt() }

        for (x in arrayOfInts) {
            for (y in arrayOfInts) {
                if (x + y == 2020) {
                    return (x * y)
                }
            }
        }

        return -1
    }

    override fun executePartTwo(input: String): Any {
        val arrayOfInts = input.trim().split("\n").map { it.toInt() }

        for (x in arrayOfInts) {
            for (y in arrayOfInts) {
                for (z in arrayOfInts) {
                    if (x + y + z == 2020) {
                        return (x * y * z)
                    }
                }
            }
        }

        return -1
    }
}
