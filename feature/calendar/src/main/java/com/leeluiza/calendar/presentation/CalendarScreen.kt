package com.leeluiza.calendar.presentation

import com.leeluiza.calendar.R
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.leeluiza.calendar.presentation.components.CalendarGrid
import com.leeluiza.calendar.presentation.components.WeekDaysHeader
import com.leeluiza.calendar.presentation.model.CalendarEvent
import com.leeluiza.core.ui.components.ColorPalette
import com.leeluiza.core.ui.components.DrawingActionsRow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    onDayClick: (String) -> Unit,
    viewModel: CalendarViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val snackBarHostState = remember { SnackbarHostState() }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val monthNames = stringArrayResource(R.array.month_names)
    val scope = rememberCoroutineScope()

    LaunchedEffect(uiState.errorMessageResId) {
        uiState.errorMessageResId?.let { resId ->
            scope.launch {
                snackBarHostState.showSnackbar(context.getString(resId))
                viewModel.clearError()
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (!uiState.isDrawingMode) {
                            IconButton(onClick = { viewModel.onEvent(CalendarEvent.PreviousMonth) }) {
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = stringResource(R.string.previous_month)
                                )
                            }
                        }

                        Text(
                            "${monthNames[uiState.currentMonth]} ${uiState.currentYear}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )

                        if (!uiState.isDrawingMode) {
                            IconButton(onClick = { viewModel.onEvent(CalendarEvent.NextMonth) }) {
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = stringResource(R.string.next_month)
                                )
                            }
                        }
                    }
                },
                actions = {
                    if (!uiState.isDrawingMode) {
                        TextButton(onClick = { viewModel.onEvent(CalendarEvent.GoToToday) }) {
                            Text(stringResource(R.string.today))
                        }
                    }

                    DrawingActionsRow(
                        isDrawingMode = uiState.isDrawingMode,
                        isEraserActive = uiState.isEraserActive,
                        canUndo = uiState.strokes.isNotEmpty(),
                        canRedo = uiState.redoStack.isNotEmpty(),
                        onUndo = { viewModel.onEvent(CalendarEvent.UndoStroke) },
                        onRedo = { viewModel.onEvent(CalendarEvent.RedoStroke) },
                        onToggleEraser = { viewModel.onEvent(CalendarEvent.ToggleEraser) },
                        onToggleDrawingMode = { viewModel.onEvent(CalendarEvent.ToggleDrawingMode) }
                    )
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
            ) {
                if (uiState.isDrawingMode) {
                    Text(
                        text = if (uiState.isEraserActive) {
                            stringResource(R.string.eraser_mode)
                        } else stringResource(R.string.drawing_mode),
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                WeekDaysHeader()
                Spacer(modifier = Modifier.height(8.dp))

                CalendarGrid(
                    uiState = uiState,
                    onDayClick = { dayId ->
                        if (!uiState.isDrawingMode) {
                            onDayClick(dayId)
                        }
                    },
                    onStrokeFinished = { stroke, colorArgb, isEraser ->
                        viewModel.onEvent(CalendarEvent.SaveStroke(stroke, colorArgb, isEraser))
                    }
                )
            }

            if (uiState.isDrawingMode) {
                val currentColor = Color(uiState.selectedColorArgb)

                ColorPalette(
                    selectedColor = currentColor,
                    onColorSelected = { color ->
                        val colorLong = color.toArgb().toLong()
                        viewModel.onEvent(CalendarEvent.ColorSelected(colorLong))
                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                        .widthIn(max = 320.dp)
                )
            }
        }
    }
}