import kotlin.math.max
import kotlin.math.min

fun intersectRanges(range: LongRange, other: LongRange): LongRange {
    if (range.start > other.endInclusive) return LongRange.EMPTY
    if (other.start > range.endInclusive) return LongRange.EMPTY

    return LongRange(max(range.start, other.start), min(range.endInclusive, other.endInclusive))
}

fun rangesIntersect(range: LongRange, other: LongRange) = intersectRanges(range, other) != LongRange.EMPTY
fun rangesConnect(range: LongRange, other: LongRange) = range.last + 1 == other.first || other.last + 1 == range.first


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
