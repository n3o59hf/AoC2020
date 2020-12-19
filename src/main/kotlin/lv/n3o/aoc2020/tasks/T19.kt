package lv.n3o.aoc2020.tasks

import lv.n3o.aoc2020.Input
import lv.n3o.aoc2020.Task

class T19(input: Input) : Task(input) {
    val data = input.asLinesPerBlock()
    val messages = data[1]
    val baseRuleset = data[0].map(this::parseRuleToPair).toMap()
    val additionalRules = baseRuleset + mapOf(
        parseRuleToPair("8: 42 | 42 8"),
        parseRuleToPair("11: 42 31 | 42 11 31")
    )

    data class Rule(val id: String, val signature: Set<List<String>>)

    private fun parseRuleToPair(ruleLine: String): Pair<String, Rule> {
        val (ruleId, signature) = ruleLine.replace("\"", "").split(": ")
        val rule = Rule(
            ruleId,
            signature
                .split(" | ")
                .map { it.split(" ") }
                .toSet()
        )
        return ruleId to rule
    }

    private fun Map<String, Rule>.validate(message: String): Boolean {
        fun Map<String, Rule>.traverse(message: String, position: Int, rule: String): Set<Int> = this[rule]
            ?.signature
            ?.flatMap { rules ->
                rules.fold(listOf(position)) { positions, nextRule ->
                    positions.flatMap { position -> traverse(message, position, nextRule) }
                }
            }
            ?.toSet()
            ?: if (rule[0] == message.getOrNull(position)) setOf(position + 1) else setOf()
        return traverse(message, 0, "0").any { it == message.length }
    }

    override suspend fun a() = messages
        .count { msg -> baseRuleset.validate(msg) }
        .toString()

    override suspend fun b() = messages
        .count { msg -> additionalRules.validate(msg) }
        .toString()
}
