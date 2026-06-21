package com.leeluiza.day.presentation.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.leeluiza.day.presentation.model.RippleEffect
import kotlinx.coroutines.launch
import kotlin.collections.forEach
import androidx.compose.animation.core.FastOutSlowInEasing

@Composable
fun RippleCanvas(
    ripples: List<RippleEffect>,
    onRippleFinished: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        ripples.forEach { ripple ->
            RippleItem(
                ripple = ripple,
                onFinished = { onRippleFinished(ripple.id) }
            )
        }
    }
}

@Composable
private fun RippleItem(
    ripple: RippleEffect,
    onFinished: () -> Unit
) {
    val radius1 = remember { Animatable(0f) }
    val radius2 = remember { Animatable(0f) }

    LaunchedEffect(ripple.id) {
        launch {
            radius1.animateTo(200f, tween(durationMillis = 400, easing = FastOutSlowInEasing))
        }
        launch {
            radius2.animateTo(350f, tween(durationMillis = 600, easing = FastOutSlowInEasing))
        }

        kotlinx.coroutines.delay(600)
        onFinished()
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        val progress1 = (radius1.value / 200f).coerceIn(0f, 1f)
        val alpha1 = (1f - progress1) * 0.5f

        val progress2 = (radius2.value / 350f).coerceIn(0f, 1f)
        val alpha2 = (1f - progress2) * 0.3f

        drawCircle(
            color = ripple.color.copy(alpha = alpha2),
            radius = radius2.value,
            center = ripple.center,
            style = Stroke(width = 3.dp.toPx())
        )

        drawCircle(
            color = ripple.color.copy(alpha = alpha1),
            radius = radius1.value,
            center = ripple.center,
            style = Stroke(width = 5.dp.toPx())
        )
    }
}