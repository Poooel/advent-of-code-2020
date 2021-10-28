package com.github.advent.of.code.days

import com.github.advent.of.code.Executable
import java.lang.IllegalArgumentException
import java.util.*

class Day08HandheldHalting: Executable {
    override fun executePartOne(input: String): Any {
        val instructions = parseInput(
            input.trim().lines()
        )
        return runInstructions(instructions = instructions).value
    }

    override fun executePartTwo(input: String): Any {
        val instructions = parseInput(
            input.trim().lines()
        )
        return findLoopingInstruction(instructions = instructions).value
    }

    private fun runInstructions(instructions: List<Instruction>): Result {
        var cursor = 0
        var accumulator = 0
        val instructionsRan = mutableSetOf<Instruction>()

        while (true) {
            if (cursor >= instructions.size) {
                return Result(value = accumulator, loop = false)
            }

            val instruction = instructions[cursor]

            if (instructionsRan.contains(instruction)) {
                return Result(value = accumulator, loop = true)
            } else {
                instructionsRan.add(instruction)
            }

            when(instruction.operation) {
                Operation.NO_OPERATION -> {
                    cursor++
                }
                Operation.ACCUMULATOR -> {
                    accumulator += instruction.argument
                    cursor++
                }
                Operation.JUMP -> {
                    cursor += instruction.argument
                }
            }
        }
    }

    private fun findLoopingInstruction(instructions: List<Instruction>): Result {
        val jumpOrNoopInstructions = instructions
            .filter { it.operation == Operation.JUMP || it.operation == Operation.NO_OPERATION }
            .reversed()
            .toMutableList()

        val result = runInstructions(instructions)

        if (!result.loop) {
            return result
        } else {
            while (jumpOrNoopInstructions.isNotEmpty()) {
                val instructionToChange = jumpOrNoopInstructions.removeFirst()
                val indexOfLastInstructionRan = instructions.indexOf(instructionToChange)
                val newInstruction = instructionToChange.copy(
                    operation = when (instructionToChange.operation) {
                        Operation.JUMP -> {
                            Operation.NO_OPERATION
                        }
                        Operation.NO_OPERATION -> {
                            Operation.JUMP
                        }
                        else -> {
                            throw IllegalArgumentException("Invalid operation.")
                        }
                    }
                )

                val newSetOfInstructions = instructions.toMutableList()
                newSetOfInstructions.removeAt(indexOfLastInstructionRan)
                newSetOfInstructions.add(indexOfLastInstructionRan, newInstruction)

                val internalResult = runInstructions(instructions = newSetOfInstructions)

                if (!internalResult.loop) {
                    return internalResult
                }
            }

            throw IllegalArgumentException("Couldn't find an instruction to swap to break loop.")
        }
    }

    private fun parseInput(input: List<String>): List<Instruction> {
        return input.map { line ->
            val (operation, argument) = line.split(" ")
            Instruction(operation = Operation.fromString(operation), argument = argument.toInt())
        }
    }

    private data class Instruction(
        val operation: Operation,
        val argument: Int,
        val uniqueId: UUID = UUID.randomUUID()
    )

    private enum class Operation(val value: String) {
        NO_OPERATION("nop"),
        ACCUMULATOR("acc"),
        JUMP("jmp");

        companion object {
            fun fromString(value: String): Operation {
                return values().find { it.value == value } ?:
                    throw IllegalArgumentException("Invalid operation: $value")
            }
        }
    }

    private data class Result(
        val value: Int,
        val loop: Boolean
    )
}
