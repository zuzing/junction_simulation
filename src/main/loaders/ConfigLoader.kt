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
            entryDirection = entryDirection,
            permittedDirections = permittedDirections,
            signalType = signalType
        )
    }
}

data class SimulationConfig(
    val numVehiclesPerGreen: Int,
    val yellowLightDuration: Int,
    val lanes: List<LaneConfig>
)

object ConfigLoader {
    fun loadConfig(filePath: String): SimulationConfig {
        val mapper = ObjectMapper().registerModule(KotlinModule())
        val file = File(filePath)

        try {
            return mapper.readValue(file, SimulationConfig::class.java)
        } catch (e: IOException) {
            throw IOException("Error loading configuration from $filePath: ${e.message}")
        }
    }
}