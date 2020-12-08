package lv.n3o.aoc2020.tasks

import lv.n3o.aoc2020.*

class T08(input: Input) : Task(input) {
    val program = input.asProgram()

    fun getTerminationOrLoop(input: List<Instruction>): State {
        val computer = Computer(input)
        var lastGoodState = computer.state
        val seenLines = mutableSetOf(lastGoodState.ip)
        while (true) {
            val newState = computer.step()
            if (seenLines.add(newState.ip)) {
                lastGoodState = newState
                if (newState.ip == program.size) break
            } else {
                break
            }
        }

        return lastGoodState
    }

    override suspend fun a(): String {
        return getTerminationOrLoop(program).acc.toString()
    }

    override suspend fun b(): String {
        for (i in program.indices) {
            val newProgram = program.toMutableList()
            val instruction = newProgram[i]
            if (instruction.op == Operation.JMP) {
                newProgram[i] = Instruction(Operation.NOP, instruction.value)
            } else if (instruction.op == Operation.NOP) {
                newProgram[i] = Instruction(Operation.JMP, instruction.value)
            } else {
                continue
            }
            val endState = getTerminationOrLoop(newProgram)

            if (endState.ip == newProgram.size) return endState.acc.toString()
        }
        error("No valid programs")
    }
}