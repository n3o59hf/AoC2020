package lv.n3o.aoc2020.tasks

import lv.n3o.aoc2020.Input
import lv.n3o.aoc2020.Task
import lv.n3o.aoc2020.coords.C2
import lv.n3o.aoc2020.infinite

class T03(input: Input) : Task(input) {
    val data = input.asCoordGrid().infinite(horizontal = true)
    override suspend fun a(): String {
        return "${getTreesOnSlope(C2(3, 1))}"
    }

    override suspend fun b(): String {
        val slopes = listOf(
            C2(1, 1),
            C2(3, 1),
            C2(5, 1),
            C2(7, 1),
            C2(1, 2)
        )

        return slopes.map { getTreesOnSlope(it).toLong() }.reduce { a, b -> a * b }.toString()
    }

    private fun getTreesOnSlope(slope: C2): Int {
        var position = C2(0, 0)
        var trees = 0
        while (data.contains(position)) {
            if (data[position] == '#') trees++
            position += slope
        }
        return trees

    }
}