package com.github.advent.of.code.days

import com.github.advent.of.code.Executable

class Day02PasswordPhilosophy: Executable {
    override fun executePartOne(input: String): Any {
        val passwordPolicies = buildPasswordPolicies(input.trim().split("\n"))
        return passwordPolicies.count { it.isPasswordRespectingPolicy() }
    }

    override fun executePartTwo(input: String): Any {
        val passwordPolicies = buildPasswordPolicies(input.trim().split("\n"))
        return passwordPolicies.count { it.isPasswordRespectingNewPolicy() }
    }

    private fun buildPasswordPolicies(passwordPolicies: List<String>): List<Policy> {
        return passwordPolicies
            .map { passwordPolicy ->
                val policyParts = passwordPolicy.split(" ")

                Policy(
                    policyParts.first().split("-").zipWithNext().map { IntRange(it.first.toInt(), it.second.toInt()) }.first(),
                    policyParts[1].first(),
                    policyParts.last()
                )
            }
    }

    data class Policy(
        val range: IntRange,
        val char: Char,
        val password: String
    ) {
        fun isPasswordRespectingPolicy(): Boolean {
            return range.contains(password.count { it == char })
        }

        fun isPasswordRespectingNewPolicy(): Boolean {
            val a = password.elementAt(range.first - 1) == char
            val b = password.elementAt(range.last - 1) == char

            return ((a && !b) || (!a && b))
        }
    }
}
