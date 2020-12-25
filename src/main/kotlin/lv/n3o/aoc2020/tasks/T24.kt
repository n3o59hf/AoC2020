package lv.n3o.aoc2020.tasks

import lv.n3o.aoc2020.Input
import lv.n3o.aoc2020.Task
import lv.n3o.aoc2020.coords.C3

class T24(input: Input) : Task(input) {
    private val directions = listOf(
        "e" to C3(-1, 1, 0),
        "se" to C3(0, 1, -1),
        "sw" to C3(1, 0, -1),
        "w" to C3(1, -1, 0),
        "nw" to C3(0, -1, 1),
        "ne" to C3(-1, 0, 1)
    ).toMap()

    private val initialTiles = input
        .asLines()
        .map { l ->
            sequence {
                var path = ""
                l.forEach { c ->
                    path += c
                    val direction = directions[path]
                    if (direction != null) {
                        yield(direction)
                        path = ""
                    }
                }
            }
        }
        .map { it.reduce { a, b -> a + b } }
        .groupBy { it }
        .filter { it.value.size % 2 == 1 }
        .keys

    override fun a() = initialTiles.count().toString()

    override fun b(): String {
        var floor = initialTiles

        for (day in 1..100) {
            floor = floor.flatMap { it.hexNeighbors() + it }.toSet().filter { c ->
                val isBlack = floor.contains(c)
                val neighborCount = c.hexNeighbors().count { floor.contains(it) }
                (isBlack && neighborCount in 1..2) || (!isBlack && neighborCount == 2)
            }.toSet()
        }

        return floor.size.toString()
    }

    private fun C3.hexNeighbors() = directions.values.map { it + this }
}