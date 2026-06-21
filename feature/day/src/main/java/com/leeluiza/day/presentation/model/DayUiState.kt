package com.leeluiza.day.presentation.model

import com.leeluiza.domain.model.BoardSticker
import com.leeluiza.domain.model.DrawingStroke
import com.leeluiza.domain.model.Task

data class DayUiState(
    val day: Int,
    val events: List<Task> = emptyList(),
    val stickers: List<BoardSticker> = emptyList(),
    val strokes: List<DrawingStroke> = emptyList(),
    val redoStack: List<DrawingStroke> = emptyList(),
    val selectedColorArgb: Long = 0xFF000000,
    val isDrawingMode: Boolean = false,
    val isEraserActive: Boolean = false,
    val showTable: Boolean = true,
    val showEventDialog: Boolean = false,
    val showStickerDialog: Boolean = false,
    val editingTask: Task? = null,
    val editingSticker: BoardSticker? = null,
    val errorMessageResId: Int? = null
)