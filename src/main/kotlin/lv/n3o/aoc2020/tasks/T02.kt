package lv.n3o.aoc2020.tasks

import lv.n3o.aoc2020.Input
import lv.n3o.aoc2020.Task

class T02(input: Input) : Task(input) {
    class PasswordRow(
        val letter: Char,
        val from: Int,
        val to : Int,
        val password: List<Char>
    )

    val data = input.asLines().map {
        val (rest, password) = it.split(": ")
        val (range, letter) = rest.split(" ")
        val (from, to) = range.split("-").map(String::toInt)

        PasswordRow(letter[0], from,to, password.toList())
    }

    override fun a(): String {

        return data.count {
            it.password.count { c -> c == it.letter } in (it.from..it.to)
        }.toString()
    }

    override fun b(): String {
        return data.count {
            (it.password[it.from - 1] == it.letter) != (it.password[it.to-1] == it.letter)
        }.toString()
    }

}