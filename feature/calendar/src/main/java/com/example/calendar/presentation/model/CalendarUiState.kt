package com.example.calendar.presentation.model

import com.example.core.ui.util.CalendarUtils
import com.example.domain.model.DrawingStroke
import java.util.Calendar

data class CalendarUiState(
    val currentMonth: Int = Calendar.getInstance().get(Calendar.MONTH),
    val currentYear: Int = Calendar.getInstance().get(Calendar.YEAR),
    val daysInMonth: Int = CalendarUtils.getDaysInMonth(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH)),
    val firstDayIndex: Int = CalendarUtils.getFirstDayIndex(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH)),
    val isDrawingMode: Boolean = false,
    val isEraserActive: Boolean = false,
    val selectedColorArgb: Long = 0xFF000000,
    val strokes: List<DrawingStroke> = emptyList(),
    val redoStack: List<DrawingStroke> = emptyList(),
    val dayDecorations: Map<Int, DayDecoration> = emptyMap(),
    val isLoading: Boolean = false,
    val errorMessageResId: Int? = null,
)