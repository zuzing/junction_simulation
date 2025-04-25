package junction

import datatypes.*

class SignalController private constructor(
    private val allSignalPatternsCombinations: List<List<SignalPattern>>,
    private val yellowLightDuration: Int,
    private var redLights: List<SignalPattern>,
    private var yellowLights: Map<SignalPattern, Int>,
    private var yellowRedLights: Map<SignalPattern, Int>,
    public var greenLights: List<SignalPattern>
) {
    constructor(laneConfig: List<LaneConfig>, yellowLightDuration: Int) : this(
        generateUnconflictedLanesCombinations(laneConfig),
        yellowLightDuration,
        allLightsCombinations(laneConfig),
        emptyMap(),
        emptyMap(),
        emptyList()
    )


    constructor(other: SignalController) : this(
        other.allSignalPatternsCombinations,
        other.yellowLightDuration,
        other.redLights.toList(),
        other.yellowLights.toMap(),
        other.yellowRedLights.toMap(),
        other.greenLights.toList()
    )

    /** Advance one “tick”: decrement all yellows, and flip any that hit 0. */
    fun step() {
        // 1) decrement & split
        val updatedY = yellowLights.mapValues { it.value - 1 }
        val updatedYR = yellowRedLights.mapValues { it.value - 1 }

        // 2) which just turned red?
        val justRed = updatedY.filterValues { it <= 0 }.keys
        // 3) which just turned green?
        val justGreen = updatedYR.filterValues { it <= 0 }.keys

        // 4) prune out zero’d entries
        yellowLights    = updatedY   .filterValues { it >  0 }
        yellowRedLights = updatedYR  .filterValues { it >  0 }

        // 5) update the main lists
        redLights   = (redLights   + justRed)   - justGreen
        greenLights = (greenLights + justGreen) - justRed
    }

    /**
     * Begin transitioning between the current green set and `newLights`:
     *  • any currently red in `newLights`: red → yellow+red → green
     *  • any currently green not in `newLights`: green → yellow → red
     */
    fun changeLights(newLights: List<SignalPattern>) {
        // red→green
        val toYellowRed = newLights.filter { it in redLights }
        yellowRedLights = yellowRedLights + toYellowRed.associateWith { yellowLightDuration }
        redLights       = redLights - toYellowRed

        // green→red
        val toYellow = greenLights.filter { it !in newLights }
        yellowLights  = yellowLights + toYellow.associateWith { yellowLightDuration }
        greenLights   = greenLights - toYellow
    }

    /**
     * Among all precomputed combinations of patterns,
     *  • exclude any combination containing a lane in yellowLights,
     *  • if there are lanes queued to turn green, require they all appear,
     *  • otherwise, return every combination.
     */
    fun getUnconflictedLanesCombinations(): List<List<SignalPattern>> {
        return allSignalPatternsCombinations.filter { combo ->
            // no conflicts with yellow→red
            yellowLights.keys.none { it in combo }
                    // and if someone is queued to go green, it must be in the combo
                    && (yellowRedLights.isEmpty()
                    || yellowRedLights.keys.all { it in combo })
        }
    }

companion object {
    private fun generateUnconflictedLanesCombinations(lanesConfig: List<LaneConfig>): List<List<SignalPattern>> {
//        should generate all maximal combinations of possible non intersecting Patterns
//        signal pattern is not lane specific but direction specific (e.g. all North lanes that have possibility to go stright can go straight at the same time)
//weak and strong patterns can coexist, shouldnt influence each other
        //conflict logic check is done elsewhere

//        STANDARD_LIGHT, // [STRONG: STRAIGHT, RIGHT; WEAK: LEFT, BACKWARDS;;;; STRONG: WEAK:]
//        FILTER_LIGHT, // STRONG: STRAIGHT, RIGHT; WEAK: LEFT, BACKWARDS;;;;;; STRONG: WEAK: RIGHT
//        ARROW_LIGHT, //STRONG: [STRAIGHT, RIGHT, LEFT, BACKWARDS;;;; STRONG: WEAK:]

        // for each direction ["NORTH", "EAST", "SOUTH", "WEST"]
        // for each lane in lanesConfig in that direction
        // TODO("Not yet implemented")

        val buildingBlocks = allLightsCombinations(lanesConfig)

        val allCombinations = mutableListOf<List<SignalPattern>>()

        allCombinations.addAll(listOf(buildingBlocks.slice(0..5)))
        allCombinations.addAll(listOf(buildingBlocks.slice(6..11)))

        return allCombinations
    }

    private fun allLightsCombinations(lanesConfig: List<LaneConfig>): List<SignalPattern> {
        // for each direction ["NORTH", "EAST", "SOUTH", "WEST"]
        // for each lane in lanesConfig in that direction
        // TODO("Not yet implemented")

        val fN = SignalPattern(
            type = Priority.STRONG,
            startDirection = CardinalDirection.NORTH,
            movementDirection = RelativeDirection.FORWARD
        )
        val rN = SignalPattern(
            type = Priority.STRONG,
            startDirection = CardinalDirection.NORTH,
            movementDirection = RelativeDirection.RIGHT
        )
        val lN = SignalPattern(
            type = Priority.WEAK,
            startDirection = CardinalDirection.NORTH,
            movementDirection = RelativeDirection.LEFT
        )
        val fS = SignalPattern(
            type = Priority.STRONG,
            startDirection = CardinalDirection.SOUTH,
            movementDirection = RelativeDirection.FORWARD
        )
        val rS = SignalPattern(
            type = Priority.STRONG,
            startDirection = CardinalDirection.SOUTH,
            movementDirection = RelativeDirection.RIGHT
        )
        val lS = SignalPattern(
            type = Priority.WEAK,
            startDirection = CardinalDirection.SOUTH,
            movementDirection = RelativeDirection.LEFT
        )
        val fE = SignalPattern(
            type = Priority.STRONG,
            startDirection = CardinalDirection.EAST,
            movementDirection = RelativeDirection.FORWARD
        )
        val rE = SignalPattern(
            type = Priority.STRONG,
            startDirection = CardinalDirection.EAST,
            movementDirection = RelativeDirection.RIGHT
        )
        val lE = SignalPattern(
            type = Priority.WEAK,
            startDirection = CardinalDirection.EAST,
            movementDirection = RelativeDirection.LEFT,
        )
        val fW = SignalPattern(
            type = Priority.STRONG,
            startDirection = CardinalDirection.WEST,
            movementDirection = RelativeDirection.FORWARD,
        )
        val rW = SignalPattern(
            type = Priority.STRONG,
            startDirection = CardinalDirection.WEST,
            movementDirection = RelativeDirection.RIGHT,
        )
        val lW = SignalPattern(
            type = Priority.WEAK,
            startDirection = CardinalDirection.WEST,
            movementDirection = RelativeDirection.LEFT,
        )

//        lN.conflictedWith = listOf(lS, fS, rS)
//        lS.conflictedWith = listOf(lN, fN, rN)
//        lE.conflictedWith = listOf(lW, fW, rW)
//        lW.conflictedWith = listOf(lE, fE, rE)


        return listOf(
            fN, rN, lN,
            fS, rS, lS,
            fE, rE, lE,
            fW, rW, lW
        )

    }
}

}