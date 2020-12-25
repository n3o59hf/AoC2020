package lv.n3o.aoc2020.tasks

import lv.n3o.aoc2020.Input
import lv.n3o.aoc2020.Task
import lv.n3o.aoc2020.coords.C2
import kotlin.math.min
import kotlin.math.sqrt

private val frameSize = 10
private val innerSize = frameSize - 2

class T20(input: Input) : Task(input) {
    data class Tile(val id: Int, val lines: List<List<Char>>, val assignedCoordinate: C2? = null) {
        val top = lines.first()
        val bottom = lines.last()
        val left = lines.map { it.first() }.joinToString("")
        val right = lines.map { it.last() }.joinToString("")

        fun flipVertical() = copy(lines = lines.asReversed())
        fun flipHorizontal() = copy(lines = lines.map { it.asReversed() })

        fun rotate() = copy(lines = (frameSize - 1 downTo 0).map { y ->
            (0 until frameSize).map { x -> lines[x][y] }
        })

        fun match(other: Tile): Tile? = when {
            other.top == this.bottom -> other.copy(assignedCoordinate = assignedCoordinate?.plus(C2(0, 1)))
            other.bottom == this.top -> other.copy(assignedCoordinate = assignedCoordinate?.plus(C2(0, -1)))
            other.right == this.left -> other.copy(assignedCoordinate = assignedCoordinate?.plus(C2(-1, 0)))
            other.left == this.right -> other.copy(assignedCoordinate = assignedCoordinate?.plus(C2(1, 0)))
            else -> null
        }

        fun getVariations() = listOf(
            this,
            this.flipVertical(),
            this.flipHorizontal(),
            this.rotate(),
            this.rotate().rotate(),
            this.rotate().rotate().rotate(),
            this.flipVertical().rotate(),
            this.flipVertical().rotate().rotate(),
            this.flipVertical().rotate().rotate().rotate(),
        )

        override fun toString() =
            "Tile $id: ${assignedCoordinate?.let { "[${it.x};${it.y}]" }}\n" + lines.joinToString("\n") {
                it.joinToString("")
            } + "\n"
    }

    private val tiles = input.asLinesPerBlock().map {
        Tile(it[0].removePrefix("Tile ").removeSuffix(":").toInt(),
            it.drop(1).map(String::toList).filterNot(List<Char>::isEmpty))
    }

    private val gridSize = sqrt(tiles.count().toDouble()).toInt()

    val grid by lazy {
        val notAssigned = tiles.drop(1).map { it.id to it }.toMap().toMutableMap()
        val assigned = mutableListOf(tiles.first().copy(assignedCoordinate = C2(0, 0)))

        while (notAssigned.isNotEmpty()) {
            val matching = notAssigned
                .values
                .asSequence()
                .flatMap { it.getVariations() }
                .flatMap { assigned.map { a -> a.match(it) } }
                .filterNotNull()
                .first()
            notAssigned.remove(matching.id)
            assigned.add(matching)
        }

        val topCorner =
            assigned.mapNotNull { it.assignedCoordinate }.fold(C2(0, 0)) { a, b -> C2(min(a.x, b.x), min(a.y, b.y)) }
        assigned.replaceAll {
            it.copy(assignedCoordinate = it.assignedCoordinate?.minus(topCorner))
        }

        assigned.map { it.assignedCoordinate!! to it }.toMap()
    }

    val mapBottomRightCorner = C2(innerSize * gridSize - 1, innerSize * gridSize - 1)

    /*
    0                  #
    1#    ##    ##    ###
    2 #  #  #  #  #  #
     01234567890123456789
     */
    val monsterSignature = listOf(
        C2(18, 0),
        C2(0, 1),
        C2(5, 1),
        C2(6, 1),
        C2(11, 1),
        C2(12, 1),
        C2(17, 1),
        C2(18, 1),
        C2(19, 1),
        C2(1, 2),
        C2(4, 2),
        C2(7, 2),
        C2(10, 2),
        C2(13, 2),
        C2(16, 2)
    )

    override fun a(): String {
        return listOf(C2(0, 0), C2(0, gridSize - 1), C2(gridSize - 1, 0), C2(gridSize - 1, gridSize - 1)).map {
            grid[it]?.id?.toLong() ?: error("Should exist")
        }.reduce { a, b -> a * b }.toString()
    }

    override fun b(): String {
        var monsterFindMap = grid.flatMap { (c, sq) ->
            val baseCoord = c.times(innerSize)
            (0 until innerSize).flatMap { y ->
                (0 until innerSize).map { x ->
                    (baseCoord + C2(x, y)) to (sq.lines[y + 1][x + 1])
                }
            }
        }.toMap()

        var monsterFound = false
        var rotateCounter = 0

        while (!monsterFound) {
            monsterFindMap = when (rotateCounter++ % 4) {
                0 -> monsterFindMap.mapKeys { C2(it.key.x, mapBottomRightCorner.y - it.key.y) }.toMutableMap()
                else -> monsterFindMap.mapKeys { C2(it.key.y, mapBottomRightCorner.x - it.key.x) }.toMutableMap()
            }

            for (y in 0..mapBottomRightCorner.y) {
                for (x in 0..mapBottomRightCorner.x) {
                    val current = C2(x, y)
                    if (monsterSignature.all { monsterFindMap[current + it] == '#' }) {
                        monsterFound = true
                        monsterSignature.forEach { monsterFindMap[current + it] = 'O' }
                    }
                }
            }
        }
        return monsterFindMap.values.count { it == '#' }.toString()
    }
}