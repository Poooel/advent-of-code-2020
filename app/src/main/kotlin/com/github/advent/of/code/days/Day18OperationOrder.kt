package com.github.advent.of.code.days

import com.github.advent.of.code.Executable
import java.io.StreamTokenizer

class Day18OperationOrder: Executable {
    override fun executePartOne(input: String): Any {
        val expressions = input.lines()
        return expressions.sumOf { evaluatePostfixNotation(infixToPostfixNotation(tokenize(it)) { 0 }) }
    }

    override fun executePartTwo(input: String): Any {
        val expressions = input.lines()
        return expressions.sumOf { evaluatePostfixNotation(infixToPostfixNotation(tokenize(it)) { x -> when(x) { "+" -> 2; "*" -> 1; else -> 0 } }) }
    }

    private fun tokenize(input: String): List<String> {
        val stringReader = input.reader()
        val tokenizer = StreamTokenizer(stringReader)
        val tokens = mutableListOf<String>()

        var currentToken = tokenizer.nextToken()

        while (currentToken != StreamTokenizer.TT_EOF) {
            when (currentToken) {
                StreamTokenizer.TT_NUMBER -> tokens.add(tokenizer.nval.toString())
                StreamTokenizer.TT_WORD -> tokens.add(tokenizer.sval)
                else -> tokens.add(currentToken.toChar().toString())
            }
            currentToken = tokenizer.nextToken()
        }

        return tokens
    }

    private fun infixToPostfixNotation(expression: List<String>, operatorPrecedence: (String) -> Int): List<String> {
        val stack = mutableListOf<String>()
        val postfixNotation = mutableListOf<String>()

        expression.forEach {
            when {
                it.isOperator() -> {
                    while (stack.isNotEmpty() && stack.last().isOperator() && operatorPrecedence(stack.last()) >= operatorPrecedence(it)) {
                        postfixNotation.add(stack.removeLast())
                    }
                    stack.add(it)
                }
                it.isLeftParenthesis() -> stack.add(it)
                it.isRightParenthesis() -> {
                    while (stack.isNotEmpty() && stack.last().isNotLeftParenthesis()) {
                        postfixNotation.add(stack.removeLast())
                    }
                    stack.removeLast()
                }
                else -> postfixNotation.add(it)
            }
        }

        while (stack.isNotEmpty()) {
            postfixNotation.add(stack.removeLast())
        }

        return postfixNotation
    }

    private fun evaluatePostfixNotation(expression: List<String>): Long {
        val stack = mutableListOf<Long>()

        expression.forEach {
            when {
                it.isOperator() -> {
                    val right = stack.removeLast()
                    val left = stack.removeLast()
                    stack.add(
                        when (it) {
                            "+" -> left + right
                            "*" -> left * right
                            else -> throw IllegalArgumentException("Unknown operator: $it")
                        }
                    )
                }
                else -> stack.add(it.toDouble().toLong())
            }
        }

        return stack.last()
    }

    private fun String.isOperator(): Boolean {
        return this == "+" || this == "*"
    }

    private fun String.isLeftParenthesis(): Boolean {
        return this == "("
    }

    private fun String.isRightParenthesis(): Boolean {
        return this == ")"
    }

    private fun String.isNotLeftParenthesis(): Boolean {
        return this != "("
    }
}
