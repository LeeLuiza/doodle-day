package com.leeluiza.core.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.leeluiza.domain.model.DrawingStroke
import com.leeluiza.domain.model.StrokePoint

@Composable
fun DrawingCanvas(
    strokes: List<DrawingStroke>,
    isDrawingMode: Boolean,
    isEraserActive: Boolean,
    selectedColorArgb: Long,
    onStrokeFinished: (List<StrokePoint>, Long, Boolean) -> Unit,
    modifier: Modifier = Modifier,
    strokeWidthDp: Float = 4f,
    eraserWidthDp: Float = 24f
) {
    val currentPoints = remember { mutableStateListOf<Offset>() }

    val currentColor by rememberUpdatedState(selectedColorArgb)
    val currentIsEraser by rememberUpdatedState(isEraserActive)

    val savedPaths = remember(strokes) {
        strokes.map { stroke ->
            val path = Path().apply {
                if (stroke.points.isNotEmpty()) {
                    moveTo(stroke.points[0].x, stroke.points[0].y)
                    for (i in 1 until stroke.points.size) {
                        lineTo(stroke.points[i].x, stroke.points[i].y)
                    }
                }
            }
            stroke to path
        }
    }

    val canvasModifier = if (isDrawingMode) {
        modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                awaitEachGesture {
                    val down = awaitFirstDown(requireUnconsumed = false)
                    currentPoints.clear()
                    currentPoints.add(down.position)
                    do {
                        val event = awaitPointerEvent()
                        val change = event.changes.first()
                        currentPoints.add(change.position)
                        change.consume()
                    } while (change.pressed)

                    if (currentPoints.size > 1) {
                        val domainPoints = currentPoints.map { offset ->
                            StrokePoint(x = offset.x, y = offset.y)
                        }
                        onStrokeFinished(domainPoints, currentColor, currentIsEraser)

                        currentPoints.clear()
                    }
                }
            }
    } else {
        modifier.fillMaxSize()
    }

    Canvas(
        modifier = canvasModifier.graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
    ) {
        savedPaths.forEach { (stroke, path) ->
            if (stroke.isEraser) {
                drawPath(
                    path = path,
                    color = Color.Transparent,
                    style = Stroke(width = eraserWidthDp.dp.toPx(), cap = StrokeCap.Round),
                    blendMode = BlendMode.Clear
                )
            } else {
                drawPath(
                    path = path,
                    color = Color(stroke.colorArgb),
                    style = Stroke(width = strokeWidthDp.dp.toPx(), cap = StrokeCap.Round)
                )
            }
        }

        if (currentPoints.size > 1) {
            val currentPath = Path().apply {
                moveTo(currentPoints[0].x, currentPoints[0].y)
                for (i in 1 until currentPoints.size) {
                    lineTo(currentPoints[i].x, currentPoints[i].y)
                }
            }
            if (currentIsEraser) {
                drawPath(
                    path = currentPath,
                    color = Color.Transparent,
                    style = Stroke(width = eraserWidthDp.dp.toPx(), cap = StrokeCap.Round),
                    blendMode = BlendMode.Clear
                )
            } else {
                drawPath(
                    path = currentPath,
                    color = Color(currentColor),
                    style = Stroke(width = strokeWidthDp.dp.toPx(), cap = StrokeCap.Round)
                )
            }
        }
    }
}