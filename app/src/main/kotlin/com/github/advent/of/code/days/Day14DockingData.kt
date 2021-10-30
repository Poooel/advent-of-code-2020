package com.github.advent.of.code.days

import com.github.advent.of.code.Executable
import java.math.BigInteger
import kotlin.math.pow

class Day14DockingData: Executable {
    override fun executePartOne(input: String): Any {
        val program = input.lines()
        return executeDockingProgram(program) { address, value, mask, memory ->
            memory[address] = convertToLong(applyMask(convertToBitString(value), mask, 'X'))
        }
    }

    override fun executePartTwo(input: String): Any {
        val program = input.lines()
        return executeDockingProgram(program) { address, value, mask, memory ->
            val addressFloating = applyMask(convertToBitString(address), mask, '0')
            val addresses = resolveFloatingBits(addressFloating)
            addresses.forEach { memory[convertToLong(it)] = value }
        }
    }

    private fun executeDockingProgram(program: List<String>, writeToMemory: (Long, Long, String, MutableMap<Long, Long>) -> Unit): Long {
        var mask = "X".repeat(36)
        val memory = mutableMapOf<Long, Long>()

        program.forEach { programLine ->
            if (programLine.startsWith("mask = ")) {
                mask = programLine.removePrefix("mask = ")
            } else {
                val (memoryAddressRaw, memoryValueRaw) = programLine.split(" = ")
                val memoryAddress = memoryAddressRaw.removePrefix("mem[").dropLast(1).toLong()
                val memoryValue = memoryValueRaw.toLong()

                writeToMemory(memoryAddress, memoryValue, mask, memory)
            }
        }

        return memory.map { it.value }.sum()
    }

    private fun applyMask(value: String, mask: String, unchangedBit: Char): String {
        val maskApplied = StringBuilder(value)

        value.forEachIndexed { index, _ ->
            if (mask[index] == unchangedBit) {
                maskApplied[index] = value[index]
            } else {
                maskApplied[index] = mask[index]
            }
        }

        return maskApplied.toString()
    }

    private fun convertToBitString(value: Long): String {
        val bigInteger = BigInteger.valueOf(value)
        return bigInteger.toString(2).padStart(36, '0')
    }

    private fun convertToLong(value: String): Long {
        val bigInteger = BigInteger(value, 2)
        return bigInteger.toLong()
    }

    private fun resolveFloatingBits(value: String): List<String> {
        val possibleCombinations = mutableListOf<List<String>>()

        value.forEachIndexed { index, bit ->
            if (bit == 'X') {
                possibleCombinations.add(
                    listOf(
                        value.replaceAt(index, "0"),
                        value.replaceAt(index, "1")
                    )
                )
            }
        }

        return mergeCombinations(getCombinations(possibleCombinations))
    }

    private fun getCombinations(values: List<List<String>>): List<List<String>> {
        val numberOfCombinations = 2.toDouble().pow(values.size).toInt()

        return (0 until numberOfCombinations).map {
            val cursorBit = BigInteger.valueOf(it.toLong()).toString(2).padStart(values.size, '0')
            val combination = mutableListOf<String>()
            cursorBit.forEachIndexed { index, bit ->
                combination.add(values[index][bit.toString().toInt()])
            }
            combination
        }
    }

    private fun mergeCombinations(values: List<List<String>>): List<String> {
        return values.map { combinations ->
            val mergedCombination = StringBuilder()

            combinations.first().forEachIndexed { index, _ ->
                val chars = combinations.map { it[index] }

                if (chars.all { it == chars.first() }) {
                    mergedCombination.append(chars.first())
                } else {
                    val bitNotFloating = chars.find { it != 'X' }!!
                    mergedCombination.append(bitNotFloating)
                }
            }

            mergedCombination.toString()
        }
    }
}

fun String.replaceAt(index: Int, replacement: String): String {
    return this.replaceRange(index, index + 1, replacement)
}
