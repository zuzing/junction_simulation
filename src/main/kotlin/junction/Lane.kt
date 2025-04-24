package junction

import datatypes.RelativeDirection
import datatypes.SignalType
import datatypes.Vehicle

class Lane(
    val id: String,
    val permittedDirections: List<RelativeDirection>,
    val signalType: SignalType)
{
    private val vehicles: MutableList<Vehicle> = mutableListOf()

    fun isNotEmpty(): Boolean{
     return vehicles.isNotEmpty()

    }

    fun queueLength(): Int {
        return vehicles.size
    }

    fun addVehicle(vehicle: Vehicle) {
        vehicles.add(vehicle)
    }

    fun popVehicle(): Vehicle? {
        return if (vehicles.isNotEmpty()) {
            vehicles.removeAt(0)
        } else {
            null
        }
    }

    fun getFirstVehicleDirection(): RelativeDirection? {
        return if (vehicles.isNotEmpty()) {
            vehicles[0].direction
        } else {
            null
        }
    }

    fun getVehicles(): List<Vehicle> {
        return vehicles
    }
}