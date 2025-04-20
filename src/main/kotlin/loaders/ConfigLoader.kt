package loaders

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.io.File
import java.io.IOException

import datatypes.CardinalDirection
import datatypes.RelativeDirection
import datatypes.SignalType
import junction.Lane

data class LaneConfig(
    val laneId: String,
    val entryDirection: CardinalDirection,
    val permittedDirections: List<RelativeDirection>,
    val signalType: SignalType)
{
    fun toLane(): Lane {
        return Lane(
            id = laneId,
            permittedDirections = permittedDirections,
            signalType = signalType
        )
    }
}

data class SimulationConfig(
    val numVehiclesPerStep: Int,
    val yellowLightDuration: Int,
    val lanes: List<LaneConfig>
)

object ConfigLoader {
    private val mapper = ObjectMapper().registerModule(KotlinModule())
    fun loadFromJson(filePath: String): SimulationConfig {
        val file = File(filePath)

        if (!file.exists()) {
            throw IOException("Configuration file not found: $filePath")
        }

        try {
            return mapper.readValue(file, SimulationConfig::class.java)
        } catch (e: Exception) {
            throw IOException("Error loading configuration from $filePath: ${e.message}", e)
        }
    }
}