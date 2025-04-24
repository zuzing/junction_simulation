package instructions

import datatypes.*
import junction.Crossroad

class AddVehicleInstruction(
    val vehicleId: String,
    val startDirection: CardinalDirection,
    val movementDirection: RelativeDirection
) : Instruction {
    override fun runOn(crossroad: Crossroad) {
        val vehicle = Vehicle(
            id = vehicleId,
            direction = movementDirection
        )
        crossroad.addVehicle(vehicle, startDirection)
    }
}