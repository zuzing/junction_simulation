package instructions

import datatypes.*
import junction.Crossroad

class AddVehicleInstruction(
    val vehicleId: String,
    val startDirection: CardinalDirection,
    val movementDirection: RelativeDirection
) : Instruction {
    override fun runOn(crossroad: Crossroad) {
        TODO("Not yet implemented")
    }
}