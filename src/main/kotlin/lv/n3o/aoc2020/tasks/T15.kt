package lv.n3o.aoc2020.tasks

import lv.n3o.aoc2020.Input
import lv.n3o.aoc2020.Task

class T15(input: Input) : Task(input) {
    val initialNumbers = input.raw().split(",").map { it.toInt() }
    val initialSize = initialNumbers.size

    fun getNumber(finalTurn: Int): Int {
        var turn = 0
        var lastNumber = -1
        var lastState = -1

        val lastSeen = IntArray(finalTurn) { -1 }
        while (turn < finalTurn) {
            lastNumber = when {
                turn < initialSize -> initialNumbers[turn]
                lastState == -1 -> 0
                else -> turn - lastState
            }
            turn++
            lastState = lastSeen[lastNumber]
            lastSeen[lastNumber] = turn
        }
        return lastNumber
    }

    override suspend fun a() = getNumber(2020).toString()
    override suspend fun b() = getNumber(30000000).toString()
}