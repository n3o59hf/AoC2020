package lv.n3o.aoc2020.tasks

import lv.n3o.aoc2020.Input
import lv.n3o.aoc2020.Task

class T14(input: Input) : Task(input) {
    val instructions = input.asLines().map { it.split(" = ") }
    override suspend fun a(): String {
        val memory = mutableMapOf<Long, Long>()
        var currentMask = ""

        for ((command, data) in instructions) {
            if (command == "mask") currentMask = data
            else {
                val cell = command.drop(4).dropLast(1).toLong()
                val binaryData = data.toLong().toString(2).padStart(36, '0')
                val masked = currentMask
                    .zip(binaryData)
                    .map { if (it.first == 'X') it.second else it.first }
                    .joinToString("")
                    .toLong(2)
                memory[cell] = masked
            }
        }

        return memory.values.sum().toString()
    }

    override suspend fun b(): String {
        val memory = mutableMapOf<String, Long>()
        var currentMask = ""
        fun getAddresses(floating: String, depth: Int = 0): List<String> =
            when {
                depth == floating.length -> listOf(floating)
                floating[depth] == 'X' ->
                    getAddresses(floating.replaceFirst('X', '0'), depth + 1) +
                            getAddresses(floating.replaceFirst('X', '1'), depth + 1)
                else -> getAddresses(floating, depth + 1)
            }
        for ((command, data) in instructions) {
            if (command == "mask") currentMask = data
            else {
                val cell = command.drop(4).dropLast(1).toLong().toString(2).padStart(36, '0')
                val masked = currentMask
                    .zip(cell)
                    .map { if (it.first == '0') it.second else it.first }
                    .joinToString("")

                val longData = data.toLong()
                getAddresses(masked).forEach {
                    memory[it] = longData
                }
            }
        }
        return memory.values.sum().toString()
    }
}