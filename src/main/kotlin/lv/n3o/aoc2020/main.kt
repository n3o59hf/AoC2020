@file:JvmName("Main")
package lv.n3o.aoc2020

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlin.system.measureTimeMillis

private const val ANSWER_SIZE = 24
private fun ci(number: Int, expectedA: String, expectedB: String): TestCase {
    val paddedNumber = "$number".padStart(2, '0')
    val className = "T$paddedNumber"
    val inputFile = "i$paddedNumber.txt"
    val taskClass = Class.forName("lv.n3o.aoc2020.tasks.$className")
    val constructor: (Input) -> Task =
        { input -> taskClass.getConstructor(Input::class.java).newInstance(input) as Task }


    return TestCase(
        "T:$paddedNumber",
        ClasspathInput(inputFile),
        constructor,
        expectedA,
        expectedB
    )
}

val testCases: List<TestCase> = listOf(
    ci(1, "224436", "303394260"),
    ci(2, "542", "360"),
    ci(3, "153", "2421944712"),
    ci(4,"216","150")
)

//val testable: TestCase? = ci(4,"","")
val testable: TestCase? = null

fun main() = runBlocking {
    if (testable != null) {
        val task = testable.executor(testable.input)
        println(task.a())
        println(task.b())
        return@runBlocking
    }
    val time = measureTimeMillis {
        val results = testCases.map { tc ->
            async(Dispatchers.Default) {
                val task = withContext(Dispatchers.IO) { tc.executor(tc.input) }
                val (answerA, exceptionA) = try {
                    task.a() to null
                } catch (e: Exception) {
                    null to e
                }

                val (answerB, exceptionB) = try {
                    task.b() to null
                } catch (e: Exception) {
                    null to e
                }

                TestResult(
                    tc.name,
                    tc.input,
                    tc.expectedA == answerA,
                    answerA,
                    exceptionA,
                    tc.expectedA,
                    tc.expectedB == answerB,
                    answerB,
                    exceptionB,
                    tc.expectedB,
                )
            }
        }
        val seperator = {
            print("|")
            print("-".repeat(6))
            print("+")
            print("-".repeat(ANSWER_SIZE+2))
            print("+")
            print("-".repeat(ANSWER_SIZE+2))
            println("|")
        }
        println()
        seperator()
        println("| TASK | ${"Answer A".padCenter(ANSWER_SIZE)} | ${"Answer B".padCenter(ANSWER_SIZE)} |")
        seperator()
        results.forEach { rA ->
            print("|")
            val r = rA.await()
            print(r.name.padCenter(6, ' '))
            print("| ")
            if (r.correctA) {
                print("OK")
                print((r.resultA ?: "").padStart(ANSWER_SIZE - 2, ' '))
            } else {
                print("F ")
                print((r.resultA ?: "").padStart(ANSWER_SIZE - 2, ' '))
            }
            print(" | ")
            if (r.correctB) {
                print("OK")
                print((r.resultB ?: "").padStart(ANSWER_SIZE - 2, ' '))
            } else {
                print("F ")
                print((r.resultB ?: "").padStart(ANSWER_SIZE - 2, ' '))
            }
            println(" |")

            if (!r.correctA) {
                println("A: Correct=${r.expectedA}")
            }
            if (r.exceptionA != null) {
                println("A: Exception=${r.exceptionA}")
            }
            if (!r.correctB) {
                println("B: Correct=${r.expectedB}")
            }
            if (r.exceptionB != null) {
                println("B: Exception=${r.exceptionB}")
            }

        }
        seperator()
    }
    println("Execution time: $time")
    println()
}

class TestCase(
    val name: String,
    val input: Input,
    val executor: (Input) -> Task,
    val expectedA: String,
    val expectedB: String
)

class TestResult(
    val name: String,
    val input: Input,
    val correctA: Boolean,
    val resultA: String?,
    val exceptionA: Throwable?,
    val expectedA: String,
    val correctB: Boolean,
    val resultB: String?,
    val exceptionB: Throwable?,
    val expectedB: String,
) {
    val correct = correctA && correctB
}


fun String.padCenter(targetLength: Int, char: Char = ' ') =
    if (this.length < targetLength) {
        val difference = targetLength - this.length
        val start = difference / 2
        this.padStart(start + this.length, char).padEnd(targetLength, char)
    } else this