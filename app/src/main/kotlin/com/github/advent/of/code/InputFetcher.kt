package com.github.advent.of.code

import khttp.get
import java.io.File

object InputFetcher {
    private val cookies = File("cookies").readText()

    fun fetch(day: Int): String {
        val url = "https://adventofcode.com/2020/day/$day/input"
        return get(url, cookies = mapOf("session" to cookies)).text
    }
}
