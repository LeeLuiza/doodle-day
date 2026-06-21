package com.example.ui.util

import com.example.core.ui.util.CalendarUtils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CalendarUtilsTest {
    @Test
    fun `getDaysInMonth should return 31 for January`() {
        assertEquals(31, CalendarUtils.getDaysInMonth(2024, 0))
    }

    @Test
    fun `getDaysInMonth should return 28 for February in non-leap year`() {
        assertEquals(28, CalendarUtils.getDaysInMonth(2023, 1))
    }

    @Test
    fun `getDaysInMonth should return 29 for February in leap year`() {
        assertEquals(29, CalendarUtils.getDaysInMonth(2024, 1))
    }

    @Test
    fun `getDaysInMonth should return 30 for April`() {
        assertEquals(30, CalendarUtils.getDaysInMonth(2024, 3))
    }

    @Test
    fun `getDaysInMonth should return 31 for December`() {
        assertEquals(31, CalendarUtils.getDaysInMonth(2024, 11))
    }

    @Test
    fun `getDaysInMonth should return 30 for June`() {
        assertEquals(30, CalendarUtils.getDaysInMonth(2024, 5))
    }

    @Test
    fun `getFirstDayIndex should return correct index for known date`() {
        val firstDayIndex = CalendarUtils.getFirstDayIndex(2024, 0)
        assertEquals(0, firstDayIndex)
    }

    @Test
    fun `getFirstDayIndex should return correct index for another date`() {
        val firstDayIndex = CalendarUtils.getFirstDayIndex(2024, 2)
        assertEquals(4, firstDayIndex)
    }

    @Test
    fun `getFirstDayIndex should return correct index for Sunday`() {
        val firstDayIndex = CalendarUtils.getFirstDayIndex(2024, 8)
        assertEquals(6, firstDayIndex)
    }

    @Test
    fun `getFirstDayIndex should return correct index for Saturday`() {
        val firstDayIndex = CalendarUtils.getFirstDayIndex(2024, 5)
        assertEquals(5, firstDayIndex)
    }
}