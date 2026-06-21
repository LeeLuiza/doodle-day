package com.leeluiza.day.presentation

import com.leeluiza.day.R
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.leeluiza.core.ui.components.ColorPalette
import com.leeluiza.core.ui.components.DrawingActionsRow
import com.leeluiza.core.ui.components.DrawingCanvas
import com.leeluiza.day.presentation.components.*
import com.leeluiza.day.presentation.model.DayBoardEvent
import com.leeluiza.day.presentation.model.RippleEffect
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DayBoardScreen(
    onBack: () -> Unit,
    viewModel: DayViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var ripples by remember { mutableStateOf<List<RippleEffect>>(emptyList()) }
    var rippleIdCounter by remember { mutableLongStateOf(0L) }

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
                title = { Text(stringResource(R.string.day_title, uiState.day)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back))
                    }
                },
                actions = {
                    if (!uiState.isDrawingMode) {
                        IconButton(onClick = { viewModel.onEvent(DayBoardEvent.OpenStickerDialog) }) {
                            Icon(Icons.Default.Add, contentDescription = stringResource(R.string.sticker))
                        }
                    }

                    DrawingActionsRow(
                        isDrawingMode = uiState.isDrawingMode,
                        isEraserActive = uiState.isEraserActive,
                        canUndo = uiState.strokes.isNotEmpty(),
                        canRedo = uiState.redoStack.isNotEmpty(),
                        onUndo = { viewModel.onEvent(DayBoardEvent.UndoStroke) },
                        onRedo = { viewModel.onEvent(DayBoardEvent.RedoStroke) },
                        onToggleEraser = { viewModel.onEvent(DayBoardEvent.ToggleEraser) },
                        onToggleDrawingMode = { viewModel.onEvent(DayBoardEvent.ToggleDrawingMode) }
                    )
                }
            )
        }
    ) { padding ->
        val rippleColor = MaterialTheme.colorScheme.primary
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onDoubleTap = { offset ->
                            val newRipple = RippleEffect(
                                id = rippleIdCounter++,
                                center = offset,
                                color = rippleColor
                            )
                            ripples = ripples + newRipple
                        }
                    )
                }
        ) {
            if (uiState.showTable) {
                EventTableCard(
                    events = uiState.events,
                    onAddClick = { viewModel.onEvent(DayBoardEvent.OpenEventDialog) },
                    onTaskToggle = { id -> viewModel.onEvent(DayBoardEvent.ToggleTaskCompletion(id)) },
                    onEditTask = { id -> viewModel.onEvent(DayBoardEvent.StartEditTask(id)) },
                    modifier = Modifier.align(Alignment.TopStart).padding(16.dp)
                )
            }

            uiState.stickers.forEach { sticker ->
                DraggableStickerItem(
                    sticker = sticker,
                    isDrawingMode = uiState.isDrawingMode,
                    onDragEnd = { id, x, y -> viewModel.onEvent(DayBoardEvent.MoveSticker(id, x, y)) },
                    onEdit = { id -> viewModel.onEvent(DayBoardEvent.StartEditSticker(id)) }
                )
            }

            val currentColor = remember(uiState.selectedColorArgb) { Color(uiState.selectedColorArgb) }

            DrawingCanvas(
                strokes = uiState.strokes,
                isDrawingMode = uiState.isDrawingMode,
                isEraserActive = uiState.isEraserActive,
                selectedColorArgb = uiState.selectedColorArgb,
                onStrokeFinished = { points, colorArgb, isEraser ->
                    viewModel.onEvent(DayBoardEvent.SaveStroke(points, colorArgb, isEraser))
                },
                modifier = Modifier.fillMaxSize()
            )

            if (uiState.isDrawingMode) {
                ColorPalette(
                    selectedColor = currentColor,
                    onColorSelected = { viewModel.onEvent(DayBoardEvent.SelectColor(it)) },
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }

            RippleCanvas(
                ripples = ripples,
                onRippleFinished = { finishedId ->
                    ripples = ripples.filter { it.id != finishedId }
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    }

    if (uiState.showEventDialog) {
        EventDialog(
            existingTask = uiState.editingTask,
            onDismiss = { viewModel.onEvent(DayBoardEvent.CloseEventDialog) },
            onConfirm = { time, title, note ->
                viewModel.onEvent(DayBoardEvent.SaveEventInput(time, title, note))
            },
            onDelete = {
                uiState.editingTask?.let { viewModel.onEvent(DayBoardEvent.DeleteTask(it.id)) }
            }
        )
    }

    if (uiState.showStickerDialog) {
        StickerDialog(
            existingSticker = uiState.editingSticker,
            onDismiss = { viewModel.onEvent(DayBoardEvent.CloseStickerDialog) },
            onConfirm = { text, color ->
                viewModel.onEvent(DayBoardEvent.SaveStickerInput(text, color))
            },
            onDelete = {
                uiState.editingSticker?.let { viewModel.onEvent(DayBoardEvent.DeleteSticker(it.id)) }
            }
        )
    }
}