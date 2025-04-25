package instructions

import datatypes.CardinalDirection
import datatypes.RelativeDirection
import datatypes.Vehicle
import junction.Crossroad

class AddVehicleInstruction(
    val vehicleId: String,
    val startDirection: CardinalDirection,
    val movementDirection: RelativeDirection
) : Instruction {
    init {
        if (movementDirection == RelativeDirection.BACKWARDS) {
            throw IllegalArgumentException("Movement direction BACKWARDS is not yet supported")
        }
    }
    override fun runOn(crossroad: Crossroad) {
        val vehicle = Vehicle(
            id = vehicleId,
            direction = movementDirection
        )
        crossroad.addVehicle(vehicle, startDirection)
    }
}