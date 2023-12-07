import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Named.named
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource


class RangeUtilsTest {

    companion object {
        private val empty = named("empty", LongRange.EMPTY)

        @JvmStatic
        fun intersectRangesData(): List<Arguments> {
            return listOf(
                Arguments.of(empty, 0L..10L, 11L..15L),
                Arguments.of(empty, 11L..15L, 0L..10L),
                Arguments.of(empty, 98L..990L, 79L..92L),
                Arguments.of(empty, 79L..92L, 98L..99L),
                Arguments.of(10L..10L, 0L..10L, 10L..15L),
                Arguments.of(5L..10L, 0L..10L, 5L..15L),
                Arguments.of(5L..10L, 5L..15L, 0L..10L),
                Arguments.of(5L..10L, 0L..15L, 5L..10L)
            )
        }

        @JvmStatic
        fun rangesIntersectData() = listOf(
            Arguments.of(false, 0L..10L, 11L..15L),
            Arguments.of(false, 11L..15L, 0L..10L),
            Arguments.of(false, 98L..990L, 79L..92L),
            Arguments.of(false, 79L..92L, 98L..99L),
            Arguments.of(true, 0L..10L, 10L..15L),
            Arguments.of(true, 0L..10L, 5L..15L),
            Arguments.of(true, 5L..15L, 0L..10L),
            Arguments.of(true, 0L..15L, 5L..10L)
        )

        @JvmStatic
        fun rangesConnectData() = listOf(
            Arguments.of(true, 0L..10L, 11L..15L),
            Arguments.of(true, 11L..15L, 0L..10L),
            Arguments.of(false, 98L..990L, 79L..92L),
            Arguments.of(false, 79L..92L, 98L..990L),
            Arguments.of(true, 0L..10L, 10L..15L),
            Arguments.of(true, 0L..10L, 5L..15L),
            Arguments.of(true, 5L..15L, 0L..10L),
            Arguments.of(true, 0L..15L, 5L..10L)
        )

        @JvmStatic
        fun squashRangesData() = listOf(
            Arguments.of(listOf(0L..15L), listOf(0L..10L, 5L..15L)),
            Arguments.of(listOf(5L..15L), listOf(5L..10L, 10L..15L)),
            Arguments.of(listOf(0L..92L), listOf(0L..50L, 50L..92L, 30L..60L)),
            Arguments.of(listOf(0L..1000L), listOf(0L..1000L, 500L..1000L, 0L..500L)),
            Arguments.of(listOf(0L..1000L), listOf(0L..10L, 11L..1000L)),
            Arguments.of(listOf(20L..30L, 40L..50L), listOf(20L..30L, 40L..50L)),
            Arguments.of(listOf(0L..10L, 20L..30L, 40L..50L), listOf(0L..10L, 20L..30L, 40L..50L))
        )
    }

    @ParameterizedTest(name = "{1} ∩ {2} = {0}")
    @MethodSource("intersectRangesData")
    fun intersectRanges(expected: LongRange, range1: LongRange, range2: LongRange) {
        assertEquals(expected, intersectRanges(range1, range2))
    }

    @ParameterizedTest(name = "{1} intersects {2} = {0}")
    @MethodSource("rangesIntersectData")
    fun rangesIntersect(expected: Boolean, range1: LongRange, range2: LongRange) {
        assertEquals(expected, rangesIntersect(range1, range2))
    }

    @ParameterizedTest(name = "{1} connects {2} = {0}")
    @MethodSource("rangesConnectData")
    fun rangesConnect(expected: Boolean, range1: LongRange, range2: LongRange) {
        assertEquals(expected, rangesConnect(range1, range2))
    }

    @ParameterizedTest(name = "[{index}]: squashing {1} results in {0}")
    @MethodSource("squashRangesData")
    fun squashRanges(expected: List<LongRange>, input: List<LongRange>) {
        assertEquals(expected, squashRanges(input))
    }
}

