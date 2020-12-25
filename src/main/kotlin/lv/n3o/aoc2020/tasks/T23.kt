package lv.n3o.aoc2020.tasks

import lv.n3o.aoc2020.Input
import lv.n3o.aoc2020.Task

class T23(input: Input) : Task(input) {
    private val initialCups = input.raw().map { (it - '0') }

    private fun game(cups: List<Int>, turns: Int): Sequence<Int> {
        val max = cups.size
        var current = cups.first()
        val taken = IntArray(3) { 0 }
        val circle = IntArray(cups.size+1)
        (cups+cups.first()).windowed(2,1).forEach { (from,to) ->
            circle[from] = to
        }

        for (turn in 1..turns) {
            taken[0] = circle[current]
            taken[1] = circle[taken[0]]
            taken[2] = circle[taken[1]]
            circle[current] = circle[taken[2]]

            var next = current - 1
            while (next == 0 || taken[0] == next || taken[1] == next || taken[2] == next) {
                if (next == 0) next = max else next--
            }

            circle[taken[2]] = circle[next]
            circle[next] = taken[0]
            current = circle[current]
        }

        return sequence {
            current = circle[1]
            while (true) {
                yield(current)
                current = circle[current]
            }
        }
    }

    override suspend fun a() = game(initialCups, 100).take(8).joinToString("")

    override suspend fun b() = game(initialCups + (10..1_000_000).toList(), 10_000_000)
        .take(2)
        .map { it.toLong() }
        .reduce { a, b -> a * b }
        .toString()
}