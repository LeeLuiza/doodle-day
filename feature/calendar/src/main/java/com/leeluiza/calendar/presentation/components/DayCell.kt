package com.leeluiza.calendar.presentation.components

import com.leeluiza.calendar.R
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DayCell(
    day: Int,
    isToday: Boolean,
    backgroundColorArgb: Long?,
    hasSticker: Boolean,
    onClick: () -> Unit
) {
    val baseBgColor = if (backgroundColorArgb != null) {
        Color(backgroundColorArgb).copy(alpha = 0.3f)
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }
    val pressedBgColor = baseBgColor.copy(alpha = 0.6f)
    var isPressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.92f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "DayCellScale"
    )

    val animatedBgColor by animateColorAsState(
        targetValue = if (isPressed) pressedBgColor else baseBgColor,
        animationSpec = tween(durationMillis = 100),
        label = "DayCellColor"
    )

    Box(
        modifier = Modifier
            .aspectRatio(1.1f)
            .scale(scale)
            .clip(RoundedCornerShape(8.dp))
            .background(animatedBgColor)
            .then(
                if (isToday) {
                    Modifier.border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp))
                } else Modifier
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        val released = tryAwaitRelease()
                        isPressed = false

                        if (released) {
                            onClick()
                        }
                    }
                )
            }
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        val textScale by animateFloatAsState(
            targetValue = if (isPressed) 0.95f else 1f,
            animationSpec = spring(stiffness = Spring.StiffnessHigh),
            label = "DayCellTextScale"
        )

        val textColor = if (isToday) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
        val animatedTextColor by animateColorAsState(
            targetValue = if (isPressed) textColor.copy(alpha = 0.7f) else textColor,
            label = "DayCellTextColor"
        )

        Text(
            text = day.toString(),
            fontSize = 16.sp,
            fontWeight = if (isToday) FontWeight.Bold else FontWeight.Medium,
            color = animatedTextColor,
            modifier = Modifier.scale(textScale)
        )

        if (hasSticker) {
            Icon(
                imageVector = Icons.Filled.Circle,
                contentDescription = stringResource(R.string.sticker_has),
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(6.dp)
                    .size(8.dp)
            )
        }
    }
}