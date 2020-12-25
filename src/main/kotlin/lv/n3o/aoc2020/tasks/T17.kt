package lv.n3o.aoc2020.tasks

import lv.n3o.aoc2020.Input
import lv.n3o.aoc2020.Task
import lv.n3o.aoc2020.coords.C3
import lv.n3o.aoc2020.coords.C4

class T17(input: Input) : Task(input) {
    val initialGrid = input.asCoordGrid().filter { it.value == '#' }.keys.toSet()

    override fun a(): String {
        fun getActiveRegion(grid: Iterable<C3>) = grid.flatMap { it.neighbors27(includeSelf = true) }.toSet()
        var currentGrid = initialGrid.map { C3(it.x, it.y, 0) }.toSet()

        for (i in 0 until 6) {
            currentGrid = getActiveRegion(currentGrid).filter {
                val isActive = currentGrid.contains(it)
                val activeNeighborCount = it.neighbors27(false).filter(currentGrid::contains).count()
                activeNeighborCount == 3 || (isActive && activeNeighborCount == 2)
            }.toSet()
        }

        return currentGrid.size.toString()
    }

    override fun b(): String {
        var currentGrid = initialGrid.map { C4(it.x, it.y, 0, 0) }.toSet()
        fun getActiveRegion(grid: Iterable<C4>) = (grid + grid.flatMap(C4::neighbors81)).toSet()
        for (i in 0 until 6) {
            currentGrid = getActiveRegion(currentGrid).filter {
                val isActive = currentGrid.contains(it)
                var activeCount = 0
                it.neighbors81.forEach { c ->
                    if (currentGrid.contains(c)) {
                        activeCount++
                        if (activeCount > 3) return@filter false
                    }
                }
                activeCount == 3 || (isActive && activeCount == 2)
            }.toSet()
        }

        return currentGrid.size.toString()
    }
}