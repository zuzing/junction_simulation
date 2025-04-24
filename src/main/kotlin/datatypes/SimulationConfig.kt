package datatypes

data class SimulationConfig(
    val numVehiclesPerStep: Int,
    val yellowLightDuration: Int,
    val maxAlgorithmDepth: Int,
    val lanes: List<LaneConfig>
)