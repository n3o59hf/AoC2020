package lv.n3o.aoc2020.tasks

import lv.n3o.aoc2020.Input
import lv.n3o.aoc2020.Task
import lv.n3o.aoc2020.coords.C2
import kotlin.math.absoluteValue

class T12(input: Input) : Task(input) {
    val data = input.asLines().map { it.first() to it.drop(1).toInt() }

    override fun a(): String {
        var direction = C2(0, 1)
        var position = C2(0, 0)
        data.forEach { (c, v) ->
            position = when (c) {
                'N' -> position + C2(v, 0)
                'S' -> position + C2(-v, 0)
                'E' -> position + C2(0, v)
                'W' -> position + C2(0, -v)
                'L' -> {
                    repeat(v / 90) { direction = direction.rotateLeft() }
                    position
                }
                'R' -> {
                    repeat(v / 90) { direction = direction.rotateRight() }
                    position
                }
                'F' -> position + (direction * v)
                else -> error("Unknown command: $c")
            }
        }
        return (position.x.absoluteValue + position.y.absoluteValue).toString()
    }

    override fun b(): String {
        var waypoint = C2(1, 10)
        var position = C2(0, 0)
        data.forEach { (c, v) ->
            when (c) {
                'N' -> waypoint += C2(v, 0)
                'S' -> waypoint += C2(-v, 0)
                'E' -> waypoint += C2(0, v)
                'W' -> waypoint += C2(0, -v)
                'L' -> {
                    (0 until v / 90).forEach { waypoint = waypoint.rotateLeft() }
                }
                'R' -> {
                    (0 until v / 90).forEach { waypoint = waypoint.rotateRight() }
                }
                'F' -> position += (waypoint * v)
                else -> error("Unknown command: $c")
            }
        }
        return (position.x.absoluteValue + position.y.absoluteValue).toString()
    }
}