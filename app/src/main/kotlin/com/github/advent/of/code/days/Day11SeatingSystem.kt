package com.github.advent.of.code.days

import com.github.advent.of.code.Executable
import java.lang.Exception
import kotlin.math.floor

class Day11SeatingSystem: Executable {
    override fun executePartOne(input: String): Any {
        return playGameOfLife(
            grid = input,
            switchToOccupied = 0,
            switchToEmpty = 4
        ) { state, x, y -> neighbors(state, x, y) }
    }

    override fun executePartTwo(input: String): Any {
        return playGameOfLife(
            grid = input,
            switchToOccupied = 0,
            switchToEmpty = 5
        ) { state, x, y -> advancedNeighbors(state, x, y) }
    }

    private fun playGameOfLife(
        grid: String,
        switchToOccupied: Int,
        switchToEmpty: Int,
        howManyNeighbors: (List<StringBuilder>, Int, Int) -> Int
    ): Int {
        val width = grid.lines().first().length
        val seats = grid.lines().sumOf { it.length }

        var state = grid.lines().map { StringBuilder(it) }
        val nextState = copy(state)

        do {
            state = copy(nextState)

            for (i in 0 until seats) {
                val x = i % width
                val y = floor(i / width.toDouble()).toInt()
                val seat = state[y][x]
                val neighbors = howManyNeighbors(state, x, y)

                val nextSeat = when(seat) {
                    'L' -> if (neighbors == switchToOccupied) '#' else seat
                    '#' -> if (neighbors >= switchToEmpty) 'L' else seat
                    else -> seat
                }

                nextState[y][x] = nextSeat
            }
        } while (notEquals(state, nextState))

        return state.sumOf { it.count { it == '#' } }
    }

    private fun neighbors(state: List<StringBuilder>, x: Int, y: Int): Int {
        val neighborsPos = listOf(
            x     to y - 1,
            x + 1 to y - 1,
            x + 1 to y,
            x + 1 to y + 1,
            x     to y + 1,
            x - 1 to y + 1,
            x - 1 to y,
            x - 1 to y - 1
        )

        return neighborsPos.sumOf {
            tryBlock {
                if (state[it.second][it.first] == '#') 1 else 0
            }
        }
    }

    private fun advancedNeighbors(state: List<StringBuilder>, x: Int, y: Int): Int {
        val neighborsPos = listOf(
            NeighborPos(x, y, Operation.NOOP, Operation.DECR),
            NeighborPos(x, y, Operation.INCR, Operation.DECR),
            NeighborPos(x, y, Operation.INCR, Operation.NOOP),
            NeighborPos(x, y, Operation.INCR, Operation.INCR),
            NeighborPos(x, y, Operation.NOOP, Operation.INCR),
            NeighborPos(x, y, Operation.DECR, Operation.INCR),
            NeighborPos(x, y, Operation.DECR, Operation.NOOP),
            NeighborPos(x, y, Operation.DECR, Operation.DECR),
        )

        return neighborsPos.sumOf {
            tryBlock {
                while (true) {
                    it.executeOp()
                    val seat = state[it.y][it.x]
                    if (seat == '#') return@tryBlock 1 else if (seat == 'L') return@tryBlock 0
                }
                0
            }
        }
    }

    private fun tryBlock(code: () -> Int): Int {
        return try {
            code()
        } catch (e: Exception) {
            0
        }
    }

    private data class NeighborPos(
        var x: Int,
        var y: Int,
        val opOnX: Operation,
        val opOnY: Operation
    ) {
        fun executeOp() {
            x = when(opOnX) {
                Operation.NOOP -> x
                Operation.INCR -> x + 1
                Operation.DECR -> x - 1
            }

            y = when(opOnY) {
                Operation.NOOP -> y
                Operation.INCR -> y + 1
                Operation.DECR -> y - 1
            }
        }
    }

    private enum class Operation {
        NOOP,
        INCR,
        DECR
    }

    private fun copy(input: List<StringBuilder>): List<StringBuilder> {
        return input.toList().map { StringBuilder(it.toString()) }
    }

    private fun notEquals(a: List<StringBuilder>, b: List<StringBuilder>): Boolean {
        return a.toList().map { it.toString() } != b.toList().map { it.toString() }
    }
}
