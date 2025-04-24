package datatypes

import junction.Lane

data class LaneConfig(
    val laneId: String,
    val entryDirection: CardinalDirection,
    val permittedDirections: List<RelativeDirection>,
    val signalType: SignalType
)
{
    fun toLane(): Lane {
        return Lane(
            id = laneId,
            permittedDirections = permittedDirections,
            signalType = signalType
        )
    }
}