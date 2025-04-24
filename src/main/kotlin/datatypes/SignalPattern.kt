package datatypes

enum class Priority {
    STRONG,
    WEAK // must yield to strong
}


data class SignalPattern(val type: Priority, val startDirection: CardinalDirection,
                         val movementDirection: RelativeDirection,
                         var conflictedWith: List<SignalPattern>?)