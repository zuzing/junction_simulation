package junction

import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test

import datatypes.*
import instructions.AddVehicleInstruction

class CrossroadTest {
    private val dummySignalController = SignalController(emptyList(), 1)

    @Test
    fun `addVehicle adds vehicle to matching lane`() {
        val lane = Lane(
            id = "laneN1",
            permittedDirections = listOf(RelativeDirection.FORWARD),
            signalType = SignalType.STANDARD_LIGHT
        )
        val crossroad = Crossroad(
            lanes = mapOf(CardinalDirection.NORTH to listOf(lane)),
            signalController = dummySignalController
        )

        val vehicle = Vehicle(
            "vehicle1",
            RelativeDirection.FORWARD
        )
        crossroad.addVehicle(vehicle, CardinalDirection.NORTH)

        assertTrue(lane.isNotEmpty(), "Lane should have received the vehicle")
        val popped = lane.popVehicle()
        assertNotNull(popped)
        assertEquals("V1", popped!!.id)
    }

    @Test
    fun `addVehicle throws when no matching lane exists`() {
        val crossroad = Crossroad(
            lanes = emptyMap(),
            signalController = dummySignalController
        )
        val vehicle = Vehicle(
            id = "V2",
            direction = RelativeDirection.LEFT
        )
        assertThrows(IllegalArgumentException::class.java) {
            crossroad.addVehicle(vehicle, CardinalDirection.EAST)
        }
    }

    @Test
    fun `step on empty intersection does not throw`() {
        val crossroad = Crossroad(
            lanes = emptyMap(),
            signalController = dummySignalController
        )
        assertDoesNotThrow {
            crossroad.step()
        }
    }

    @Test
    fun `AddVehicleInstruction delegates to crossroad`() {
        val lane = Lane(
            id = "L3",
            permittedDirections = listOf(RelativeDirection.LEFT),
            signalType = SignalType.STANDARD_LIGHT
        )
        val crossroad = Crossroad(
            lanes = mapOf(CardinalDirection.SOUTH to listOf(lane)),
            signalController = dummySignalController
        )

        // when
        val instr = AddVehicleInstruction(
            vehicleId = "V3",
            startDirection = CardinalDirection.SOUTH,
            movementDirection = RelativeDirection.LEFT
        )
        instr.runOn(crossroad)

        // then vehicle is in that lane
        assertTrue(lane.isNotEmpty())
        assertEquals("V3", lane.popVehicle()!!.id)
    }

    @Test
    fun `step processes vehicles correctly`() {
        val laneN = Lane(
            id = "laneN1",
            permittedDirections = listOf(RelativeDirection.FORWARD),
            signalType = SignalType.STANDARD_LIGHT
        )
        val laneE = Lane(
            id = "laneE1",
            permittedDirections = listOf(RelativeDirection.RIGHT, RelativeDirection.FORWARD),
            signalType = SignalType.STANDARD_LIGHT
        )
        val crossroad = Crossroad(
            lanes = mapOf(
                CardinalDirection.NORTH to listOf(laneN),
                CardinalDirection.EAST to listOf(laneE)
            ),
            signalController = dummySignalController
        )

        laneN.addVehicle(Vehicle("vehicle1", RelativeDirection.FORWARD))
        laneE.addVehicle(Vehicle("vehicle2", RelativeDirection.RIGHT))

        crossroad.signalController.greenLights = listOf(
            SignalPattern(
                startDirection = CardinalDirection.NORTH,
                movementDirection = RelativeDirection.FORWARD,
                type = Priority.STRONG,
                conflictedWith = null
            ),
            SignalPattern(
                startDirection = CardinalDirection.EAST,
                movementDirection = RelativeDirection.RIGHT,
                type = Priority.STRONG,
                conflictedWith = null
            )
        )

        crossroad.step()

        assertEquals(0, laneN.queueLength())
        assertEquals(1, laneE.queueLength())

        crossroad.signalController.greenLights = listOf(
            SignalPattern(
                startDirection = CardinalDirection.NORTH,
                movementDirection = RelativeDirection.FORWARD,
                type = Priority.STRONG,
                conflictedWith = null
            ),
        )

        laneN.addVehicle(Vehicle("vehicle3", RelativeDirection.FORWARD))
        laneE.addVehicle(Vehicle("vehicle4", RelativeDirection.FORWARD))

        crossroad.step()

        assertEquals(0, laneN.queueLength())
        assertEquals(1, laneE.queueLength())
    }

    @Test
    fun `step resolves conflicts correctly`() {
        val laneN = Lane(
            id = "laneN1",
            permittedDirections = listOf(RelativeDirection.FORWARD, RelativeDirection.LEFT),
            signalType = SignalType.STANDARD_LIGHT
        )
        val laneS = Lane(
            id = "laneS1",
            permittedDirections = listOf(RelativeDirection.FORWARD, RelativeDirection.LEFT),
            signalType = SignalType.STANDARD_LIGHT
        )
        val crossroad = Crossroad(
            lanes = mapOf(
                CardinalDirection.NORTH to listOf(laneN),
                CardinalDirection.SOUTH to listOf(laneS)
            ),
            signalController = dummySignalController
        )

        laneN.addVehicle(Vehicle("vehicle1", RelativeDirection.LEFT))
        laneS.addVehicle(Vehicle("vehicle2", RelativeDirection.LEFT))


        val northLeft = SignalPattern(
            type             = Priority.WEAK,
            startDirection   = CardinalDirection.NORTH,
            movementDirection= RelativeDirection.LEFT,
            conflictedWith   = null
        )
        val southLeft = SignalPattern(
            type             = Priority.WEAK,
            startDirection   = CardinalDirection.SOUTH,
            movementDirection= RelativeDirection.LEFT,
            conflictedWith   = null
        )

        northLeft.conflictedWith = listOf(southLeft)
        southLeft.conflictedWith = listOf(northLeft)

        crossroad.signalController.greenLights = listOf(northLeft, southLeft)

        crossroad.step()

        assertEquals(1, laneN.queueLength() + laneS.queueLength())
    }
}