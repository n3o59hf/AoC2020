package lv.n3o.aoc2020.tasks

import lv.n3o.aoc2020.Input
import lv.n3o.aoc2020.Task
import lv.n3o.aoc2020.coords.C2

class T11(input: Input) : Task(input) {
    val BUSY = '#'
    val FREE = 'L'
    val NONE = '.'

    val data = input.asCoordGrid()
    val neighbors = listOf(
        C2(-1, -1),
        C2(-1, 0),
        C2(-1, 1),
        C2(0, -1),
        C2(0, 1),
        C2(1, -1),
        C2(1, 0),
        C2(1, 1)
    )

    override suspend fun a(): String {
        var grid = data.filter { it.value == 'L' }
        var oldGrid = mapOf<C2, Char>()
        while (oldGrid != grid) {
            oldGrid = grid
            grid = oldGrid.mapValues { (k, _) -> oldGrid.nextStateA(k) }
        }

        return grid.count { (_, v) -> v == BUSY }.toString()
    }

    fun Map<C2, Char>.nextStateA(c: C2) = when (neighbors.filter { this[it + c] == BUSY }.count()) {
        0 -> BUSY
        in 1..3 -> this[c] ?: error("Invalid seat")
        in 4..8 -> FREE
        else -> error("Too much neighbors")
    }

    override suspend fun b(): String {
        val visibleSeats = data.mapNotNull { (c, v) ->
            when (v) {
                '.' -> null
                else -> c to neighbors.mapNotNull { data.findVisibleSeat(c, it) }
            }
        }.toMap()

        var seatData = visibleSeats.mapValues { BUSY }
        var oldSeatData = visibleSeats.mapValues { FREE }

        while (seatData != oldSeatData) {
            oldSeatData = seatData
            seatData = oldSeatData.mapValues { (c, _) -> oldSeatData.nextStateB(c, visibleSeats[c] ?: listOf()) }
        }

        return seatData.count { (_, v) -> v == BUSY }.toString()
    }

    fun Map<C2, Char>.findVisibleSeat(c: C2, direction: C2): C2? {
        var current = c + direction
        while (this[current] == NONE) {
            current += direction
        }
        return if (this[current] == 'L') current else null
    }

    fun Map<C2, Char>.nextStateB(c: C2, visibleSeats: List<C2>) =
        when (visibleSeats.filter { this[it] == BUSY }.count()) {
            0 -> BUSY
            in 1..4 -> this[c] ?: error("Invalid seat")
            in 5..8 -> FREE
            else -> error("Too much neighbors")
        }
}