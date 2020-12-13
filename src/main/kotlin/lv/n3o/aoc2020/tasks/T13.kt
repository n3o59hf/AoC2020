package lv.n3o.aoc2020.tasks

import lv.n3o.aoc2020.Input
import lv.n3o.aoc2020.Task

class T13(input: Input) : Task(input) {
    val currentTime = input.asLines()[0].toInt()
    val busLines = input.asLines()[1].split(",").map(String::toIntOrNull)

    override suspend fun a(): String {
        val nextDepartureTimes = busLines.filterNotNull().map { it to it - (currentTime % it) }
        return nextDepartureTimes.minByOrNull { it.second }?.let { it.first * it.second }.toString()
    }

    override suspend fun b(): String {
        val indexedLines = busLines
            .mapIndexedNotNull { offset, id ->
                if (id == null) null else BussCheck(
                    id.toLong(),
                    offset.toLong()
                )
            }
            .sortedBy { -it.id }

        var increment = indexedLines.first().id
        var time = - indexedLines.first().offset
        var currentIndex = 0
        var lastZero: Long? = null

        while (true) {
            time += increment
            log("Looking at time: $time")
            if (indexedLines[currentIndex].checkTime(time)) {
                if (lastZero == null) {
                    log("First match for ${indexedLines[currentIndex]}")
                    if (currentIndex == indexedLines.size-1) return time.toString()
                    lastZero = time
                } else {
                    increment = time - lastZero
                    time -= increment
                    log("Second match for ${indexedLines[currentIndex]}, adjusting increment to $increment")
                    lastZero = null
                    currentIndex++
                }
            }
        }
    }

    data class BussCheck(val id: Long, val offset: Long) {
        fun checkTime(time: Long) = ((time + offset) % id) == 0L
    }
}