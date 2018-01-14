package com.kotato.assertSimilar

import com.github.javafaker.Faker
import com.kotato.assertSimilar.MatcherSimilar.assertSimilar
import org.junit.jupiter.api.Test
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import java.util.Calendar
import java.util.Date
import java.util.concurrent.TimeUnit
import kotlin.test.assertFailsWith


class AssertSimilarTest {

    @Test
    fun `it should compare objects`() {
        PotatoStub.random().let { assertSimilar(it, it.copy()) }
    }

    @Test
    fun `it should compare objects with list`() {
        NinjaListStub.random().let { assertSimilar(it, it.copy()) }
    }

    @Test
    fun `it should compare objects with list and some missing objects`() {
        NinjaListStub.random().let { value ->
            assertFailsWith<AssertionError> {
                assertSimilar(value, value.copy(list = value.list.dropLast(1)))
            }
        }
    }

    @Test
    fun `it should compare objects with list and some additional objects`() {
        NinjaListStub.random().let { value ->
            assertFailsWith<AssertionError> {
                assertSimilar(value,
                              value.copy(list = value.list.toMutableList().also { it.add("") }))
            }
        }
    }

    @Test
    fun `it should compare objects with unsorted list`() {
        NinjaListStub.random().let { assertSimilar(it, it.copy(list = it.list.asReversed())) }
    }

    @Test
    fun `it should compare objects with array`() {
        NinjaArrayStub.random().let { assertSimilar(it, it.copy()) }
    }

    @Test
    fun `it should compare objects with array and some missing objects`() {
        NinjaArrayStub.random().let { value ->
            assertFailsWith<AssertionError> {
                assertSimilar(value, value.copy(array = value.array.dropLast(1).toTypedArray()))
            }
        }
    }

    @Test
    fun `it should compare objects with array and some additional objects`() {
        NinjaArrayStub.random().let { value ->
            assertFailsWith<AssertionError> {
                assertSimilar(value,
                              value.copy(array = value.array.toMutableList().also { it.add("") }.toTypedArray()))
            }
        }
    }

    @Test
    fun `it should compare objects with unsorted array`() {
        NinjaArrayStub.random().let { assertSimilar(it, it.copy(array = it.array.reversedArray())) }
    }

    @Test
    fun `it should compare objects with map`() {
        NinjaMapStub.random().let { assertSimilar(it, it.copy()) }
    }

    @Test
    fun `it should compare objects with map and some missing objects`() {
        NinjaMapStub.random().let { value ->
            assertFailsWith<AssertionError> {
                assertSimilar(value, value.copy(map = value.map.toMutableMap().also { it.remove(0) }))
            }
        }
    }

    @Test
    fun `it should compare objects with map and some additional objects`() {
        NinjaMapStub.random().let { value ->
            assertFailsWith<AssertionError> {
                assertSimilar(value, value.copy(map = value.map.toMutableMap().also { it.put(3, "potato") }))
            }
        }
    }

    @Test
    fun `it should compare objects with Date`() {
        NinjaDateStub.random().let { assertSimilar(it, it.copy()) }
    }

    @Test
    fun `it should compare objects with Date and 2s of difference`() {
        NinjaDateStub.random().let { value ->
            assertFailsWith<AssertionError> {
                assertSimilar(value, value.copy(date = Date(value.date.time + 2000)))
            }
        }
    }

    @Test
    fun `it should compare objects with Date and more than 2s of difference`() {
        NinjaDateStub.random().let { value ->
            assertFailsWith<AssertionError> {
                assertSimilar(value, value.copy(date = Date(value.date.time + 2001)))
            }
        }
    }

    @Test
    fun `it should compare objects with Date and less than 2s of difference`() {
        NinjaDateStub.random().let {
            assertSimilar(it, it.copy(date = Date(it.date.time + 1999)))
        }
    }

    @Test
    fun `it should compare objects with Calendar`() {
        NinjaCalendarStub.random().let { assertSimilar(it, it.copy()) }
    }

    @Test
    fun `it should compare objects with Calendar and more than 2s of difference`() {
        NinjaCalendarStub.random().let { value ->
            assertFailsWith<AssertionError> {
                Calendar.Builder()
                        .setInstant(value.calendar.toInstant().plusMillis(2001).toEpochMilli())
                        .build()
                        .let { assertSimilar(value, value.copy(calendar = it)) }
            }
        }
    }

    @Test
    fun `it should compare objects with Calendar and less than 2s of difference`() {
        NinjaCalendarStub.random().let { value ->
            Calendar.Builder()
                    .setInstant(value.calendar.toInstant().plusMillis(1999).toEpochMilli())
                    .build()
                    .let { assertSimilar(value, value.copy(calendar = it)) }
        }
    }

    @Test
    fun `it should compare objects with Calendar and 2s of difference`() {
        NinjaCalendarStub.random().let { value ->
            assertFailsWith<AssertionError> {
                Calendar.Builder()
                        .setInstant(value.calendar.toInstant().plusMillis(2000).toEpochMilli())
                        .build()
                        .let { assertSimilar(value, value.copy(calendar = it)) }
            }
        }
    }

    @Test
    fun `it should compare objects with ZonedDateTime`() {
        NinjaZonedDateTimeStub.random().let { assertSimilar(it, it.copy()) }
    }

    @Test
    fun `it should compare objects with ZonedDateTime and more than 2s of difference`() {
        NinjaZonedDateTimeStub.random().let { value ->
            assertFailsWith<AssertionError> {
                assertSimilar(value,
                              value.copy(zonedDateTime = value.zonedDateTime.plus(2001, ChronoUnit.MILLIS)))
            }
        }
    }

    @Test
    fun `it should compare objects with ZonedDateTime and less than 2s of difference`() {
        NinjaZonedDateTimeStub.random().let {
            assertSimilar(it, it.copy(zonedDateTime = it.zonedDateTime.plus(1999, ChronoUnit.MILLIS)))
        }
    }

    @Test
    fun `it should compare objects with ZonedDateTime and 2s of difference`() {
        NinjaZonedDateTimeStub.random().let { value ->
            assertFailsWith<AssertionError> {
                assertSimilar(value,
                              value.copy(zonedDateTime = value.zonedDateTime.plus(2000, ChronoUnit.MILLIS)))
            }
        }
    }

    private data class Potato(val int: Int)
    private data class NinjaList(val potato: Potato, val list: List<String>)
    private data class NinjaArray(val potato: Potato, val array: Array<String>)
    private data class NinjaMap(val potato: Potato, val map: Map<Int, String>)
    private data class NinjaDate(val potato: Potato, val date: Date)
    private data class NinjaCalendar(val potato: Potato, val calendar: Calendar)
    private data class NinjaZonedDateTime(val potato: Potato, val zonedDateTime: ZonedDateTime)
    private object FakerInstance {
        private val instance = Faker()
        operator fun invoke() = instance
    }

    private object PotatoStub {
        fun random() = Potato(FakerInstance().number().randomDigit())
    }

    private object NinjaListStub {
        fun random(potato: Potato = PotatoStub.random(),
                   list: List<String> = (0..2).map { FakerInstance().cat().name() }) = NinjaList(potato, list)
    }

    private object NinjaArrayStub {
        fun random(potato: Potato = PotatoStub.random(),
                   array: Array<String> = (0..2).map { FakerInstance().cat().name() }.toTypedArray()) =
                NinjaArray(potato, array)
    }

    private object NinjaMapStub {
        fun random(potato: Potato = PotatoStub.random(),
                   map: Map<Int, String> = (0..2).map { it to FakerInstance().cat().name() }.toMap()) =
                NinjaMap(potato, map)
    }

    private object NinjaDateStub {
        fun random(potato: Potato = PotatoStub.random(),
                   date: Date = FakerInstance().date().future(10, TimeUnit.DAYS)) = NinjaDate(potato, date)
    }

    private object NinjaCalendarStub {
        fun random(potato: Potato = PotatoStub.random(),
                   calendar: Calendar = Calendar.getInstance()) = NinjaCalendar(potato, calendar)
    }

    private object NinjaZonedDateTimeStub {
        fun random(potato: Potato = PotatoStub.random(),
                   zonedDateTime: ZonedDateTime = ZonedDateTime.now()) = NinjaZonedDateTime(potato, zonedDateTime)
    }

}