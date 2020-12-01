package com.github.advent.of.code

import com.github.advent.of.code.days.Day01ReportRepair

object DayFetcher {
    fun fetch(day: Int): Executable {
        return when (day) {
            1 -> Day01ReportRepair()
            else -> throw NotImplementedError()
        }
    }
}
