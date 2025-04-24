package instructions

import junction.Crossroad

class StepInstruction(
) : Instruction {
    override fun runOn(crossroad: Crossroad) {
        crossroad.step()
    }
}