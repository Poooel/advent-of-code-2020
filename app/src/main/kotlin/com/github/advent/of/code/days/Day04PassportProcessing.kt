package com.github.advent.of.code.days

import com.github.advent.of.code.Executable

class Day04PassportProcessing: Executable {
    override fun executePartOne(input: String): Any {
        return validatePassports(parsePassports(input.trim().split("\n\n"), true))
    }

    override fun executePartTwo(input: String): Any {
        return validatePassports(parsePassports(input.trim().split("\n\n"), false))
    }

    private fun parsePassports(input: List<String>, skipValidation: Boolean): List<Passport> {
        return input.map { rawPassport ->
            val passportFields = rawPassport.split("\n")
                .map { rawPassportLine ->
                    rawPassportLine.split(" ")
                }.flatten()

            val parsedFields = passportFields.map {
                val passportField = it.split(":")
                passportField.first() to passportField.last()
            }.toMap()

            Passport(parsedFields, skipValidation)
        }
    }

    private fun validatePassports(passports: List<Passport>): Int {
        return passports.count { it.validate() }
    }

    data class Passport(
        val fields: Map<String, String>,
        val skipValidation: Boolean
    ) {
        private val validators = listOf(
            { validateBirthYear(fields["byr"]!!) },
            { validateIssueYear(fields["iyr"]!!) },
            { validateExpirationYear(fields["eyr"]!!) },
            { validateHeight(fields["hgt"]!!) },
            { validateHairColor(fields["hcl"]!!) },
            { validateEyeColor(fields["ecl"]!!) },
            { validatePasspordId(fields["pid"]!!) }
        )

        private val fieldsName = mutableMapOf(
            "byr" to true,
            "iyr" to true,
            "eyr" to true,
            "hgt" to true,
            "hcl" to true,
            "ecl" to true,
            "pid" to true
        )

        fun validate(): Boolean {
            return if (skipValidation) {
                fieldsName.all { !(!fields.containsKey(it.key) && it.value) }
            } else {
                fieldsName.all { !(!fields.containsKey(it.key) && it.value) } && validators.all { it() }
            }
        }

        private fun validateBirthYear(input: String): Boolean {
            return isDigits(input) && input.toInt() in 1920..2002
        }

        private fun validateIssueYear(input: String): Boolean {
            return isDigits(input) && input.toInt() in 2010..2020
        }

        private fun validateExpirationYear(input: String): Boolean {
            return isDigits(input) && input.toInt() in 2020..2030
        }

        private fun validateHeight(input: String): Boolean {
            return when {
                Regex("(\\d+cm)").matches(input) -> {
                    input.removeSuffix("cm").toInt() in 150..193
                }
                Regex("(\\d+in)").matches(input) -> {
                    input.removeSuffix("in").toInt() in 59..76
                }
                else -> {
                    false
                }
            }
        }

        private fun validateHairColor(input: String): Boolean {
            return Regex("(#[0-9a-f]{6})").matches(input)
        }

        private fun validateEyeColor(input: String): Boolean {
            return listOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth").contains(input)
        }

        private fun validatePasspordId(input: String): Boolean {
            return isDigits(input) && input.length == 9
        }

        private fun isDigits(input: String): Boolean {
            return input.toIntOrNull() != null
        }
    }
}
