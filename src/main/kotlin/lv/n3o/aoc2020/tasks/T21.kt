package lv.n3o.aoc2020.tasks

import lv.n3o.aoc2020.Input
import lv.n3o.aoc2020.Task

class T21(input: Input) : Task(input) {
    data class Food(val ingredients: List<String>, val allergens: Set<String>)

    private val food = input
        .asLines()
        .map {
            it
                .replace(")", "")
                .split(" (contains ")
                .let { (ingredients, allergens) ->
                    Food(ingredients.split(" "), allergens.split(", ").toSet())
                }
        }

    private val allergenIngredients = sequence {
        var suspicious = food.flatMap { it.allergens }.toSet().associateBy({ it }) { a ->
            food
                .filter { it.allergens.contains(a) }
                .map { it.ingredients.toSet() }
                .reduce { acc, ing -> acc.intersect(ing) }
        }

        while (suspicious.isNotEmpty()) {
            val (allergen, ingredients) = suspicious.filter { it.value.size == 1 }.toList().first()
            val ingredient = ingredients.first()
            yield(allergen to ingredient)
            suspicious = suspicious.filterNot { it.key == allergen }.mapValues { it.value - ingredient }.toMutableMap()
        }
    }.sortedBy { it.first }.map { it.second }

    override fun a() = food.sumBy { (it.ingredients - allergenIngredients).size }.toString()

    override fun b() = allergenIngredients.joinToString(",")
}