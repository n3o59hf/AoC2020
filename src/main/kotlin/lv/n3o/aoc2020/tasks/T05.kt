package lv.n3o.aoc2020.tasks

import lv.n3o.aoc2020.Input
import lv.n3o.aoc2020.Task

class T05(input: Input) : Task(input) {
    val data = input.asLines().map {
        it
            .replace('F', '0')
            .replace('B', '1')
            .replace('L', '0')
            .replace('R', '1')
            .toInt(2)
    }.sorted()

    override fun a() = data.last().toString()

    override fun b() = (1 + data.windowed(2, 1)
        .first { (a, b) ->
            a + 1 != b
        }[0])
        .toString()
}

