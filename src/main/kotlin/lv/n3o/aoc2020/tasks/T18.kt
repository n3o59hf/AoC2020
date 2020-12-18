package lv.n3o.aoc2020.tasks

import lv.n3o.aoc2020.Input
import lv.n3o.aoc2020.Task

class T18(input: Input) : Task(input) {
    val expressions = input.asLines()

    fun calculate1(input: String): String {
        // reduce parenthesis
        var buffer = ""
        var depth = 0
        var expression = ""
        loop@ for (i in input.indices) {
            val c = input[i]
            when (c) {
                ' ' -> continue@loop
                '(' -> depth++
                ')' -> {
                    depth--
                    if (depth == 0) {
                        expression += calculate1(buffer.drop(1))
                        buffer = ""
                        continue@loop
                    }
                }
            }
            if (depth > 0) {
                buffer += c
            } else {
                expression += c
            }
        }

        while (expression.any { !it.isDigit() }) {
            val arg1 = expression.takeWhile { it.isDigit() }
            val operand = expression[arg1.length]
            val arg2 = expression.drop(arg1.length + 1).takeWhile { it.isDigit() }
            val result =
                if (operand == '+') (arg1.toLong() + arg2.toLong()).toString() else (arg1.toLong() * arg2.toLong()).toString()
            expression = result + expression.drop(arg1.length + arg2.length + 1)
        }

        return expression

    }
    fun calculate2(input: String): String {
        // reduce parenthesis
        var buffer = ""
        var depth = 0
        var expression = ""
        loop@ for (i in input.indices) {
            val c = input[i]
            when (c) {
                ' ' -> continue@loop
                '(' -> depth++
                ')' -> {
                    depth--
                    if (depth == 0) {
                        expression += calculate2(buffer.drop(1))
                        buffer = ""
                        continue@loop
                    }
                }
            }
            if (depth > 0) {
                buffer += c
            } else {
                expression += c
            }
        }

        val parts = expression.split(Regex("(?<=[+*])|(?=[+*])")).toMutableList()
        while(parts.contains("+")) {
            val op = parts.indexOf("+")
            val a = parts[op-1]
            val b = parts[op+1]
            parts.removeAt(op-1)
            parts.removeAt(op-1)
            parts[op-1] = (a.toLong() + b.toLong()).toString()
        }
        while(parts.contains("*")) {
            val op = parts.indexOf("*")
            val a = parts[op-1]
            val b = parts[op+1]
            parts.removeAt(op-1)
            parts.removeAt(op-1)
            parts[op-1] = (a.toLong() * b.toLong()).toString()
        }

        return parts[0]

    }

    override suspend fun a(): String {
        return expressions.map { calculate1(it).toLong() }.sum().toString()
    }

    override suspend fun b(): String {
//    println(calculate2("((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2"))
//        return ""
            return expressions.map { calculate2(it).toLong() }.sum().toString()
    }

}// 58961504812891