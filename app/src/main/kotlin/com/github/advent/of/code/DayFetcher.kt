package com.github.advent.of.code

import com.github.advent.of.code.days.*

object DayFetcher {
    fun fetch(day: Int): Executable {
        return when (day) {
            1 -> Day01ReportRepair()
            2 -> Day02PasswordPhilosophy()
            3 -> Day03TobogganTrajectory()
            4 -> Day04PassportProcessing()
            5 -> Day05BinaryBoarding()
            6 -> Day06CustomCustoms()
            7 -> Day07HandyHaversacks()
            8 -> Day08HandheldHalting()
            9 -> Day09EncodingError()
            10 -> Day10AdapterArray()
            11 -> Day11SeatingSystem()
            12 -> Day12RainRisk()
            13 -> Day13ShuttleSearch()
            14 -> Day14DockingData()
            else -> throw NotImplementedError()
        }
    }
}
