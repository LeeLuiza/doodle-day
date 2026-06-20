package com.example.calendar.presentation.model

data class DayDecoration(
    val day: Int,
    val backgroundColorArgb: Long? = null,
    val hasSticker: Boolean = false
)