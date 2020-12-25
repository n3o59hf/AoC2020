package lv.n3o.aoc2020.tasks

import lv.n3o.aoc2020.Input
import lv.n3o.aoc2020.Task
import java.math.BigInteger

class T25(input: Input) : Task(input) {
    val divisor = 20201227
    val keys = input.asLines()

    private fun findLoopNumber(key: Int): Int {
        var counter = 0
        var result = 1
        while (result != key) {
            result = (result * 7) % divisor
            counter++
        }
        return counter
    }

    override suspend fun a() = BigInteger(keys[1])
        .modPow(
            BigInteger(findLoopNumber(keys[0].toInt()).toString()),
            BigInteger(divisor.toString())
        ).toString()

    override suspend fun b() = ""
}