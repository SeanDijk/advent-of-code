import kotlin.math.max
import kotlin.math.min

/**
 * Intersects two LongRanges and returns the intersection as a LongRange object.
 *
 * @param range The first LongRange.
 * @param other The second LongRange.
 * @return The intersection of the two LongRanges.
 */
fun intersectRanges(range: LongRange, other: LongRange): LongRange {
    if (range.first > other.last) return LongRange.EMPTY
    if (other.first > range.last) return LongRange.EMPTY

    return LongRange(max(range.first, other.first), min(range.last, other.last))
}

fun rangesIntersect(range: LongRange, other: LongRange) = intersectRanges(range, other) != LongRange.EMPTY
fun rangesConnect(range: LongRange, other: LongRange) = range.last + 1 == other.first || other.last + 1 == range.first || rangesIntersect(range, other)


fun squashRanges(ranges: List<LongRange>): List<LongRange> {
    if (ranges.isEmpty()) return ranges

    val sortedRanges = ranges.sortedBy { it.first }
    val squashedList = mutableListOf<LongRange>()

    var currentSquash = sortedRanges[0]
    for (i in 1 until sortedRanges.size) {
        val valueToCompare = sortedRanges[i]
        currentSquash =
            if (rangesIntersect(currentSquash, valueToCompare) || rangesConnect(currentSquash, valueToCompare)) {
                currentSquash.first..valueToCompare.last
            } else {
                squashedList.add(currentSquash)
                valueToCompare
            }
    }
    squashedList.add(currentSquash)

    return squashedList
}


fun IntRange.overlapsWith(other: IntRange) = this.last >= other.first && this.first <= other.last
