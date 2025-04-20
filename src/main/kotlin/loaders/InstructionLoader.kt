package loaders

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.io.File
import java.io.IOException

import instructions.*
import datatypes.*

object InstructionLoader {

    private val mapper = ObjectMapper().registerModule(KotlinModule())

    fun loadFromJson(filePath: String): List<Instruction> {
        val file = File(filePath)

        if (!file.exists()) {
            throw IOException("Configuration file not found: $filePath")
        }

        try {
            val json: JsonNode = mapper.readTree(file)
            val commands= json.get("commands")
                ?: throw IOException("Configuration file is missing 'commands' field: $filePath");

            return commands.map { command ->
                when (val type = command.get("type").asText()) {
                    "addVehicle" -> {
                        val vehicleId = command["vehicleId"]?.asText()
                            ?: throw IOException("Missing 'vehicleId' in addVehicle instruction.")
                        val start = CardinalDirection.fromString(command["startRoad"]?.asText()
                            ?: throw IOException("Missing 'startRoad' in addVehicle instruction."))
                        val end = CardinalDirection.fromString(command["endRoad"]?.asText()
                            ?: throw IOException("Missing 'endRoad' in addVehicle instruction."))
                        val movement = RelativeDirection.fromCardinalDirection(start, end)
                        AddVehicleInstruction(vehicleId, start, movement)
                    }
                    "step" -> StepInstruction()
                    else -> throw IOException("Unknown instruction type: $type")
                }
            }
        } catch (e: Exception) {
            throw IOException("Error loading configuration from $filePath: ${e.message}", e)
        }
    }
}