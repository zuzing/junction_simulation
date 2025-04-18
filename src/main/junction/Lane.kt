class Lane(
    val id: String,
    val entryDirection: CardinalDirection,
    val permittedDirections: List<RelativeDirection>
    val signalType: SignalType)
{
    private val vehicles: MutableList<Vehicle> = mutableListOf()

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

    fun getVehicles(): List<Vehicle> {
        return vehicles
    }
}