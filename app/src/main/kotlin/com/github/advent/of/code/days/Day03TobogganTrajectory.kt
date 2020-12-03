package com.github.advent.of.code.days

import com.github.advent.of.code.Executable

class Day03TobogganTrajectory: Executable {
    override fun executePartOne(input: String): Any {
        val parsedInput = input.trim().split("\n")

        return howManyTreesDoYouHit(repeatInput(parsedInput, 3, 1), 3, 1)
    }

    override fun executePartTwo(input: String): Any {
        val parsedInput = input.trim().split("\n")
        val slopes = listOf(Pair(1, 1), Pair(3, 1), Pair(5, 1), Pair(7, 1), Pair(1, 2))

        return slopes.map {
            howManyTreesDoYouHit(repeatInput(parsedInput, it.first, it.second), it.first, it.second).toLong()
        }.reduce { accumulator, i ->
            accumulator * i
        }
    }

    private fun repeatInput(input: List<String>, right: Int, down: Int): List<String> {
        val fullWidth = (input.size * right) / down
        val repeatInput = (fullWidth / input.first().length) + 1

        return input.map {
            it.repeat(repeatInput)
        }
    }

    private fun howManyTreesDoYouHit(input: List<String>, right: Int, down: Int): Int {
        var x = right
        var y = down
        var trees = 0

        while (y < input.size) {
            if (input[y].elementAt(x) == '#') {
                trees++
            }
            x += right
            y += down
        }

        return trees
    }
}
