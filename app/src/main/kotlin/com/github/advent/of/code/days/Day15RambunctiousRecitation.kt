package com.github.advent.of.code.days

import com.github.advent.of.code.Executable

class Day15RambunctiousRecitation: Executable {
    override fun executePartOne(input: String): Any {
        return playGame(input, 2020)
    }

    override fun executePartTwo(input: String): Any {
        return playGame(input, 30_000_000)
    }

    private fun playGame(initialState: String, playUntil: Int): Int {
        val memory = mutableMapOf<Int, MutableList<Int>>()
        initialState.split(",").forEachIndexed { turn, number -> memory[number.toInt()] = mutableListOf(turn + 1) }
        var lastNumberSpoken = initialState.last().toString().toInt()
        val nextStep = initialState.split(",").size

        (nextStep until playUntil).map { turn ->
            if (memory[lastNumberSpoken] == null) {
                memory[lastNumberSpoken] = mutableListOf(turn + 1)
                lastNumberSpoken = 0
            } else {
                lastNumberSpoken = if (memory[lastNumberSpoken]!!.size == 1) {
                    0
                } else {
                    val (a, b) = memory[lastNumberSpoken]!!.takeLast(2)
                    b - a
                }

                memory.compute(lastNumberSpoken) { k, v ->
                    if (v == null) {
                        mutableListOf(turn + 1)
                    } else {
                        v.add(turn + 1)
                        v
                    }
                }
            }
        }

        return lastNumberSpoken
    }
}
