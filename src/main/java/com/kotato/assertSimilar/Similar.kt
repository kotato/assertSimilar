package com.kotato.assertSimilar

import org.apache.commons.lang.builder.ReflectionToStringBuilder
import org.apache.commons.lang.builder.ToStringStyle
import org.hamcrest.Description
import org.hamcrest.Factory
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeDiagnosingMatcher
import org.unitils.reflectionassert.ReflectionComparator
import org.unitils.reflectionassert.comparator.Comparator
import org.unitils.reflectionassert.comparator.impl.LenientOrderCollectionComparator
import org.unitils.reflectionassert.comparator.impl.MapComparator
import org.unitils.reflectionassert.comparator.impl.ObjectComparator
import org.unitils.reflectionassert.comparator.impl.SimpleCasesComparator
import org.unitils.reflectionassert.report.impl.DefaultDifferenceReport

import java.util.ArrayList

class Similar<T> private constructor(
        private val expected: T,
        private val comparator: ReflectionComparator = comparator()) : TypeSafeDiagnosingMatcher<T>() {

    override fun describeTo(description: Description) {
        description.appendText("reflectively deep equal to: " + getStringRepresentation(expected))
    }

    override fun matchesSafely(item: T, mismatchDescription: Description): Boolean {
        var result = true
        if (!comparator.isEqual(expected, item)) {
            result = false
            val difference = comparator.getDifference(expected, item)
            val message = DefaultDifferenceReport().createReport(difference)
            mismatchDescription.appendText("was: " + message)
        }

        return result
    }

    private fun getStringRepresentation(value: T): String {
        return ReflectionToStringBuilder(value, ToStringStyle.MULTI_LINE_STYLE).toString()
    }

    companion object {

        @Factory
        fun <T> similar(target: T?): Matcher<T> {
            return Similar(target)
        }

        private fun comparator(): ReflectionComparator {
            val comparatorChain = ArrayList<Comparator>()
            comparatorChain.add(SimilarDatesComparator())
            comparatorChain.add(SimpleCasesComparator())
            comparatorChain.add(LenientOrderCollectionComparator())
            comparatorChain.add(MapComparator())
            comparatorChain.add(ObjectComparator())

            return ReflectionComparator(comparatorChain)
        }
    }
}
