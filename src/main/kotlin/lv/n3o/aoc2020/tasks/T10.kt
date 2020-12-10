package lv.n3o.aoc2020.tasks

import lv.n3o.aoc2020.Input
import lv.n3o.aoc2020.Task

class T10(input: Input) : Task(input) {
    val data = input.asListOfInts().sorted()
    val deviceAdapter = data.last() + 3
    val fullChain = listOf(0) + data + deviceAdapter

    override suspend fun a(): String {
        val differences = fullChain.windowed(2, 1) { (a, b) -> b - a }.groupBy { it }
        return ((differences[1]?.size ?: 0) * (differences[3]?.size ?: 0)).toString()
    }

    override suspend fun b(): String {
        val variations = mutableMapOf(0 to 1L)
        fullChain.drop(1).forEach { c ->
            variations[c] =  listOf(c - 1, c - 2, c - 3).map { variations[it] ?: 0L }.sum()
        }
        return (variations[fullChain.last()] ?: 0).toString()
    }
}