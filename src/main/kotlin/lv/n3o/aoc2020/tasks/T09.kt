package lv.n3o.aoc2020.tasks

import lv.n3o.aoc2020.Input
import lv.n3o.aoc2020.Task

class T09(input: Input) : Task(input) {
    private val PREAMBLE_SIZE = 25
    val data = input.asListOfLongs()

    val outlier = data.windowed(PREAMBLE_SIZE + 1, 1, false) {
        it.last() to it.dropLast(1).sorted()
    }.first { (number, preamble) ->
        !preamble.any {
            val diff = number - it
            diff != number && preamble.contains(diff)
        }
    }.first

    override fun a(): String {
        return outlier.toString()
    }

    override fun b(): String {
        for (i in data.indices) {
            var current = i
            var reminder = outlier
            while (reminder > 0) {
                reminder -= data[current]
                if (reminder == 0L && current != i) {
                    return data
                        .subList(i, current)
                        .sorted()
                        .let { it.first() + it.last() }
                        .toString()
                }
                current++
            }
        }
        error("No answer found")
    }
}