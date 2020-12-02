package com.github.advent.of.code

import com.github.advent.of.code.days.Day01ReportRepair
import com.github.advent.of.code.days.Day02PasswordPhilosophy

object DayFetcher {
    fun fetch(day: Int): Executable {
        return when (day) {
            1 -> Day01ReportRepair()
            2 -> Day02PasswordPhilosophy()
            else -> throw NotImplementedError()
        }
    }
}
