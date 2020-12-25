package lv.n3o.aoc2020.tasks

import lv.n3o.aoc2020.Input
import lv.n3o.aoc2020.Task

class T01(input: Input) : Task(input) {
    val data = input.asListOfLongs()

    override fun a(): String {
        val indexed = data.withIndex()
        val (e1, e2) = indexed
            .flatMap { (i, x) -> indexed.mapNotNull { (j, y) -> if (i == j) null else (x + y to (x to y)) } }
            .first { it.first == 2020L }
            .second

        return "${e1 * e2}"
    }

    override fun b(): String {
        val indexed = data.withIndex()
        val (e1, e2, e3) = indexed
            .flatMap { (i, x) ->
                indexed.flatMap { (j, y) ->
                    indexed.mapNotNull { (k, z) ->
                        if (i == j || j == k || i == k) null
                        else (x + y + z to Triple(x, y, z))
                    }
                }
            }
            .first { it.first == 2020L }
            .second

        return "${e1 * e2 * e3}"
    }

}