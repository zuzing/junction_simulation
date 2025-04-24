package algorithm

import algorithm.evaluators.Evaluator
import datatypes.SignalPattern
import instructions.Instruction
import instructions.StepInstruction
import junction.Crossroad

class TrafficController(private val evaluator: Evaluator) {

    fun performAlgorithm(crossroad: Crossroad, instructions: List<Instruction>, depth: Int): List<SignalPattern> {
        var bestCombination: List<SignalPattern> = emptyList()
        var bestScore = Int.MIN_VALUE

        val working = Crossroad(crossroad)
        val queue = instructions.toMutableList()

        // run instructions until step
        while (queue.isNotEmpty()) {
            val instr = queue.removeAt(0)
            if (instr is StepInstruction) break
            instr.runOn(working)
        }

        for (combination in crossroad.signalController.getUnconflictedLanesCombinations()) {
            val copy = Crossroad(working)
            copy.signalController.changeLights(combination)

            copy.step()

            val score = findOptimaSignalPattern(copy, queue, depth - 1)

            if (score > bestScore) {
                bestScore = score
                bestCombination = combination
            }
        }

        return bestCombination
    }

    private fun findOptimaSignalPattern(crossroad: Crossroad, instructions: MutableList<Instruction>, depth: Int): Int {
        if(depth == 0 ){
            return evaluator.eval(crossroad)
        }

        val queue = instructions.toMutableList()
        val working = Crossroad(crossroad)
        while (queue.isNotEmpty()) {
            val instr = queue.removeAt(0)
            if (instr is StepInstruction) {
                working.step()
                break
            }
            instr.runOn(working)
        }

        var bestScore = Int.MIN_VALUE
        for (combination in working.signalController.getUnconflictedLanesCombinations()) {
            val copy = Crossroad(working)
            copy.signalController.changeLights(combination)
            val score = findOptimaSignalPattern(copy, queue, depth - 1)
            if (score > bestScore) {
                bestScore = score
            }
        }

        return bestScore
    }
}