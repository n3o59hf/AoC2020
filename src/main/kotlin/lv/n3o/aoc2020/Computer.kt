package lv.n3o.aoc2020

typealias Value = Long

enum class Operation(val txt: String, val action: (State, Value) -> State) {
    ACC("acc", { state, value -> state.copy(acc = state.acc + value) }),
    JMP("jmp", { state, value -> state.copy(ip = (state.ip + value - 1).toInt()) }),
    NOP("nop", { state, _ -> state });

    companion object {
        val MAPPING = values().map { it.txt to it }.toMap()
    }
}

data class State(
    val acc: Value,
    val ip: Int
) {
    fun inc() = copy(ip = ip + 1)
}

data class Instruction(val op: Operation, val value: Value)

class Computer(val program: List<Instruction>, initial: State = State(0, 0)) {
    var state: State = initial
        private set

    fun step(): State {
        val instruction = program[state.ip]
        state = instruction.op.action(state, instruction.value).inc()
        return state
    }
}

fun String.asInstruction(): Instruction {
    val (op, value) = split(" ")
    return Instruction(
        Operation.MAPPING[op] ?: error("Unknown OP: $op"),
        value.toValue()
    )
}

fun String.toValue() : Value = toLong()