package com.leeluiza.day.presentation.model

import androidx.compose.ui.graphics.Color
import com.leeluiza.domain.model.StrokePoint

sealed interface DayBoardEvent {
    data object ToggleTable : DayBoardEvent
    data object ToggleDrawingMode : DayBoardEvent
    data object ToggleEraser : DayBoardEvent
    data object UndoStroke : DayBoardEvent
    data object RedoStroke : DayBoardEvent

    data object OpenEventDialog : DayBoardEvent
    data object CloseEventDialog : DayBoardEvent
    data object OpenStickerDialog : DayBoardEvent
    data object CloseStickerDialog : DayBoardEvent

    data class SaveEventInput(val time: String, val title: String, val note: String) : DayBoardEvent
    data class ToggleTaskCompletion(val taskId: String) : DayBoardEvent
    data class StartEditTask(val taskId: String) : DayBoardEvent
    data class DeleteTask(val taskId: String) : DayBoardEvent

    data class StartEditSticker(val stickerId: String) : DayBoardEvent
    data class SaveStickerInput(val text: String, val colorArgb: Long) : DayBoardEvent
    data class DeleteSticker(val id: String) : DayBoardEvent
    data class MoveSticker(val id: String, val finalX: Float, val finalY: Float) : DayBoardEvent

    data class SelectColor(val color: Color) : DayBoardEvent

    data class SaveStroke(
        val points: List<StrokePoint>,
        val colorArgb: Long,
        val isEraser: Boolean
    ) : DayBoardEvent
}