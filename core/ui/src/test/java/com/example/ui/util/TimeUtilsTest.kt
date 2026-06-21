package com.example.core.ui.util

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.TimeZone

class TimeUtilsTest {

    private val originalTz = TimeZone.getDefault()

    @BeforeEach
    fun setUp() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
    }

    @AfterEach
    fun tearDown() {
        TimeZone.setDefault(originalTz)
    }

    @Test
    fun `parseTimeToLong should parse valid time string correctly`() {
        val result = TimeUtils.parseTimeToLong("12:30")
        assertTrue(result > 0L)
    }

    @Test
    fun `parseTimeToLong should return 0 for blank string`() {
        assertEquals(0L, TimeUtils.parseTimeToLong(""))
    }

    @Test
    fun `parseTimeToLong should return 0 for whitespace string`() {
        assertEquals(0L, TimeUtils.parseTimeToLong("   "))
    }

    @Test
    fun `parseTimeToLong should return 0 for invalid format`() {
        assertEquals(0L, TimeUtils.parseTimeToLong("invalid"))
    }

    @Test
    fun `parseTimeToLong should return 0 for time without colon`() {
        assertEquals(0L, TimeUtils.parseTimeToLong("1230"))
    }

    @Test
    fun `parseTimeToLong should parse midnight correctly`() {
        val result = TimeUtils.parseTimeToLong("00:00")
        assertTrue(result >= 0L)
    }

    @Test
    fun `formatTimeToString should return empty string for 0L`() {
        assertEquals("", TimeUtils.formatTimeToString(0L))
    }

    @Test
    fun `parseTimeToLong and formatTimeToString should be reversible in UTC`() {
        val testTimes = listOf("09:00", "12:30", "23:59", "00:01", "00:01")

        testTimes.forEach { time ->
            val timestamp = TimeUtils.parseTimeToLong(time)
            val formatted = TimeUtils.formatTimeToString(timestamp)
            assertEquals(time, formatted, "Failed for time: $time")
        }
    }

    @Test
    fun `parseTimeToLong should parse midnight as timestamp 0 in UTC`() {
        val result = TimeUtils.parseTimeToLong("00:00")
        assertEquals(0L, result)
    }

    @Test
    fun `formatTimeToString should format valid timestamp correctly`() {
        val originalTime = "14:45"
        val timestamp = TimeUtils.parseTimeToLong(originalTime)
        val formatted = TimeUtils.formatTimeToString(timestamp)

        assertEquals(originalTime, formatted)
    }

    @Test
    fun `parseTimeToLong and formatTimeToString round trip should work`() {
        val originalTime = "15:30"
        val timestamp = TimeUtils.parseTimeToLong(originalTime)
        val restored = TimeUtils.formatTimeToString(timestamp)

        assertEquals(originalTime, restored)
    }
}