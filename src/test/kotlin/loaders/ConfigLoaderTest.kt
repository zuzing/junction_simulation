package loaders

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.io.TempDir
import kotlin.test.Test
import java.nio.file.Path
import java.io.IOException

import datatypes.*

class ConfigLoaderTest {

    @TempDir
    lateinit var tempDir: Path

    @Test
    fun `loadConfigFromJson should load valid config successfully`() {
        val validJson = """
            {
              "numVehiclesPerStep": 5,
              "yellowLightDuration": 3,
              "maxAlgorithmDepth": 10,
              "lanes": [
                {
                  "laneId": "laneN1",
                  "entryDirection": "NORTH",
                  "permittedDirections": ["FORWARD", "LEFT"],
                  "signalType": "STANDARD_LIGHT"
                },
                {
                  "laneId": "laneE1",
                  "entryDirection": "EAST",
                  "permittedDirections": ["RIGHT"],
                  "signalType": "FILTER_LIGHT"
                }
              ]
            }
        """.trimIndent()


        val configFile = tempDir.resolve("valid_config.json").toFile()
        configFile.writeText(validJson)

        val config = ConfigLoader.loadFromJson(configFile.absolutePath)

        assertNotNull(config)
        assertEquals(5, config.numVehiclesPerStep)
        assertEquals(3, config.yellowLightDuration)
        assertEquals(2, config.lanes.size)

        assertEquals("laneN1", config.lanes[0].laneId)
        assertEquals(CardinalDirection.NORTH, config.lanes[0].entryDirection)
        assertEquals(listOf(RelativeDirection.FORWARD, RelativeDirection.LEFT), config.lanes[0].permittedDirections)
        assertEquals(SignalType.STANDARD_LIGHT, config.lanes[0].signalType)

        assertEquals("laneE1", config.lanes[1].laneId)
        assertEquals(CardinalDirection.EAST, config.lanes[1].entryDirection)
        assertEquals(listOf(RelativeDirection.RIGHT), config.lanes[1].permittedDirections)
        assertEquals(SignalType.FILTER_LIGHT, config.lanes[1].signalType)
    }

    @Test
    fun `loadConfigFromJson should throw IOException for non-existent file`() {
        val fakeFilePath = tempDir.resolve("missing.json").toString()

        assertThrows(IOException::class.java) {
            ConfigLoader.loadFromJson(fakeFilePath)
        }
    }

    @Test
    fun `loadConfigFromJson should throw IOException for JSON with incorrect structure or types`() {
        val wrongStructureJson = """
            {
              "numVehiclesPerStep": "five", // Incorrect type (String instead of Int)
              "yellowLightDuration": 3,
               "maxAlgorithmDepth": 10,
              "lanes": []
            }
        """.trimIndent()

        val configFile = tempDir.resolve("wrong_structure_config.json").toFile()
        configFile.writeText(wrongStructureJson)

        assertThrows(IOException::class.java) {
            ConfigLoader.loadFromJson(configFile.absolutePath)
        }
    }

    @Test
    fun `loadConfigFromJson should throw IOException for invalid JSON`() {
        val invalidJson = """
            {
                "numVehiclesPerStep": 5,
                "yellowLightDuration": 3,
                "maxAlgorithmDepth": 10,
            } // missing lanes array
            """.trimIndent()

            val configFile = tempDir.resolve("invalid_config.json").toFile()
            configFile.writeText(invalidJson)

            assertThrows(IOException::class.java) {
                ConfigLoader.loadFromJson(configFile.absolutePath)
            }
        }
}