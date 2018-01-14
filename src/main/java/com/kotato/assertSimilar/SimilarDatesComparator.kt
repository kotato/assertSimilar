package com.kotato.assertSimilar

import org.unitils.reflectionassert.ReflectionComparator
import org.unitils.reflectionassert.comparator.Comparator
import org.unitils.reflectionassert.difference.Difference
import java.lang.Math.abs
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import java.util.Calendar
import java.util.Date

internal class SimilarDatesComparator : Comparator {
    override fun canCompare(left: Any, right: Any): Boolean {
        return left is Date || right is Date ||
               left is Calendar || right is Calendar ||
               left is ZonedDateTime || right is ZonedDateTime
    }

    override fun compare(left: Any?,
                         right: Any?,
                         onlyFirstDifference: Boolean,
                         reflectionComparator: ReflectionComparator): Difference? {
        if (left === right) {
            return null
        }
        if (left == null) {
            return Difference("Left value null", left, right)
        }
        if (right == null) {
            return Difference("Right value null", left, right)
        }
        if (left is Date && right is Date) {
            return if (similarDates(left, right)) {
                null
            } else Difference("Different date values", left, right)
        }
        if (left is Calendar && right is Calendar) {
            return if (similarCalendarDates(left, right)) {
                null
            } else Difference("Different calendar values", left, right)
        }

        return if (left is ZonedDateTime && right is ZonedDateTime) {
            if (similarZonedDateTimes(left, right)) {
                null
            } else Difference("Different zoned date time values", left, right)
        } else null

    }

    private fun similarDates(left: Date, right: Date): Boolean {
        return abs(left.time - right.time) < 2000
    }

    private fun similarZonedDateTimes(left: ZonedDateTime, right: ZonedDateTime): Boolean {
        return abs(ChronoUnit.MILLIS.between(left, right)) < 2000
    }

    private fun similarCalendarDates(left: Calendar, right: Calendar): Boolean {
        return similarDates(left.time, right.time)
    }
}
