package com.leeluiza.calendar.presentation.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.leeluiza.calendar.presentation.model.CalendarUiState
import com.leeluiza.core.ui.components.DrawingCanvas
import com.leeluiza.domain.model.StrokePoint
import java.util.Calendar

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CalendarGrid(
    uiState: CalendarUiState,
    onDayClick: (String) -> Unit,
    onStrokeFinished: (List<StrokePoint>, Long, Boolean) -> Unit
) {
    val today = Calendar.getInstance()
    val isCurrentMonth = (uiState.currentYear == today.get(Calendar.YEAR) &&
            uiState.currentMonth == today.get(Calendar.MONTH))
    val todayDay = today.get(Calendar.DAY_OF_MONTH)

    Box(modifier = Modifier.fillMaxSize()) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            userScrollEnabled = !uiState.isDrawingMode
        ) {
            items(uiState.firstDayIndex) {
                Box(modifier = Modifier.aspectRatio(1.1f))
            }

            items(
                count = uiState.daysInMonth,
                key = { index ->
                    "${uiState.currentYear}_${uiState.currentMonth}_${index + 1}"
                }
            ) { index ->
                val dayNum = index + 1
                val decoration = uiState.dayDecorations[dayNum]

                val dayId = "${uiState.currentYear}_${uiState.currentMonth}_$dayNum"

                DayCell(
                    day = dayNum,
                    isToday = isCurrentMonth && (dayNum == todayDay),
                    backgroundColorArgb = decoration?.backgroundColorArgb,
                    hasSticker = decoration?.hasSticker ?: false,
                    onClick = { onDayClick(dayId) }
                )
            }
        }

        DrawingCanvas(
            strokes = uiState.strokes,
            isDrawingMode = uiState.isDrawingMode,
            isEraserActive = uiState.isEraserActive,
            selectedColorArgb = uiState.selectedColorArgb,
            onStrokeFinished = onStrokeFinished,
            modifier = Modifier.fillMaxSize()
        )
    }
}