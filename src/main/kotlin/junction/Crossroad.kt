package junction

import loaders.LaneConfig
import datatypes.*

class Crossroad
{
    var lanes: Map<CardinalDirection, List<Lane>>

    constructor(lanes: Map<CardinalDirection, List<Lane>>){
        this.lanes = lanes
    }
    constructor(lanesConfiguration: List<LaneConfig>){
        this.lanes = lanesConfiguration
            .groupBy { it.entryDirection }
            .mapValues { entry ->
                entry.value.map { laneConfig ->
                    laneConfig.toLane()
                }
            }
    }



//    companion object {
//        public val unconflictedLanes: List<List<Lane>> = getUnconflictedLanes()
//
//        private fun getUnconflictedLanes(): List<List<Lane>> {
//
//            TODO("Not yet implemented")
//        }
//    }
}