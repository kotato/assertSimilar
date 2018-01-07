package com.kotato.assertSimilar

import org.hamcrest.MatcherAssert.assertThat

object MatcherSimilar {
    fun <T> assertSimilar(actual: T, expected: T) {
        assertSimilar("", actual, expected)
    }

    fun <T> assertSimilar(reason: String, actual: T?, expected: T?) {
        if (null == actual && null == expected) return

        assertThat<T>(reason, actual, Similar.similar<T>(expected))
    }
}
