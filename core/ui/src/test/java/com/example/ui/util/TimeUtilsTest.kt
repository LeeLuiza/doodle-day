package com.example.ui.util

import com.example.core.ui.util.TimeUtils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TimeUtilsTest {
    @Test
    fun `parseTimeToLong should parse valid time string correctly`() {
        val result = TimeUtils.parseTimeToLong("12:30")
        assert(result > 0L)
    }

    @Test
    fun `parseTimeToLong should return 0 for blank string`() {
        val result = TimeUtils.parseTimeToLong("")
        assertEquals(0L, result)
    }

    @Test
    fun `parseTimeToLong should return 0 for whitespace string`() {
        val result = TimeUtils.parseTimeToLong("   ")
        assertEquals(0L, result)
    }

    @Test
    fun `parseTimeToLong should return 0 for invalid format`() {
        val result = TimeUtils.parseTimeToLong("invalid")
        assertEquals(0L, result)
    }

    @Test
    fun `parseTimeToLong should return 0 for time without colon`() {
        val result = TimeUtils.parseTimeToLong("1230")
        assertEquals(0L, result)
    }

    @Test
    fun `parseTimeToLong should parse midnight and format it back correctly`() {
        val originalTime = "00:00"
        val timestamp = TimeUtils.parseTimeToLong(originalTime)
        val formatted = TimeUtils.formatTimeToString(timestamp)

        assertEquals(originalTime, formatted)
    }

    @Test
    fun `formatTimeToString should return empty string for 0L`() {
        val result = TimeUtils.formatTimeToString(0L)
        assertEquals("", result)
    }

    @Test
    fun `formatTimeToString should format valid timestamp correctly`() {
        val originalTime = "14:45"
        val timestamp = TimeUtils.parseTimeToLong(originalTime)
        val formatted = TimeUtils.formatTimeToString(timestamp)

        assertEquals(originalTime, formatted)
    }

    @Test
    fun `formatTimeToString should handle round trip correctly`() {
        val testTimes = listOf("09:00", "12:30", "23:59", "00:01")

        testTimes.forEach { time ->
            val timestamp = TimeUtils.parseTimeToLong(time)
            val formatted = TimeUtils.formatTimeToString(timestamp)
            assertEquals(time, formatted, "Failed for time: $time")
        }
    }

    @Test
    fun `parseTimeToLong and formatTimeToString should be reversible`() {
        val originalTime = "15:30"
        val timestamp = TimeUtils.parseTimeToLong(originalTime)
        val restored = TimeUtils.formatTimeToString(timestamp)

        assertEquals(originalTime, restored)
    }
}