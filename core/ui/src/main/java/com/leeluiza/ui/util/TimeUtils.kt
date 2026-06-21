package com.leeluiza.core.ui.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object TimeUtils {
    private const val TIME_FORMAT = "HH:mm"

    fun parseTimeToLong(timeString: String): Long {
        if (timeString.isBlank()) return 0L
        return try {
            val format = SimpleDateFormat(TIME_FORMAT, Locale.getDefault())
            val date = format.parse(timeString)
            date?.time ?: 0L
        } catch (e: Exception) {
            0L
        }
    }

    fun formatTimeToString(timestamp: Long): String {
        if (timestamp == 0L) return ""
        val format = SimpleDateFormat(TIME_FORMAT, Locale.getDefault())
        return format.format(Date(timestamp))
    }
}