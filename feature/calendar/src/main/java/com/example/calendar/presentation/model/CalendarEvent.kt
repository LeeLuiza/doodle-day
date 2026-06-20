package com.example.calendar.presentation.model

sealed interface CalendarEvent {
    data class DayClicked(val dayNumber: Int) : CalendarEvent
    data object ToggleDrawingMode : CalendarEvent
    data object ToggleEraser : CalendarEvent
    data object UndoStroke : CalendarEvent
    data object RedoStroke : CalendarEvent
    data object PreviousMonth : CalendarEvent
    data object NextMonth : CalendarEvent
    data object GoToToday : CalendarEvent

    data class ColorSelected(val colorArgb: Long) : CalendarEvent
    data class SaveStroke(
        val points: List<com.example.domain.model.StrokePoint>,
        val colorArgb: Long,
        val isEraser: Boolean
    ) : CalendarEvent
    data class DayLongClicked(val dayNumber: Int, val colorArgb: Long) : CalendarEvent
}