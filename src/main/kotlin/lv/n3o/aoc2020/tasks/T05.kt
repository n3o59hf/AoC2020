package lv.n3o.aoc2020.tasks

import lv.n3o.aoc2020.Input
import lv.n3o.aoc2020.Task
import lv.n3o.aoc2020.coords.C2
import lv.n3o.aoc2020.coords.Coord2d
import lv.n3o.aoc2020.coords.minus

class T05(input: Input) : Task(input) {
    val data = input.asLines().map { it.toSeat().seatNumber() }.sorted()

    override suspend fun a(): String {
        return data.last().toString()
    }

    override suspend fun b(): String {
        val id = data.windowed(2, 1, false).first { (a, b) ->
            a + 1 != b
        }[0] + 1
        return id.toString()
    }

    private fun String.toSeat(): C2 {
        var low = C2(0, 0)
        var high = C2(8, 128)

        this.forEach {
            val diff = (high - low) / 2
            when (it) {
                'F' -> {
                    high = C2(high.x, high.y - diff.y)
                }
                'B' -> {
                    low = C2(low.x, low.y + diff.y)
                }
                'L' -> {
                    high = C2(high.x - diff.x, high.y)
                }
                'R' -> {
                    low = C2(low.x + diff.x, low.y)
                }
            }
        }

        return low
    }

    private operator fun Coord2d.div(i: Int): C2 = C2(x / 2, y / 2)

    private fun Coord2d.seatNumber() = x + y * 8
}
