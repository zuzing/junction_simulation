package datatypes

import org.junit.jupiter.api.Assertions
import kotlin.test.Test

import junction.Lane

class LaneConfigTest {
    @Test
    fun `toLane should properly convert LaneConfig to Lane`() {

        val laneConfig = LaneConfig(
            laneId = "laneN1",
            entryDirection = CardinalDirection.NORTH,
            permittedDirections = listOf(RelativeDirection.FORWARD, RelativeDirection.RIGHT),
            signalType = SignalType.STANDARD_LIGHT
        )

        val lane: Lane = laneConfig.toLane()

        Assertions.assertEquals(laneConfig.laneId, lane.id)
        Assertions.assertEquals(laneConfig.permittedDirections, lane.permittedDirections)
        Assertions.assertEquals(lane.signalType, lane.signalType)
    }
}