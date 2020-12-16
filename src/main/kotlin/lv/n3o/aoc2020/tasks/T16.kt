package lv.n3o.aoc2020.tasks

import lv.n3o.aoc2020.Input
import lv.n3o.aoc2020.Task

class T16(input: Input) : Task(input) {
    val data = input.asLinesPerBlock()
    val classes: Map<String, List<IntRange>> = data[0].map {
        val (name, values) = it.split(": ")
        name to values
            .split(" or ")
            .map { value ->
                value.split("-")
                    .map(String::toInt)
                    .let { range -> range.first()..range.last() }
            }
    }.toMap()

    val myTicket = data[1][1].split(",").map(String::toInt)
    val otherTickets = data[2].drop(1).map { it.split(",").map(String::toInt) }

    val validNumbers = classes.values.flatMap { it.flatMap { range -> range.toList() } }.toSet()

    override suspend fun a(): String {
        return otherTickets.flatten().filter { !validNumbers.contains(it) }.sum().toString()
    }

    override suspend fun b(): String {
        val possibleFields = myTicket.map { classes.keys.toMutableSet() }
        otherTickets.forEach { ticket ->
            if (ticket.all { validNumbers.contains(it) }) {
                ticket.forEachIndexed { i, field ->
                    val invalidFields = possibleFields[i].filter { key ->
                        val rules = classes[key] ?: error("")
                        rules.none { it.contains(field) }
                    }
                    possibleFields[i].removeAll(invalidFields)
                }
            }
        }

        while (!possibleFields.all { it.size == 1 }) {
            val uniqueFields = possibleFields.filter { it.size == 1 }.flatten().toSet()
            possibleFields.forEach {
                if (it.size != 1) it.removeAll(uniqueFields)
            }
        }

        val fieldIndexes = possibleFields
            .map { it.first() }
            .mapIndexedNotNull { index, field ->
                if (field.startsWith("departure")) index else null
            }
        return fieldIndexes
            .map { myTicket[it].toLong() }
            .reduce { a, b -> a * b }
            .toString()
    }
}