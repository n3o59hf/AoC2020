package lv.n3o.aoc2020.tasks

import lv.n3o.aoc2020.Input
import lv.n3o.aoc2020.Task
import lv.n3o.aoc2020.coords.C3
import lv.n3o.aoc2020.coords.C4

class T17(input: Input) : Task(input) {
    val initialGrid = input.asCoordGrid().filter { it.value == '#' }.keys.toSet()

    override suspend fun a(): String {
        fun getActiveRegion(grid: Iterable<C3>) = grid.flatMap { it.neighbors27(includeSelf = true) }.toSet()
        var currentGrid = initialGrid.map { C3(it.x, it.y, 0) }.toList()

        for (i in 0 until 6) {
            currentGrid = getActiveRegion(currentGrid).filter {
                val isActive = currentGrid.contains(it)
                val activeNeighborCount = it.neighbors27(false).filter(currentGrid::contains).count()
                activeNeighborCount == 3 || (isActive && activeNeighborCount == 2)
            }
        }

        return currentGrid.size.toString()
    }

    override suspend fun b(): String {
        fun getActiveRegion(grid: Iterable<C4>) = grid.flatMap { it.neighbors81(includeSelf = true) }.toSet()
        var currentGrid = initialGrid.map { C4(it.x, it.y, 0, 0) }.toList()

        for (i in 0 until 6) {
            currentGrid = getActiveRegion(currentGrid).filter {
                val isActive = currentGrid.contains(it)
                val activeNeighborCount = it.neighbors81(false).filter(currentGrid::contains).count()
                activeNeighborCount == 3 || (isActive && activeNeighborCount == 2)
            }
        }

        return currentGrid.size.toString()
    }
}