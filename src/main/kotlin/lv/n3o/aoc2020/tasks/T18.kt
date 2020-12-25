package lv.n3o.aoc2020.tasks

import lv.n3o.aoc2020.Input
import lv.n3o.aoc2020.Task

class T18(input: Input) : Task(input) {
    sealed class Expression(private val representation: String) {
        class Constant(val value: Long) : Expression("$value")
        object Addition : Expression("+")
        object Multiplication : Expression("*")
        class Subexpression(val list: List<Expression>) : Expression("$list")

        override fun toString() = representation
    }

    private fun parseExpression(data: String): Expression {
        data.toLongOrNull()?.let { return Expression.Constant(it) }
        if (data == "*") return Expression.Multiplication
        if (data == "+") return Expression.Addition

        var pointer = 0
        val output = mutableListOf<Expression>()
        while (pointer < data.length) {
            when {
                data[pointer] == ' ' -> pointer++
                data[pointer].isDigit() -> {
                    val number = data.drop(pointer).takeWhile { it.isDigit() }
                    output.add(parseExpression(number))
                    pointer += number.length
                }
                data[pointer] == '(' -> {
                    var buffer = ""
                    var depth = 1
                    do {
                        buffer += data[++pointer]
                        if (data[pointer] == '(') depth++
                        if (data[pointer] == ')') depth--
                    } while (depth != 0)
                    output.add(parseExpression(buffer.dropLast(1)))
                    pointer++
                }
                else -> output.add(parseExpression(data[pointer++].toString()))

            }
        }
        return Expression.Subexpression(output)
    }

    private val expressions = input.asLines().map { parseExpression(it.replace(" ", "")) }

    private fun resolve(e: Expression, calculator: (List<Expression>) -> Long): Long =
        when (e) {
            is Expression.Subexpression -> calculator(e.list)
            is Expression.Constant -> e.value
            else -> error("Can't resolve just $e")
        }

    override fun a(): String {
        fun calculate(e: List<Expression>): Long = when {
            e.size == 1 -> resolve(e[0], ::calculate)
            e[e.size - 2] is Expression.Addition -> calculate(e.dropLast(2)) + resolve(e.last(), ::calculate)
            e[e.size - 2] is Expression.Multiplication -> calculate(e.dropLast(2)) * resolve(e.last(), ::calculate)
            else -> error("Not operator on ${e[e.size - 2]}")
        }
        return expressions.map { resolve(it, ::calculate) }.sum().toString()
    }

    override fun b(): String {
        fun calculate(e: List<Expression>): Long = when {
            e.size == 1 -> resolve(e[0], ::calculate)
            e.contains(Expression.Multiplication) -> e.indexOf(Expression.Multiplication).let {
                calculate(e.take(it)) * calculate(e.drop(it + 1))
            }
            e.contains(Expression.Addition) -> e.indexOf(Expression.Addition).let {
                calculate(e.take(it)) + calculate(e.drop(it + 1))
            }


            else -> error("Not operator on ${e[e.size - 2]}")
        }
        return expressions.map { resolve(it, ::calculate) }.sum().toString()
    }


}// 58961504812891