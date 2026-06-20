package com.example.day.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.day.R
import com.example.domain.model.BoardSticker
import com.example.domain.model.Task
import com.example.core.ui.components.ColorPalette
import com.example.core.ui.util.TimeUtils

@Composable
fun EventDialog(
    existingTask: Task?,
    onDismiss: () -> Unit,
    onConfirm: (time: String, title: String, note: String) -> Unit,
    onDelete: () -> Unit
) {
    var time by rememberSaveable {
        mutableStateOf(existingTask?.time?.let { TimeUtils.formatTimeToString(it) } ?: "")
    }
    var title by rememberSaveable { mutableStateOf(existingTask?.title ?: "") }
    var note by rememberSaveable { mutableStateOf(existingTask?.note ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (existingTask == null) stringResource(R.string.new_event)
                    else stringResource(R.string.edit_event)
                )
                if (existingTask != null) {
                    IconButton(onClick = { onDelete(); onDismiss() }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = stringResource(R.string.delete),
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = time,
                    onValueChange = { time = it },
                    label = { Text(stringResource(R.string.time_hint)) }
                )
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text(stringResource(R.string.title_hint)) }
                )
                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text(stringResource(R.string.note_hint)) }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(time, title, note) },
                enabled = title.isNotBlank()
            ) {
                Text(
                    text = if (existingTask == null) stringResource(R.string.add)
                    else stringResource(R.string.save)
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}

@Composable
fun StickerDialog(
    existingSticker: BoardSticker?,
    onDismiss: () -> Unit,
    onConfirm: (text: String, colorArgb: Long) -> Unit,
    onDelete: () -> Unit
) {
    var text by rememberSaveable { mutableStateOf(existingSticker?.text ?: "") }

    var selectedColorArgb by rememberSaveable {
        mutableLongStateOf(existingSticker?.colorArgb ?: Color.Yellow.toArgb().toLong())
    }
    val selectedColor = Color(selectedColorArgb.toInt())

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (existingSticker == null) stringResource(R.string.new_sticker)
                    else stringResource(R.string.edit_sticker)
                )
                if (existingSticker != null) {
                    IconButton(onClick = { onDelete(); onDismiss() }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = stringResource(R.string.delete),
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text(stringResource(R.string.sticker_text_hint)) },
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = stringResource(R.string.color_label),
                    fontWeight = FontWeight.Medium
                )

                ColorPalette(
                    selectedColor = selectedColor,
                    onColorSelected = { color ->
                        selectedColorArgb = color.toArgb().toLong()
                    }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(text, selectedColorArgb) },
                enabled = text.isNotBlank()
            ) {
                Text(
                    text = if (existingSticker == null) stringResource(R.string.create)
                    else stringResource(R.string.save)
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}