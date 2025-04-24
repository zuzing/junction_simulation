package loaders

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.io.File
import java.io.IOException

import datatypes.SimulationConfig

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