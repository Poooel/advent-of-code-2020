package com.github.advent.of.code.days

import com.github.advent.of.code.Executable
import java.lang.Exception
import java.lang.IllegalArgumentException

class Day17ConwayCubes: Executable {
    override fun executePartOne(input: String): Any {
        return playGameOfLife3D(input, 6)
    }

    override fun executePartTwo(input: String): Any {
        return playGameOfLife4D(input, 6)
    }

    private fun playGameOfLife3D(
        grid: String,
        cycles: Int
    ): Int {
        var state = initializeGameState3D(grid, cycles)
        val nextState = copy3D(state)

        for (i in 0..cycles) {
            state = copy3D(nextState)

            for (j in state.indices) {
                for (k in state[j].indices) {
                    for (l in state[j][k].indices) {
                        val cube = state[j][k][l]
                        val neighbors = neighbors3D(state, l, k, j)

                        val nextCubeState = when(cube) {
                            '#' -> if (neighbors == 2 || neighbors == 3) cube else '.'
                            '.' -> if (neighbors == 3) '#' else cube
                            else -> throw IllegalArgumentException("uh oh")
                        }

                        nextState[j][k][l] = nextCubeState
                    }
                }
            }
        }

        return state.sumOf { it.sumOf { it.count { it == '#' } } }
    }

    private fun neighbors3D(state: List<List<StringBuilder>>, x: Int, y: Int, z: Int): Int {
        val neighborsPos = sequence {
            (-1..1).forEach { xOffset ->
                (-1..1).forEach { yOffset ->
                    (-1..1).forEach { zOffset ->
                        yield(Triple(x + xOffset, y + yOffset, z + zOffset))
                    }
                }
            }
        }

        return neighborsPos.filter { it != Triple(x, y, z) }.sumOf {
            tryBlock {
                val (x, y, z) = it
                if (state[z][y][x] == '#') 1 else 0
            }
        }
    }

    private fun initializeGameState3D(grid: String, cycles: Int): List<List<StringBuilder>> {
        val x = grid.lines().size + (cycles * 2)
        val y = grid.lines().size + (cycles * 2)
        val z = 1 + (cycles * 2)

        val initialGameState = (0 until z).map {
            (0 until y).map {
                StringBuilder(".".repeat(x))
            }
        }

        val xOffset = (x - grid.lines().size) / 2
        val yOffset = (y - grid.lines().size) / 2
        val zOffset = (z - 1) / 2

        grid.lines().forEachIndexed { y, line ->
            line.forEachIndexed { x, cube ->
                initialGameState[0 + zOffset][y + yOffset][x + xOffset] = cube
            }
        }

        return initialGameState
    }

    private fun copy3D(input: List<List<StringBuilder>>): List<List<StringBuilder>> {
        return input.toList().map { it.toList().map { StringBuilder(it.toString()) } }
    }

    private fun playGameOfLife4D(
        grid: String,
        cycles: Int
    ): Int {
        var state = initializeGameState4D(grid, cycles)
        val nextState = copy4D(state)

        for (i in 0..cycles) {
            state = copy4D(nextState)

            for (j in state.indices) {
                for (k in state[j].indices) {
                    for (l in state[j][k].indices) {
                        for (m in state[j][k][l].indices) {
                            val cube = state[j][k][l][m]
                            val neighbors = neighbors4D(state, m, l, k, j)

                            val nextCubeState = when(cube) {
                                '#' -> if (neighbors == 2 || neighbors == 3) cube else '.'
                                '.' -> if (neighbors == 3) '#' else cube
                                else -> throw IllegalArgumentException("uh oh")
                            }

                            nextState[j][k][l][m] = nextCubeState
                        }
                    }
                }
            }
        }

        return state.sumOf { it.sumOf { it.sumOf { it.count { it == '#' } } } }
    }

    private fun neighbors4D(state: List<List<List<StringBuilder>>>, x: Int, y: Int, z: Int, w: Int): Int {
        val neighborsPos = sequence {
            (-1..1).forEach { xOffset ->
                (-1..1).forEach { yOffset ->
                    (-1..1).forEach { zOffset ->
                        (-1..1).forEach { wOffset ->
                            yield(Quadruple(x + xOffset, y + yOffset, z + zOffset, w + wOffset))
                        }
                    }
                }
            }
        }

        return neighborsPos.filter { it != Quadruple(x, y, z, w) }.sumOf {
            tryBlock {
                val (x, y, z, w) = it
                if (state[w][z][y][x] == '#') 1 else 0
            }
        }
    }

    private fun initializeGameState4D(grid: String, cycles: Int): List<List<List<StringBuilder>>> {
        val x = grid.lines().size + (cycles * 2)
        val y = grid.lines().size + (cycles * 2)
        val z = 1 + (cycles * 2)
        val w = 1 + (cycles * 2)

        val initialGameState = (0 until w).map {
            (0 until z).map {
                (0 until y).map {
                    StringBuilder(".".repeat(x))
                }
            }
        }

        val xOffset = (x - grid.lines().size) / 2
        val yOffset = (y - grid.lines().size) / 2
        val zOffset = (z - 1) / 2
        val wOffset = (w - 1) / 2

        grid.lines().forEachIndexed { y, line ->
            line.forEachIndexed { x, cube ->
                initialGameState[0 + wOffset][0 + zOffset][y + yOffset][x + xOffset] = cube
            }
        }

        return initialGameState
    }

    private fun copy4D(input: List<List<List<StringBuilder>>>): List<List<List<StringBuilder>>> {
        return input.toList().map { it.toList().map { it.toList().map { StringBuilder(it.toString()) } } }
    }

    private fun tryBlock(code: () -> Int): Int {
        return try {
            code()
        } catch (e: Exception) {
            0
        }
    }

    private data class Quadruple<out A, out B, out C, out D>(
        val first: A,
        val second: B,
        val third: C,
        val fourth: D
    )
}
