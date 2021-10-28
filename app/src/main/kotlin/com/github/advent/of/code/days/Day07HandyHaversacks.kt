package com.github.advent.of.code.days

import com.github.advent.of.code.Executable

class Day07HandyHaversacks: Executable {
    override fun executePartOne(input: String): Any {
        val inputLines = input.trim().lines()

        val bags = mutableMapOf<String, Bag>()

        inputLines.forEach { line ->
            parseInputLine(line, bags)
        }

        return findHowManyContain(colorToContain = "shiny gold", bags = bags)
    }

    override fun executePartTwo(input: String): Any {
        val inputLines = input.trim().lines()

        val bags = mutableMapOf<String, Bag>()

        inputLines.forEach { line ->
            parseInputLine(line, bags)
        }

        return findHowManyItContains(bags["shiny gold"]!!)
    }

    private fun findHowManyContain(colorToContain: String, bags: Map<String, Bag>): Int {
        return bags
            .filter { it.key != colorToContain }
            .count { exploreBag(bag = it.value, colorToContain = colorToContain) }
    }

    private fun exploreBag(bag: Bag, colorToContain: String): Boolean {
        return if (bag.bags.isEmpty()) {
            false
        } else if (bag.bags.any { it.key.color == colorToContain }) {
            true
        } else {
            bag.bags.any { exploreBag(bag = it.key, colorToContain = colorToContain) }
        }
    }

    private fun findHowManyItContains(initialBag: Bag): Int {
        return initialBag.bags
            .map { countHowManyItContains(it.key, it.value) }
            .sum()
    }

    private fun countHowManyItContains(bag: Bag, multiplicator: Int): Int {
        return if (bag.bags.isEmpty()) {
            multiplicator
        } else {
            bag.bags
                .map { countHowManyItContains(it.key, it.value * multiplicator) }
                .sum() + multiplicator
        }
    }

    private fun parseInputLine(line: String, existingBags: MutableMap<String, Bag>) {
        val (adjective, color) = line.split(" ")
        val bagColor = "$adjective $color"

        val bags = Regex("\\d( \\w+)+").findAll(line).toList().associate {
            val (innerQuantity, innerAdjective, innerColor) = it.value.split(" ")
            val innerBagColor = "$innerAdjective $innerColor"

            val innerBag = existingBags.getOrPut(innerBagColor) { Bag(bags = emptyMap(), color = innerBagColor) }

            innerBag to innerQuantity.toInt()
        }

        existingBags.compute(bagColor) { k, v ->
            if (v == null) {
                Bag(
                    color = k,
                    bags = bags
                )
            } else {
                v.bags = (v.bags.asSequence() + bags.asSequence())
                    .distinct()
                    .groupBy({it.key}, {it.value})
                    .mapValues { (_, values) -> values.sum() }
                v
            }
        }
    }

    private data class Bag(
        val color: String,
        var bags: Map<Bag, Int>
    )
}
