package junction

import datatypes.*
class Crossroad(
    public val lanes: Map<CardinalDirection, List<Lane>>,
    public val signalController: SignalController,
    private val numVehiclesPerStep: Int = 1
) {
    constructor(
        lanesConfiguration: List<LaneConfig>,
        signalController: SignalController,
        numVehiclesPerStep: Int
    ) : this(
        lanesConfiguration
            .groupBy { it.entryDirection }
            .mapValues { (_, configs) ->
                configs.map { it.toLane() }
            },
        signalController,
        numVehiclesPerStep
    )

    constructor(other: Crossroad) : this(
        other.lanes.mapValues { (_, laneList) ->
            laneList.toList()
        },
        SignalController(other.signalController),
        other.numVehiclesPerStep
    )

    /*
    TODO: make it lanesForPattern returning a list of lanes
     */
    private fun laneForPattern(pattern: SignalPattern): Lane =
        lanes[pattern.startDirection]
            ?.first { pattern.movementDirection in it.permittedDirections }
            ?: throw IllegalArgumentException("No lane for pattern $pattern")

    /*
    Currently only checks for weak left turns. TODO: make more general.
     */
    private fun findConflicts(signalPatterns: List<SignalPattern>): Map<SignalPattern, List<SignalPattern>> {
        val conflictsResult = mutableMapOf<SignalPattern, MutableList<SignalPattern>>()

        val weakLeftTurns = signalPatterns.filter {
            it.type == Priority.WEAK && it.movementDirection == RelativeDirection.LEFT
        }
        if (weakLeftTurns.isEmpty()) {
            return emptyMap()
        }

        for (weakLeftTurn in weakLeftTurns) {
            val oppositeDirection = weakLeftTurn.startDirection.getOpposite()

            val conflictingPatterns = signalPatterns.filter { otherPattern ->
                otherPattern.startDirection == oppositeDirection
            }
            if (conflictingPatterns.isNotEmpty()) {
                conflictsResult.computeIfAbsent(weakLeftTurn) { mutableListOf() }
                    .addAll(conflictingPatterns)
            }
        }

        return conflictsResult
    }

    /**
     * Advance one tick:
     *  1. Let vehicles go on weak signals if there are no conflicts
     *  2. Then let vehicles go on strong signals
     *  3. Finally, tick the lights forward
     */
    fun step() {
        /* TODO: consider if there are multiple lanes in the same direction in conflict
         */
        val conflictedWith = findConflicts(signalController.greenLights)
        val weakPatterns   = signalController.greenLights.filter  { it.type == Priority.WEAK   }
        val strongPatterns = signalController.greenLights.filter  { it.type == Priority.STRONG }

        val servedWeakGroups = mutableSetOf<SignalPattern>()

        // 1) Process WEAK patterns, one per conflict‐group
        outer@ for (pattern in weakPatterns) {
            if (pattern in servedWeakGroups) continue@outer

            // gather its conflicting patterns (only WEAK vs. WEAK)
            val conflicts = (conflictedWith[pattern] ?: emptyList())
                .filter { it.type == Priority.WEAK }

            // --- STRONG conflict check ---
            // if any of its conflicts is STRONG‐green with a waiting vehicle going the same way, skip
            val anyStrongBlocking = (conflictedWith[pattern] ?: emptyList())
                .filter { it.type == Priority.STRONG }
                .any { conflict ->
                    val lane = laneForPattern(conflict)
                    lane.isNotEmpty() &&
                            lane.getFirstVehicleDirection() == conflict.movementDirection
                }
            if (anyStrongBlocking) continue@outer

            // --- WEAK vs WEAK resolution ---
            // include ourselves in the group
            val weakGroup = (listOf(pattern) + conflicts).distinct()
            // pick the one with the longest queue
            val winner = weakGroup.maxByOrNull { laneForPattern(it).queueLength() } ?: pattern
            // mark entire group as served
            servedWeakGroups += weakGroup
            // if we're not the winner, skip moving
            if (pattern != winner) continue@outer

            // finally, let one vehicle go
            laneForPattern(pattern).popVehicle()
        }

        // 2) Process STRONG patterns (no conflicts amongst themselves)
        for (pattern in strongPatterns) {
            laneForPattern(pattern).popVehicle()
        }

        // 3) Advance the lights
        signalController.step()
    }

    /**
     * Inserts the vehicle into one of the lanes matching its entry & movement.
     * If multiple lanes permit the movement, picks one at random.
     */
    fun addVehicle(vehicle: Vehicle, entryDirection: CardinalDirection) {
        val candidates = lanes[entryDirection]
            ?.filter { lane -> vehicle.direction in lane.permittedDirections }
            .orEmpty()

        if (candidates.isNotEmpty()) {
            candidates.random().addVehicle(vehicle)
        } else {
            throw IllegalArgumentException("No lane for vehicle $vehicle")
        }
    }

    fun getVehicles(): List<Vehicle> {
        return lanes.values.flatten()
            .flatMap { it.getVehicles() }
    }
}