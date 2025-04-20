package loaders

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.io.TempDir
import kotlin.test.Test
import java.nio.file.Path
import java.io.IOException

import datatypes.*
import instructions.*


class InstructionLoaderTest {

    @TempDir
    lateinit var tempDir: Path

    @Test
    fun `loadFromJson should correctly parse instructions`() {
        val validJson = """
            {
              "commands": [
                {
                  "type": "addVehicle",
                  "vehicleId": "vehicle1",
                  "startRoad": "north",
                  "endRoad": "east"
                },
                {
                  "type": "step"
                }
              ]
            }
        """.trimIndent()

        val instrFile = tempDir.resolve("valid_instructions.json").toFile()
        instrFile.writeText(validJson)

        val instructions = InstructionLoader.loadFromJson(instrFile.absolutePath)

        assertEquals(2, instructions.size)

        val addVehicle = instructions[0]
        assertTrue(addVehicle is AddVehicleInstruction)

        addVehicle as AddVehicleInstruction

        assertEquals("vehicle1", addVehicle.vehicleId)
        assertEquals(CardinalDirection.NORTH, addVehicle.startDirection)
        assertEquals(RelativeDirection.RIGHT, addVehicle.movementDirection)

        val step = instructions[1]
        assertTrue(step is StepInstruction)
    }

    @Test
    fun `loadFromJson should throw when file is missing`() {
        val fakeFilePath = tempDir.resolve("missing.json").toString()
        assertThrows(IOException::class.java) {
            InstructionLoader.loadFromJson(fakeFilePath)
        }
    }

    @Test
    fun `loadFromJson should throw when commands field is missing`() {
        val json = """ { "notCommands": [] } """
        val filePath = tempDir.resolve("missing_commands.json").toString()
        val instrFile = tempDir.resolve("missing_commands.json").toFile()
        instrFile.writeText(json)

        val exception = assertThrows(IOException::class.java) {
            InstructionLoader.loadFromJson(filePath)
        }

        assert(exception.message!!.contains("missing 'commands' field", ignoreCase = true))
    }

    @Test
    fun `loadFromJson should throw on unknown instruction type`() {
        val json = """
            {
              "commands": [
                { "type": "remove" }
              ]
            }
        """.trimIndent()

        val filePath = tempDir.resolve("unknown_type.json").toString()
        val instrFile = tempDir.resolve(filePath).toFile()
        instrFile.writeText(json)

        val exception = assertThrows(IOException::class.java) {
            InstructionLoader.loadFromJson(filePath)
        }

        assert(exception.message!!.contains("Unknown instruction type", ignoreCase = true))
    }

    @Test
    fun `loadFromJson should throw if addVehicle instruction is missing required fields`() {
        val jsonMissingVehicleId = """
            {
              "commands": [
                {
                  "type": "addVehicle",
                  "startRoad": "NORTH",
                  "endRoad": "EAST"
                }
              ]
            }
        """.trimIndent()

        val filePath = tempDir.resolve("missing_vehicleId.json").toString()
        val instrFile = tempDir.resolve(filePath).toFile()
        instrFile.writeText(jsonMissingVehicleId)

        assertThrows(IOException::class.java) {
            InstructionLoader.loadFromJson(filePath)
        }
    }

    @Test
    fun `loadFromJson should throw if addVehicle instruction contains incorrect types`() {
        val json = """
            {
              "commands": [
                {
                  "type": "addVehicle",
                  "vehicleId": "vehicle1",
                  "startRoad": "NORTH",
                  "endRoad": "LEFT"
                }
              ]
            }
        """.trimIndent()

        val filePath = tempDir.resolve("incorrect_types.json").toString()
        val instrFile = tempDir.resolve(filePath).toFile()
        instrFile.writeText(json)

        assertThrows(IOException::class.java) {
            InstructionLoader.loadFromJson(filePath)
        }
    }
}