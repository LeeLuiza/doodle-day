package com.example.core.ui.util

import java.util.Calendar

object CalendarUtils {
    fun getDaysInMonth(year: Int, month: Int): Int {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, 1)
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    }

    fun getFirstDayIndex(year: Int, month: Int): Int {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, 1)
        val javaDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        return (javaDayOfWeek + 5) % 7
    }
}