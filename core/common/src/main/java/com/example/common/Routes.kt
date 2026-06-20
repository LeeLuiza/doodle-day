package com.example.common

object Routes {
    const val CALENDAR = "calendar"
    const val DAY = "day/{date}"
    const val CHAT = "chat"

    fun createDayRoute(date: String) = "day/$date"
}