package lv.n3o.aoc2020.tasks

import lv.n3o.aoc2020.Input
import lv.n3o.aoc2020.Task

class T06(input: Input) : Task(input) {
    val data = input.asLinesPerBlock().map { group -> group.map(String::toSet) }

    override suspend fun a(): String {
        val groupSets = data.map { it.reduce { a, b -> a + b } }
        return groupSets.sumBy(Set<Char>::size).toString()
    }

    override suspend fun b(): String {
        val groupSets = data.map { it.reduce(Set<Char>::intersect) }
        return groupSets.sumBy(Set<Char>::size).toString()
    }
}