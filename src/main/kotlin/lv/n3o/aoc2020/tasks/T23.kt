package lv.n3o.aoc2020.tasks

import lv.n3o.aoc2020.Input
import lv.n3o.aoc2020.Node
import lv.n3o.aoc2020.Task

class T23(input: Input) : Task(input) {
    private val initialCups = input.raw().map { (it - '0') }

    private fun game(cups: List<Int>, turns: Int): Sequence<Int> {
        val sentinel = Node(0)
        val max = cups.maxOrNull() ?: 1
        var current = cups.drop(1).fold(Node(cups[0])) { node, value -> node.putAfter(value) }.next

        val lookup = current.allNodes().associateBy { it.value }.toMutableMap()
        val lookupCache = Array(cups.size + 1) { lookup[it] ?: sentinel }
        val takenCache = Array(3) { sentinel }

        for (turn in 1..turns) {
            takenCache[0] = current.removeNodeAfter()
            takenCache[1] = current.removeNodeAfter()
            takenCache[2] = current.removeNodeAfter()

            var next = current.value - 1
            while (takenCache.any { it.value == next } || next == 0) {
                if (next == 0) next = max else next--
            }

            var nextPosition = lookupCache[next]

            takenCache.forEach { t ->
                nextPosition = nextPosition.putNodeAfter(t)
            }
            current = current.next
        }

        return sequence {
            current = lookupCache[1].next
            while (true) {
                yield(current.value)
                current = current.next
            }
        }
    }

    override suspend fun a() = game(initialCups, 100).take(8).joinToString("")

    override suspend fun b() = game(initialCups + (10..1_000_000).toList(), 10_000_000)
        .take(2)
        .map { it.toLong() }
        .reduce { a, b -> a * b }
        .toString()

    //        var current =
//            (initialCups + (10..1_000_000).toList()).fold(Node(0)) { node, value -> node.putAfter(value) }
//                .find(0)?.next
//                ?: error("Something failed")
//        current.prev.prev.removeAfter()
//        val lookupCache = current.allNodes().associateBy { it.value }.toMutableMap()
//        println(current.take(10))
//
//        for (turn in 1..10_000_000) {
//            if (turn % 1_000_000 == 0) {
//                println("Turn $turn ${current.allNodes().toList().size} ${lookupCache.size}")
//
//            }
//            val taken = mutableListOf<Int>()
//            taken.add(current.removeAfter())
//            taken.add(current.removeAfter())
//            taken.add(current.removeAfter())
//
//            var next = current.value - 1
//            while (taken.contains(next) || next == 0) {
//                if (next == 0) next = 1_000_000 else next--
//            }
//            var nextPosition = lookupCache[next] ?: error("Next not found $next")
//
//            taken.forEach { t ->
//                nextPosition = nextPosition.putAfter(t)
//                lookupCache[t] = nextPosition
//            }
//            current = current.next
//        }
//
//        val take = (lookupCache[1] ?: error("failed")).next.take(2)
//        println(take)
//        return take.map { it.toLong() }
//            .reduce { a, b -> a * b }.toString()
}