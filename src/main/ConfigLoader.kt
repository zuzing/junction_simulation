import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.io.File
import java.io.IOException

data class LaneConfig(
    val LaneId: String,
    val entryDirection: CardinalDirection,
    val permittedDirections: List<RelativeDirection>
    val signalType: SignalType)
){
    fun toLane(id: String): Lane {
        return Lane(
            id = LaneId,
            entryDirection = entryDirection,
            permittedDirections = permittedDirections,
            signalType = signalType
        )
    }
}

data class SimulationConfig(
    val numVehiclesPerGreen: Int,
    val yellowLightDuration: Int,
    val Lanes: List<LaneConfig>
)

object ConfigLoader {
    fun loadConfig(filePath: String): SimulationConfig {
        val mapper = ObjectMapper().registerModule(KotlinModule())
        val file = File(filePath)

        try {
            return mapper.readValue(file, SimulationConfig::class.java)
        } catch (e: IOException) {
            throw e("Error loading configuration from $filePath: ${e.message}")
        }
    }
}