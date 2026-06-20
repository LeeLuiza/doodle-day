package com.example.day.presentation.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.ui.util.TimeUtils
import com.example.day.R
import com.example.domain.model.Task

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EventTableCard(
    events: List<Task>,
    onAddClick: () -> Unit,
    onTaskToggle: (String) -> Unit,
    onEditTask: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(stringResource(R.string.plan_for_day), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                IconButton(onClick = onAddClick, modifier = Modifier.size(32.dp)) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = stringResource(R.string.add),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            if (events.isEmpty()) {
                Text(stringResource(R.string.no_events), color = Color.Gray, fontSize = 14.sp)
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(events, key = { it.id }) { event ->
                        TaskItem(
                            event = event,
                            onToggle = { onTaskToggle(event.id) },
                            onEdit = { onEditTask(event.id) }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TaskItem(
    event: Task,
    onToggle: () -> Unit,
    onEdit: () -> Unit
) {
    var animationTrigger by remember { mutableStateOf(0) }

    val scale = remember(animationTrigger) { Animatable(1f) }

    LaunchedEffect(animationTrigger) {
        if (animationTrigger > 0) {
            scale.animateTo(1.1f, spring(stiffness = 500f))
            scale.animateTo(1f, spring(stiffness = 300f))
        }
    }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (event.isCompleted) {
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            } else MaterialTheme.colorScheme.surfaceVariant
        ),
        modifier = Modifier.scale(scale.value)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .combinedClickable(
                    onClick = onToggle,
                    onDoubleClick = {
                        animationTrigger++
                        onEdit()
                    }
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(checked = event.isCompleted, onCheckedChange = null)
            Column(modifier = Modifier.padding(start = 8.dp)) {
                Text(
                    text = event.title,
                    fontWeight = FontWeight.Bold,
                    textDecoration = if (event.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                )
                Text(
                    text = TimeUtils.formatTimeToString(event.time),
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                if (event.note.isNotBlank()) Text(text = event.note, fontSize = 12.sp, color = Color.Gray)
            }
        }
    }
}