package datatypes

enum class CardinalDirection {
    NORTH,
    EAST,
    SOUTH,
    WEST; // order is important here, impacts the RelativeDirection class
    companion object {
        fun fromString(direction: String): CardinalDirection {
//            return values().find { it.name.equals(direction, ignoreCase = true) }
            return enumValueOf<CardinalDirection>(direction.uppercase()) // not sure if this works
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
                0 -> FORWARD
                1 -> RIGHT
                2 -> BACKWARDS
                3 -> LEFT
                else -> throw IllegalArgumentException("Invalid direction change")
            }
        }
    }
}