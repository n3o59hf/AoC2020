package lv.n3o.aoc2020.tasks

import lv.n3o.aoc2020.Input
import lv.n3o.aoc2020.Task

class T07(input: Input) : Task(input) {
    private val OWN_BAG = "shiny gold"
    val rules: Map<String, List<Pair<Int, String>>> = input.asLines().map { line ->
        val (color, ruleset) = line
            .replace(" bags", "")
            .replace(" bag", "")
            .replace(".", "")
            .split(" contain ")
        val ruleParts = ruleset
            .split(", ")
            .mapNotNull {
                val (number, ruleColor) = it.split(" ", limit = 2)
                (number.toIntOrNull() ?: return@mapNotNull null) to ruleColor
            }
        color to ruleParts
    }.toMap()

    override fun a(): String {
        val reverseRules = rules.flatMap { (from, to) ->
            to.map { (_, color) -> color to from }
        }.groupBy(Pair<String, String>::first, Pair<String, String>::second)

        val seenBags = mutableSetOf<String>()
        val bagsToObserve = mutableSetOf<String>()
        bagsToObserve.add(OWN_BAG)

        while (bagsToObserve.isNotEmpty()) {
            val bag = bagsToObserve.first()
            bagsToObserve.remove(bag)
            (reverseRules[bag])?.forEach { newBag ->
                if (seenBags.add(newBag)) {
                    bagsToObserve.add(newBag)
                }
            }
        }
        return (seenBags.size).toString()
    }

    override fun b(): String {
        fun sumBags(initial: String): Int {
            val includedBags = rules[initial] ?: error("Should have rules for all")
            return 1 + includedBags.map { (count, bag) -> count * sumBags(bag) }.sum()
        }

        return (sumBags(OWN_BAG) - 1L).toString()
    }
}