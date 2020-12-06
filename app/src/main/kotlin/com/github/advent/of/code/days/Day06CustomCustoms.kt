package com.github.advent.of.code.days

import com.github.advent.of.code.Executable

class Day06CustomCustoms: Executable {
    override fun executePartOne(input: String): Any {
        return input.trim().split("\n\n").map {
            it.split("\n").map {
                it.chunked(1)
            }
                .flatten()
                .distinct()
        }
            .sumBy { it.count() }
    }

    override fun executePartTwo(input: String): Any {
        return input.trim().split("\n\n").map {
            it.split("\n").map {
                it.chunked(1)
            }
        }
            .map {
                it.reduce { acc, list ->
                    acc.intersect(list).toList()
                }
            }
            .sumBy { it.count() }
    }
}
