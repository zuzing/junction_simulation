package instructions

import junction.Crossroad

interface Instruction{
    fun runOn(crossroad: Crossroad)
}