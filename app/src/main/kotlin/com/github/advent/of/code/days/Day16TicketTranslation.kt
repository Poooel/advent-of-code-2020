package com.github.advent.of.code.days

import com.github.advent.of.code.Executable

class Day16TicketTranslation: Executable {
    override fun executePartOne(input: String): Any {
        val state = parseInput(input)
        return findInvalidTickets(state)
    }

    override fun executePartTwo(input: String): Any {
        val state = parseInput(input)
        val noInvalidTickets = state.copy(nearbyTickets = state.nearbyTickets.filter { verifyInvalidTicket(state, it, 1) == 0 })
        val matchingRulesForEachTicket = collectMatchingRulesForEachTicket(noInvalidTickets)
        val fields = findFields(matchingRulesForEachTicket, state)
        return computeAnswer(state, fields)
    }

    private fun findInvalidTickets(state: State): Int {
        return state.nearbyTickets.sumOf { nearbyTicket ->
            verifyInvalidTicket(state, nearbyTicket)
        }
    }

    private fun verifyInvalidTicket(state: State, ticket: String, incr: Int = 0): Int {
        val elements = ticket.split(",").map { it.toInt() }

        for (element in elements) {
            var invalid = false
            for (rule in state.rules) {
                if (element in rule.firstConstraint || element in rule.secondConstraint) {
                    invalid = false
                    break
                } else {
                    invalid = true
                    continue
                }
            }
            if (invalid) {
                return element + incr
            }
        }

        return 0
    }

    private fun collectMatchingRulesForEachTicket(state: State): List<List<List<Rule>>> {
        return state.nearbyTickets.map { ticket ->
            val elements = ticket.split(",").map { it.toInt() }
            val rulesForTicket = mutableListOf<List<Rule>>()

            for (element in elements) {
                val rulesForElement = mutableListOf<Rule>()
                for (rule in state.rules) {
                    if (element in rule.firstConstraint || element in rule.secondConstraint) {
                        rulesForElement.add(rule)
                    }
                }
                rulesForTicket.add(rulesForElement)
            }

            rulesForTicket
        }
    }

    private fun findFields(matchingRulesForEachTicket: List<List<List<Rule>>>, state: State): Map<Int, Rule> {
        var matchingRulesForEachElements = mutableMapOf<Int, MutableList<List<Rule>>>()
        state.myTicket.split(",").forEachIndexed { index, _ -> matchingRulesForEachElements[index] = mutableListOf() }

        matchingRulesForEachTicket.forEach { matchingRulesForTicket ->
            matchingRulesForTicket.forEachIndexed { index, matchingRulesForElement ->
                matchingRulesForEachElements[index]!!.add(matchingRulesForElement)
            }
        }

        val fields = mutableMapOf<Int, Rule>()

        while (fields.size < state.rules.size) {
            val easiestToGuess = findEasiestElementToGuess(matchingRulesForEachElements)
            val element = easiestToGuess.key
            val matchingRules = easiestToGuess.value

            val allCommonRules = state.rules.filter { rule ->
                matchingRules.all { it.contains(rule) }
            }

            fields[element] = allCommonRules.single()
            matchingRulesForEachElements.remove(element)
            matchingRulesForEachElements = matchingRulesForEachElements.mapValues { it.value.map { it.filter { it != allCommonRules.single() } }.toMutableList() }.toMutableMap()
        }

        return fields
    }

    private fun findEasiestElementToGuess(matchingRulesForEachElements: Map<Int, List<List<Rule>>>): Map.Entry<Int, List<List<Rule>>> {
        val solutionsPerElements = matchingRulesForEachElements.entries.associateWith { matchingRulesForElement ->
            matchingRulesForElement.value.map { matchingRules ->
                matchingRules.size
            }
        }

        return solutionsPerElements.minByOrNull { it.value.average() }!!.key
    }

    private fun computeAnswer(state: State, fields: Map<Int, Rule>): Long {
        val fieldsReversed = fields.entries.associate { (k, v) -> v to k }
        val wantedFields = fieldsReversed.filter { it.key.name.startsWith("departure") }

        return wantedFields.map { (_, element) ->
            state.myTicket.split(",")[element].toLong()
        }.reduce(Long::times)
    }

    private fun parseInput(input: String): State {
        val (rulesSection, myTicketSection, nearbyTicketsSection) = input.split("\n\n")

        val rules = rulesSection.lines().map { rule ->
            val name = rule.split(":").first()
            val constraints = rule.split(": ").last()
            val (firstConstraint, secondConstraint) = constraints.split(" or ")
            val (firstConstraintLow, firstConstraintHigh) = firstConstraint.split("-")
            val (secondConstraintLow, secondConstraintHigh) = secondConstraint.split("-")

            Rule(
                name = name,
                firstConstraint = IntRange(firstConstraintLow.toInt(), firstConstraintHigh.toInt()),
                secondConstraint = IntRange(secondConstraintLow.toInt(), secondConstraintHigh.toInt())
            )
        }

        val myTicket = myTicketSection.lines().last()
        val nearbyTickets = nearbyTicketsSection.lines().drop(1)

        return State(
            rules = rules,
            myTicket = myTicket,
            nearbyTickets = nearbyTickets
        )
    }

    private data class Rule(
        val name: String,
        val firstConstraint: IntRange,
        val secondConstraint: IntRange
    )

    private data class State(
        val rules: List<Rule>,
        val myTicket: String,
        val nearbyTickets: List<String>
    )
}
