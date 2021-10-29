package com.github.advent.of.code.days

import com.github.advent.of.code.Executable
import java.lang.IllegalArgumentException
import kotlin.math.absoluteValue
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

class Day12RainRisk: Executable {
    override fun executePartOne(input: String): Any {
        val ship = ShipImpl()
        moveShip(ship, input.lines())
        return ship.manhattanDistance()
    }

    override fun executePartTwo(input: String): Any {
        val ship = ShipWithWaypointImpl()
        moveShip(ship, input.lines())
        return ship.manhattanDistance()
    }

    private fun moveShip(ship: Ship, instructions: List<String>) {
        instructions.forEach { instruction ->
            val isTurn = instruction.first() in Turn.values().map { it.value }
            val isMove = instruction.first() in Orientation.values().map { it.value }
            val isForward = instruction.first() == 'F'

            if (isTurn) {
                val turn = Turn.fromString(instruction.first())
                val value = instruction.substring(1).toInt()
                ship.turn(value, turn)
            } else if (isMove) {
                val orientation = Orientation.fromString(instruction.first())
                val value = instruction.substring(1).toInt()
                ship.move(value, orientation)
            } else if (isForward) {
                val value = instruction.substring(1).toInt()
                ship.forward(value)
            } else {
                throw IllegalArgumentException("uh oh")
            }
        }
    }

    private class ShipImpl: Ship {
        private var shipOrientation: Orientation = Orientation.EAST
        private var x: Int = 0 // EAST + / WEST -
        private var y: Int = 0 // NORTH - / SOUTH +

        override fun turn(value: Int, turn: Turn) {
            val orientationChange = value / 90

            shipOrientation = when(turn) {
                Turn.LEFT -> Orientation.values()[Math.floorMod((shipOrientation.ordinal - orientationChange), Orientation.values().size)]
                Turn.RIGHT -> Orientation.values()[(shipOrientation.ordinal + orientationChange) % Orientation.values().size]
            }
        }

        override fun move(value: Int, orientation: Orientation) {
            when(orientation) {
                Orientation.NORTH -> y -= value
                Orientation.EAST -> x += value
                Orientation.SOUTH -> y += value
                Orientation.WEST -> x -= value
            }
        }

        override fun forward(value: Int) {
            move(value, shipOrientation)
        }

        override fun manhattanDistance(): Int {
            return x.absoluteValue + y.absoluteValue
        }
    }

    private class ShipWithWaypointImpl: Ship {
        private var waypointX: Int = 10 // EAST + / WEST -
        private var waypointY: Int = -1 // NORTH - / SOUTH +

        private var shipX: Int = 0 // EAST + / WEST -
        private var shipY: Int = 0 // NORTH - / SOUTH +

        override fun turn(value: Int, turn: Turn) {
            val orientationChange = value / 90
            val sign = if (turn == Turn.RIGHT) -1 else 1
            val theta = sign * orientationChange * 0.5 * Math.PI

            val tempX = waypointX
            val tempY = waypointY

            waypointX = (tempX * cos(theta) + tempY * sin(theta)).roundToInt()
            waypointY = (-tempX * sin(theta) + tempY * cos(theta)).roundToInt()
        }

        override fun move(value: Int, orientation: Orientation) {
            when(orientation) {
                Orientation.NORTH -> waypointY -= value
                Orientation.EAST -> waypointX += value
                Orientation.SOUTH -> waypointY += value
                Orientation.WEST -> waypointX -= value
            }
        }

        override fun forward(value: Int) {
            shipX += (waypointX * value)
            shipY += (waypointY * value)
        }

        override fun manhattanDistance(): Int {
            return shipX.absoluteValue + shipY.absoluteValue
        }
    }

    private interface Ship {
        fun turn(value: Int, turn: Turn)
        fun move(value: Int, orientation: Orientation)
        fun forward(value: Int)
        fun manhattanDistance(): Int
    }

    private enum class Orientation(val value: Char) {
        NORTH('N'),
        EAST('E'),
        SOUTH('S'),
        WEST('W');

        companion object {
            fun fromString(value: Char): Orientation {
                return values().find { it.value == value } ?:
                    throw IllegalArgumentException("Invalid orientation: $value")
            }
        }
    }

    private enum class Turn(val value: Char) {
        LEFT('L'),
        RIGHT('R');

        companion object {
            fun fromString(value: Char): Turn {
                return values().find { it.value == value } ?:
                    throw IllegalArgumentException("Invalid turn: $value")
            }
        }
    }
}
