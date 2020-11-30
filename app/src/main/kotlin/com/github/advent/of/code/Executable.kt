package com.github.advent.of.code

interface Executable {
    fun execute(part: Int, input: String) {
        if (part == 1) {
            executePartOne(input)
        } else {
            executePartTwo(input)
        }
    }

    fun executePartOne(input: String)
    fun executePartTwo(input: String)
}
