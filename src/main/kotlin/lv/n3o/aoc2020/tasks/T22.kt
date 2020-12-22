package lv.n3o.aoc2020.tasks

import lv.n3o.aoc2020.Input
import lv.n3o.aoc2020.Task

class T22(input: Input) : Task(input) {
    private val cards = input.asLinesPerBlock().map { it.drop(1).map(String::toInt) }

    private fun countPoints(p1: List<Int>, p2: List<Int>) = (p1 + p2)
        .reversed()
        .foldRightIndexed(0) { index, card, acc ->
            acc + (card * (index + 1))
        }
        .toString()

    override suspend fun a(): String {
        val player1 = cards[0].toMutableList()
        val player2 = cards[1].toMutableList()

        while (player1.isNotEmpty() && player2.isNotEmpty()) {
            if (player1[0] > player2[0]) {
                player1.add(player1.removeFirst())
                player1.add(player2.removeFirst())
            } else {
                player2.add(player2.removeFirst())
                player2.add(player1.removeFirst())
            }
        }

        return countPoints(player1, player2)
    }

    override suspend fun b(): String {
        fun combat(player1: MutableList<Int>, player2: MutableList<Int>): Boolean {
            val previousStates = mutableSetOf<List<Int>>()

            while (player1.isNotEmpty() && player2.isNotEmpty()) {
                val checksum = player1 + 0 + player2
                if (!previousStates.add(checksum)) return true // P1 wins

                val p1 = player1.removeFirst()
                val p2 = player2.removeFirst()


                val p1WinsRound = when {
                    player1.size >= p1 && player2.size >= p2 ->
                        combat(player1.take(p1).toMutableList(), player2.take(p2).toMutableList())
                    else -> p1 > p2
                }

                if (p1WinsRound) {
                    player1.add(p1)
                    player1.add(p2)
                } else {
                    player2.add(p2)
                    player2.add(p1)
                }
            }

            return player1.isNotEmpty()
        }

        val p1 = cards[0].toMutableList()
        val p2 = cards[1].toMutableList()
        combat(p1, p2)
        return countPoints(p1, p2)
    }
}