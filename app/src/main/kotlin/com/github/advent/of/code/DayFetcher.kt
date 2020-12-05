package com.github.advent.of.code

import com.github.advent.of.code.days.Day01ReportRepair
import com.github.advent.of.code.days.Day02PasswordPhilosophy
import com.github.advent.of.code.days.Day03TobogganTrajectory
import com.github.advent.of.code.days.Day04PassportProcessing

object DayFetcher {
    fun fetch(day: Int): Executable {
        return when (day) {
            1 -> Day01ReportRepair()
            2 -> Day02PasswordPhilosophy()
            3 -> Day03TobogganTrajectory()
            4 -> Day04PassportProcessing()
            else -> throw NotImplementedError()
        }
    }
}
