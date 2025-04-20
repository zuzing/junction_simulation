package datatypes

import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test


class CardinalDirectionTest {

    @Test
    fun `fromString should return correct CardinalDirection for valid input`() {
        assertEquals(CardinalDirection.NORTH, CardinalDirection.fromString("north"))
        assertEquals(CardinalDirection.EAST, CardinalDirection.fromString("EAST"))
        assertEquals(CardinalDirection.SOUTH, CardinalDirection.fromString("SoUtH"))
        assertEquals(CardinalDirection.WEST, CardinalDirection.fromString("west"))
    }

    @Test
    fun `fromString should throw exception for invalid input`() {
        assertThrows(IllegalArgumentException::class.java) {
            CardinalDirection.fromString("northeast")
        }
    }
}

class RelativeDirectionTest {

    @Test
    fun `fromCardinalDirection should return correct relative direction from NORTH`() {
        assertEquals(RelativeDirection.BACKWARDS, RelativeDirection.fromCardinalDirection(CardinalDirection.NORTH, CardinalDirection.NORTH))
        assertEquals(RelativeDirection.RIGHT, RelativeDirection.fromCardinalDirection(CardinalDirection.NORTH, CardinalDirection.EAST))
        assertEquals(RelativeDirection.FORWARD, RelativeDirection.fromCardinalDirection(CardinalDirection.NORTH, CardinalDirection.SOUTH))
        assertEquals(RelativeDirection.LEFT, RelativeDirection.fromCardinalDirection(CardinalDirection.NORTH, CardinalDirection.WEST))
    }

    @Test
    fun `fromCardinalDirection should return correct relative direction from other directions`() {
        assertEquals(RelativeDirection.RIGHT, RelativeDirection.fromCardinalDirection(CardinalDirection.WEST, CardinalDirection.NORTH))
        assertEquals(RelativeDirection.LEFT, RelativeDirection.fromCardinalDirection(CardinalDirection.EAST, CardinalDirection.NORTH))
        assertEquals(RelativeDirection.BACKWARDS, RelativeDirection.fromCardinalDirection(CardinalDirection.SOUTH, CardinalDirection.SOUTH))
        assertEquals(RelativeDirection.FORWARD, RelativeDirection.fromCardinalDirection(CardinalDirection.WEST, CardinalDirection.EAST))
        assertEquals(RelativeDirection.FORWARD, RelativeDirection.fromCardinalDirection(CardinalDirection.SOUTH, CardinalDirection.NORTH))
    }
}