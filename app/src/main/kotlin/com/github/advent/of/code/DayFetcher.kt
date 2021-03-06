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
            else -> throw NotImplementedError()
        }
    }
}
