package com.example.core.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Redo
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EditOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun DrawingActionsRow(
    isDrawingMode: Boolean,
    isEraserActive: Boolean,
    canUndo: Boolean,
    canRedo: Boolean,
    onUndo: () -> Unit,
    onRedo: () -> Unit,
    onToggleEraser: () -> Unit,
    onToggleDrawingMode: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        if (isDrawingMode) {
            IconButton(onClick = onUndo, enabled = canUndo) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Undo,
                    contentDescription = "Undo",
                    tint = if (canUndo) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                )
            }
            IconButton(onClick = onRedo, enabled = canRedo) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Redo,
                    contentDescription = "Redo",
                    tint = if (canRedo) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                )
            }
            IconButton(onClick = onToggleEraser) {
                Icon(
                    imageVector = Icons.Default.EditOff,
                    contentDescription = "Eraser",
                    tint = if (isEraserActive) Color.Red else MaterialTheme.colorScheme.onSurface
                )
            }
        }

        IconButton(onClick = onToggleDrawingMode) {
            Icon(
                imageVector = if (isDrawingMode) Icons.Default.Close else Icons.Default.Edit,
                contentDescription = if (isDrawingMode) "Close drawing" else "Open drawing",
                tint = if (isDrawingMode) Color.Red else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}