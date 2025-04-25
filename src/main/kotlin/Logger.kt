

import junction.Crossroad
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

@Serializable
private data class StepStatus(val leftVehicles: List<String>)

@Serializable
private data class LogOutput(val stepStatuses: List<StepStatus>)

class Logger(private val pathFile: String) {
    private val stepStatuses = mutableListOf<StepStatus>()

    fun logVehiclesLeft(crossroad: Crossroad) {
        val left = crossroad.getVehicles()
            .map { it.id }.sorted()

        stepStatuses.add(StepStatus(leftVehicles = left))
    }

    fun finish() {
        val output = LogOutput(stepStatuses = stepStatuses)
        val json = Json { prettyPrint = true }
        File(pathFile).writeText(json.encodeToString(output))
    }
}