package lv.n3o.aoc2020.tasks

import lv.n3o.aoc2020.Input
import lv.n3o.aoc2020.Task

class T04(input: Input) : Task(input) {
    val data = input
        .raw()
        .replace("\r", "")
        .split("\n\n")
        .map {
            it.split("\n", " ").filter(String::isNotBlank).map { item ->
                val (k, v) = item.split(":")
                k to v
            }.toMap()
        }
    private val neededFields = listOf(
        "byr",
        "iyr",
        "eyr",
        "hgt",
        "hcl",
        "ecl",
        "pid"
    )

    private val validEyeColors = listOf(
        "amb", "blu", "brn", "gry", "grn", "hzl", "oth",
    )

    override suspend fun a(): String {
        return data.count { entry -> neededFields.all { entry.containsKey(it) } }.toString()
    }

    fun checkYear(input: String, range: IntRange): Boolean {
        val year = input.toIntOrNull()
        return year != null && year in range
    }

    override suspend fun b(): String {
        val validations: Map<String, (String) -> Boolean> = mapOf(
//        byr (Birth Year) - four digits; at least 1920 and at most 2002.
            "byr" to { checkYear(it, 1920..2002) },
//        iyr (Issue Year) - four digits; at least 2010 and at most 2020.
            "iyr" to { checkYear(it, 2010..2020) },
//        eyr (Expiration Year) - four digits; at least 2020 and at most 2030.
            "eyr" to { checkYear(it, 2020..2030) },
//        hgt (Height) - a number followed by either cm or in:
//        If cm, the number must be at least 150 and at most 193.
//        If in, the number must be at least 59 and at most 76.
            "hgt" to {
                when {
                    it.endsWith("cm") -> {
                        it.length == 5 && it.substring(0, 3).toIntOrNull()?.let { h -> h in 150..193 } ?: false
                    }
                    it.endsWith("in") -> {
                        it.length == 4 && it.substring(0, 2).toIntOrNull()?.let { h -> h in 59..76 } ?: false
                    }
                    else -> false
                }
            },
//        hcl (Hair Color) - a # followed by exactly six characters 0-9 or a-f.
            "hcl" to {
                it.startsWith("#") && it.length == 7 && it.drop(1).all { c -> c in '0'..'9' || c in 'a'..'f' }
            },
//        ecl (Eye Color) - exactly one of: amb blu brn gry grn hzl oth.
            "ecl" to {
                validEyeColors.contains(it)
            },
//        pid (Passport ID) - a nine-digit number, including leading zeroes.
            "pid" to {
                it.all { c -> c in '0'..'9' } && it.length == 9
            }
        )

        return data.count { entry ->
            validations.all { v ->
                val value = entry[v.key]
                val result = value != null && v.value(value)

                if (!result) {
                    log("${v.key} failed with value $value")
                    log("\tfor $entry")
                }
                result
            }
        }.toString()

    }
}