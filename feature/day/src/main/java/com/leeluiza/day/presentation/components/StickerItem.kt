package com.leeluiza.day.presentation.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leeluiza.domain.model.BoardSticker
import kotlin.math.roundToInt
import androidx.compose.ui.draw.scale

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DraggableStickerItem(
    sticker: BoardSticker,
    isDrawingMode: Boolean,
    onDragEnd: (String, Float, Float) -> Unit,
    onEdit: (String) -> Unit
) {
    var offsetX by remember { mutableStateOf(sticker.offsetX) }
    var offsetY by remember { mutableStateOf(sticker.offsetY) }

    val scaleAnimatable = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        scaleAnimatable.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = 0.4f,
                stiffness = 200f
            )
        )
    }

    LaunchedEffect(sticker.offsetX, sticker.offsetY) {
        offsetX = sticker.offsetX
        offsetY = sticker.offsetY
    }

    Box(
        modifier = Modifier
            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
            .scale(scaleAnimatable.value)
            .combinedClickable(onDoubleClick = { if (!isDrawingMode) onEdit(sticker.id) }) { }
            .pointerInput(isDrawingMode) {
                if (!isDrawingMode) {
                    detectDragGestures(onDragEnd = { onDragEnd(sticker.id, offsetX, offsetY) }) { change, dragAmount ->
                        change.consume()
                        offsetX += dragAmount.x
                        offsetY += dragAmount.y
                    }
                }
            }
            .size(120.dp)
            .shadow(4.dp, RoundedCornerShape(8.dp))
            .background(Color(sticker.colorArgb.toInt()), RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        Text(
            text = sticker.text,
            color = Color.Black,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}