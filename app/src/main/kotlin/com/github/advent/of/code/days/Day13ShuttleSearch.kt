package com.github.advent.of.code.days

import com.github.advent.of.code.Executable

class Day13ShuttleSearch: Executable {
    override fun executePartOne(input: String): Any {
        val (target, buses) = input.lines()
        val validBuses = buses.split(",").filter { it != "x" }
        val earliestStops = validBuses.associateWith { getClosestStop(target.toLong(), it.toLong()) }
        val earliestStop = earliestStops.minByOrNull { it.value }!!
        return earliestStop.key.toLong() * (earliestStop.value - target.toLong())
    }

    override fun executePartTwo(input: String): Any {
        TODO("Not yet implemented")
    }

    private fun getClosestStop(target: Long, busId: Long): Long {
        val stopSequence = generateSequence(0L) { it + busId }
        return stopSequence.filter { it >= target }.take(1).first()
    }
}
