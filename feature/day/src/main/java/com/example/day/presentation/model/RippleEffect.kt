package com.example.day.presentation.model

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

data class RippleEffect(
    val id: Long,
    val center: Offset,
    val color: Color
)