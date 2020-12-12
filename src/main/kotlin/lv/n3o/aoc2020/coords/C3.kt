package lv.n3o.aoc2020.coords

import kotlin.math.abs

data class C3(
    val x: Int,
    val y: Int,
    val z: Int
) {
    operator fun plus(other: C3) = new(x + other.x, y + other.y, z + other.z)
    operator fun minus(other: C3) = this + new(-other.x, -other.y, -other.z)

    fun new(x: Int, y: Int, z: Int) = C3(x, y, z)
    fun distance(to: C3) = abs(to.x - x) + abs(to.y - y) + abs(to.z - z)

    fun vector(to: C3) = new(to.x - x, to.y - y, to.z - z)
    fun unit() = new(
        when {
            x > 0 -> 1
            x < 0 -> -1
            else -> 0
        },
        when {
            y > 0 -> 1
            y < 0 -> -1
            else -> 0
        },
        when {
            z > 0 -> 1
            z < 0 -> -1
            else -> 0
        }
    )
}