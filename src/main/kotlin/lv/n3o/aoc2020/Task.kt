package lv.n3o.aoc2020

import lv.n3o.aoc2020.coords.C2

typealias DebugListener = (String) -> Unit

abstract class Input {
    private val input: String by lazy { readInput() }
    abstract fun readInput(): String

    fun raw() = input

    fun asLines(trim: Boolean = true): List<String> = input.split("\n").filter { it.isNotBlank() }.let {
        if (trim) it.map(String::trim) else it
    }

    fun asListOfLongs(): List<Long> = asLines().map {
        it.trim().toLong()
    }

    fun asCoordGrid(): Map<C2, Char> =
        asLines().flatMapIndexed { y, l ->
            l.mapIndexed { x, c ->
                C2(x, y) to c
            }
        }.toMap()
}

class ClasspathInput(val name: String) : Input() {
    override fun readInput(): String {
        with(this::class.java.classLoader.getResourceAsStream(name)!!) {
            return bufferedReader(Charsets.UTF_8).readText()
        }
    }
}

abstract class Task(val input: Input) {
    var debugListener: DebugListener? = null

    fun isLoggerOn() = debugListener != null
    fun log(vararg things: Any?) {
        debugListener ?: return

        val logline = things.map { it ?: "<null>" }.joinToString(" ") { it.toString() }
        val className = this::class.java.canonicalName.split('.').takeLast(2)[0].padEnd(3, ' ')
        val time = timeFromApplicationStart.formatTime()
        val methodName = Thread.currentThread().stackTrace[2].methodName

        debugListener?.let { it(("$time ($className.$methodName): $logline\n")) }
    }

    fun log(scope: () -> String) {
        if (isLoggerOn()) log(scope())
    }

    open suspend fun a(): String = ""
    open suspend fun b(): String = ""
}