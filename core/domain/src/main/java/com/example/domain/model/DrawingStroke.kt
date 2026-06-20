package com.example.domain.model

data class DrawingStroke(
    val id: String,
    val colorArgb: Long,
    val points: List<StrokePoint>,
    val isEraser: Boolean = false
)