@file:JvmName("Main")

package lv.n3o.aoc2020

import kotlinx.coroutines.*
import kotlin.math.min
import kotlin.system.measureTimeMillis

private const val FIRST_DAY = 1
private const val LAST_DAY = 25
private const val ANSWER_SIZE = 48
private const val TEST_NEW_TASKS = true
private const val REPEAT_RUNS = 1

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
    ci(4, "216", "150"),
    ci(5, "996", "671"),
    ci(6, "6430", "3125"),
    ci(7, "272", "172246"),
    ci(8, "1586", "703"),
    ci(9, "25918798", "3340942"),
    ci(10, "2030", "42313823813632"),
    ci(11, "2344", "2076"),
    ci(12, "2879", "178986"),
    ci(13, "3606", "379786358533423"),
    ci(14, "6559449933360", "3369767240513"),
    ci(15, "468", "1801753"),
    ci(16, "29759", "1307550234719"),
    ci(17, "213", "1624"),
    ci(18, "30753705453324", "244817530095503"),
    ci(19, "111", "343"),
    ci(20, "22878471088273", "1680"),
    ci(21, "2595", "thvm,jmdg,qrsczjv,hlmvqh,zmb,mrfxh,ckqq,zrgzf"),
)

fun main() = runBlocking {
    //If tasks not registered are present, execute first such task
    if (TEST_NEW_TASKS) if (testNewTasks()) return@runBlocking
    var minTime = Long.MAX_VALUE
    (1..REPEAT_RUNS).forEach { runId ->
        val time = measureTimeMillis {
            val results = testCases.map(this::executeTaskAsync)


            println()
            printTableSeparator()
            println("| TASK | ${"Answer A".padCenter(ANSWER_SIZE)} | ${"Answer B".padCenter(ANSWER_SIZE)} |")
            printTableSeparator()

            results.forEach { rA ->
                print("|")
                val r = rA.await()
                print(r.name.padCenter(6, ' '))

                print("| ")
                print(if (r.correctA) "OK" else "F ")
                print((r.resultA ?: "").padStart(ANSWER_SIZE - 2, ' '))
                print(" | ")
                print(if (r.correctB) "OK" else "F ")
                print((r.resultB ?: "").padStart(ANSWER_SIZE - 2, ' '))
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
            printTableSeparator()
        }
        minTime = min(minTime, time)
        if (REPEAT_RUNS != 1) {
            println("RUN ${runId.toString().padStart(5, '0')}\tExecution time: $time")
        } else {
            println("Execution time: $time")
        }
        println()
    }
    if (REPEAT_RUNS != 1) {
        println("Fastest run: $minTime")
        println()
    }
}

fun CoroutineScope.executeTaskAsync(tc: TestCase) = async(Dispatchers.Default) {
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

suspend fun testNewTasks(): Boolean {
    (FIRST_DAY..LAST_DAY).forEach { i ->
        try {
            val c = ci(i, "", "")
            if (testCases.none { it.name == c.name }) {

                val time = measureTimeMillis {
                    val task = c.executor(c.input)
                    task.debugListener = { s ->
                        println(s)
                    }
                    println(task.a())
                    println(task.b())
                }
                println("Time: $time")
                return true
            }
        } catch (ignored: ClassNotFoundException) {
        }
    }
    return false
}

fun printTableSeparator() {
    print("|")
    print("-".repeat(6))
    print("+")
    print("-".repeat(ANSWER_SIZE + 2))
    print("+")
    print("-".repeat(ANSWER_SIZE + 2))
    println("|")
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