package com.github.advent.of.code

import com.github.kittinunf.fuel.core.Headers
import com.github.kittinunf.fuel.httpGet
import java.io.File

object InputFetcher {
    private val cookies = File("cookies").readText()

    fun fetch(day: Int): String {
        val (_, _, result) = "https://adventofcode.com/2020/day/$day/input"
            .httpGet()
            .header(Headers.COOKIE to "session=$cookies")
            .responseString()

        return result.get()
    }
}
