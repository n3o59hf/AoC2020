package lv.n3o.aoc2020.tasks

import lv.n3o.aoc2020.Input
import lv.n3o.aoc2020.Task

class T19(input: Input) : Task(input) {

    data class Rule(val id: String, val signature: Set<List<String>>)

    val data = input.asLinesPerBlock()

    val messages = data[1]

    fun validate(ruleset: Map<String, Rule>, message: String, ruleId: String): Set<Int> {
        val rule = ruleset[ruleId] ?: error("!")
        return rule.signature.flatMap { matches ->
            var positions = listOf(0)
            for (m in matches) {
                if (m.toIntOrNull() != null) {
                    positions = positions.flatMap { p ->
                        validate(ruleset, message.drop(p), m).map { p + it }
                    }
                } else {
                    positions = positions.filter { it < message.length && message[it] == m[0] }.map { it + 1 }
                }
            }
            positions
        }.filter { it != -1 }.toSet()
    }

    override suspend fun a(): String {
        val ruleset = data[0].map {
            val (ruleId, signature) = it.split(": ")
            Rule(
                ruleId,
                when {
                    signature.contains("\"") -> setOf(listOf(signature.replace("\"", "")))
                    signature.contains("|") -> signature.split(" | ").map { sub -> sub.split(" ") }.toSet()
                    else -> setOf(signature.split(" "))
                }
            )
        }
            .map { it.id to it }
            .toMap()


        return messages.count { msg ->
            validate(ruleset, msg, "0").any { it == msg.length }
        }.toString()
    }

    override suspend fun b(): String {
        val ruleset = data[0]
            .map {
                when {
                    it.startsWith("8: ") -> "8: 42 | 42 8"
                    it.startsWith("11: ") -> "11: 42 31 | 42 11 31"
                    else -> it
                }
            }
            .map {
                val (ruleId, signature) = it.split(": ")
                Rule(
                    ruleId,
                    when {
                        signature.contains("\"") -> setOf(listOf(signature.replace("\"", "")))
                        signature.contains("|") -> signature.split(" | ").map { sub -> sub.split(" ") }.toSet()
                        else -> setOf(signature.split(" "))
                    }
                )
            }
            .map { it.id to it }
            .toMap()

        return messages.count { msg ->
            validate(ruleset, msg, "0").any { it == msg.length }
        }.toString()
    }
}
