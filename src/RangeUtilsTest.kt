import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class RangeUtilsTest {

    @Test
    fun intersectRanges() {
        assertEquals(LongRange.EMPTY, intersectRanges(0L..10L, 11L..15L))
        assertEquals(LongRange.EMPTY, intersectRanges(11L..15L, 0L..10L))
        assertEquals(LongRange.EMPTY, intersectRanges(98L..990L, 79L..92L))
        assertEquals(LongRange.EMPTY, intersectRanges(79L..92L, 98L..99L))
        assertEquals(10L..10L, intersectRanges(0L..10L, 10L..15L))
        assertEquals(5L..10L, intersectRanges(0L..10L, 5L..15L))
        assertEquals(5L..10L, intersectRanges(5L..15L, 0L..10L))
        assertEquals(5L..10L, intersectRanges(0L..15L, 5L..10L))
    }
}
