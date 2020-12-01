package com.github.advent.of.code

interface Executable {
    fun execute(part: Int, input: String): String {
        return if (part == 1) {
            executePartOne(input)
        } else {
            executePartTwo(input)
        }
    }

    fun executePartOne(input: String): String
    fun executePartTwo(input: String): String
}
