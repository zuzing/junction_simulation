package datatypes

enum class CardinalDirection {
    NORTH,
    EAST,
    SOUTH,
    WEST; // order is important here, impacts the RelativeDirection class

    fun getOpposite(): CardinalDirection {
        val numDirections = entries.size
        val oppositeOrdinal = (this.ordinal + 2) % numDirections
        return entries[oppositeOrdinal]
    }
    companion object {
        fun fromString(direction: String): CardinalDirection {
            return enumValueOf<CardinalDirection>(direction.uppercase())
        }
    }
}

enum class RelativeDirection {
    FORWARD,
    LEFT,
    RIGHT,
    BACKWARDS;

    companion object {
        fun fromCardinalDirection(startDirection: CardinalDirection, endDirection: CardinalDirection): RelativeDirection {
            val startOrdinal = startDirection.ordinal
            val endOrdinal = endDirection.ordinal
            val numDirections = CardinalDirection.entries.size
            val diff = (endOrdinal - startOrdinal + numDirections) % numDirections

            return when (diff) {
                0 -> BACKWARDS
                1 -> RIGHT
                2 -> FORWARD
                3 -> LEFT
                else -> throw IllegalArgumentException("Invalid direction change")
            }
        }
    }
}