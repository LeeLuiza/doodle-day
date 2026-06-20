package com.example.day.presentation

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.ui.util.TimeUtils
import com.example.day.R
import com.example.day.presentation.model.DayBoardEvent
import com.example.day.presentation.model.DayUiState
import com.example.domain.model.BoardSticker
import com.example.domain.model.DrawingStroke
import com.example.domain.model.ErrorType
import com.example.domain.model.Task
import com.example.domain.model.Result
import com.example.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class DayViewModel @Inject constructor(
    private val getTasksUseCase: GetDayTasksUseCase,
    private val addTaskUseCase: AddTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val completeTaskUseCase: CompleteTaskUseCase,

    private val getStickersUseCase: GetDayStickersUseCase,
    private val addStickerUseCase: AddStickerUseCase,
    private val updateStickerUseCase: UpdateStickerUseCase,
    private val deleteStickerUseCase: DeleteStickerUseCase,

    private val getDrawingsUseCase: GetDayDrawingsUseCase,
    private val addDrawingsUseCase: AddDayDrawingsUseCase,

    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val currentDayId: String = savedStateHandle.get<String>("date") ?: ""
    private val dayArg: Int = currentDayId.substringAfterLast("_").toIntOrNull() ?: 1

    private val _uiState = MutableStateFlow(DayUiState(day = dayArg))
    val uiState: StateFlow<DayUiState> = _uiState.asStateFlow()

    private val _strokesSaveTrigger = MutableStateFlow<Pair<String, List<DrawingStroke>>?>(null)

    init {
        loadData()
        initDrawingsSaveDebounce()
    }

    private fun loadData() {
        viewModelScope.launch {
            combine(
                getTasksUseCase(currentDayId),
                getStickersUseCase(currentDayId),
                getDrawingsUseCase(currentDayId)
            ) { tasksResult, stickersResult, strokesResult ->
                val errors = mutableListOf<Int>()
                val tasks = when (tasksResult) {
                    is Result.Success -> tasksResult.data
                    is Result.Error -> {
                        errors.add(mapErrorTypeToResId(tasksResult.errorType, R.string.error_tasks_load))
                        emptyList()
                    }
                }
                val stickers = when (stickersResult) {
                    is Result.Success -> stickersResult.data
                    is Result.Error -> {
                        errors.add(mapErrorTypeToResId(stickersResult.errorType, R.string.error_stickers_load))
                        emptyList()
                    }
                }
                val strokes = when (strokesResult) {
                    is Result.Success -> strokesResult.data
                    is Result.Error -> {
                        errors.add(mapErrorTypeToResId(strokesResult.errorType, R.string.error_drawings_load))
                        emptyList()
                    }
                }
                Triple(tasks, stickers, strokes) to errors.firstOrNull()
            }.collect { (data, errorResId) ->
                _uiState.update {
                    it.copy(
                        events = data.first,
                        stickers = data.second,
                        strokes = data.third,
                        errorMessageResId = errorResId
                    )
                }
            }
        }
    }

    @OptIn(FlowPreview::class)
    private fun initDrawingsSaveDebounce() {
        viewModelScope.launch {
            _strokesSaveTrigger
                .debounce(1500L)
                .collect { pair ->
                    if (pair != null) {
                        addDrawingsUseCase(pair.first, pair.second)
                    }
                }
        }
    }

    fun clearError() { _uiState.update { it.copy(errorMessageResId = null) } }

    fun onEvent(event: DayBoardEvent) {
        when (event) {
            is DayBoardEvent.ToggleTable -> _uiState.update { it.copy(showTable = !it.showTable) }
            is DayBoardEvent.ToggleDrawingMode -> _uiState.update { it.copy(isDrawingMode = !it.isDrawingMode, isEraserActive = false) }
            is DayBoardEvent.ToggleEraser -> _uiState.update { it.copy(isEraserActive = !it.isEraserActive) }
            is DayBoardEvent.SelectColor -> _uiState.update { it.copy(selectedColorArgb = event.color.toArgb().toLong(), isEraserActive = false) }

            is DayBoardEvent.OpenEventDialog -> _uiState.update { it.copy(showEventDialog = true) }
            is DayBoardEvent.CloseEventDialog -> _uiState.update { it.copy(showEventDialog = false, editingTask = null) }
            is DayBoardEvent.OpenStickerDialog -> _uiState.update { it.copy(showStickerDialog = true) }
            is DayBoardEvent.CloseStickerDialog -> _uiState.update { it.copy(showStickerDialog = false, editingSticker = null) }

            is DayBoardEvent.SaveEventInput -> handleSaveEventInput(event)
            is DayBoardEvent.StartEditTask -> {
                val task = _uiState.value.events.find { it.id == event.taskId }
                _uiState.update { it.copy(editingTask = task, showEventDialog = true) }
            }
            is DayBoardEvent.DeleteTask -> viewModelScope.launch {
                deleteTaskUseCase(event.taskId)
                _uiState.update { it.copy(editingTask = null, showEventDialog = false) }
            }
            is DayBoardEvent.ToggleTaskCompletion -> viewModelScope.launch {
                completeTaskUseCase(event.taskId)
            }

            is DayBoardEvent.SaveStickerInput -> handleSaveStickerInput(event)
            is DayBoardEvent.StartEditSticker -> {
                val sticker = _uiState.value.stickers.find { it.id == event.stickerId }
                _uiState.update { it.copy(editingSticker = sticker, showStickerDialog = true) }
            }
            is DayBoardEvent.MoveSticker -> viewModelScope.launch {
                val sticker = _uiState.value.stickers.find { it.id == event.id } ?: return@launch
                val updated = sticker.copy(offsetX = event.finalX, offsetY = event.finalY)
                addStickerUseCase(currentDayId, updated)
            }
            is DayBoardEvent.DeleteSticker -> viewModelScope.launch {
                deleteStickerUseCase(event.id)
                _uiState.update { it.copy(editingSticker = null, showStickerDialog = false) }
            }

            is DayBoardEvent.SaveStroke -> handleSaveStroke(event)
            is DayBoardEvent.UndoStroke -> handleUndoStroke()
            is DayBoardEvent.RedoStroke -> handleRedoStroke()
        }
    }

    @StringRes
    private fun mapErrorTypeToResId(errorType: ErrorType, @StringRes contextResId: Int): Int {
        return when (errorType) {
            ErrorType.DATA_LOAD_ERROR -> contextResId
            ErrorType.DATA_SAVE_ERROR -> R.string.error_data_save
            ErrorType.DATA_DELETE_ERROR -> R.string.error_data_delete
            ErrorType.NETWORK_ERROR -> R.string.error_network
            ErrorType.UNKNOWN_ERROR -> R.string.error_unknown
        }
    }

    private fun handleSaveEventInput(event: DayBoardEvent.SaveEventInput) {
        viewModelScope.launch {
            val currentTask = _uiState.value.editingTask
            if (event.title.isBlank()) return@launch
            val timeLong = TimeUtils.parseTimeToLong(event.time)

            if (currentTask != null) {
                updateTaskUseCase(currentDayId, currentTask.copy(time = timeLong, title = event.title, note = event.note))
            } else {
                addTaskUseCase(currentDayId, Task(id = UUID.randomUUID().toString(), time = timeLong, title = event.title, note = event.note, isCompleted = false))
            }
            _uiState.update { it.copy(showEventDialog = false, editingTask = null) }
        }
    }

    private fun handleSaveStickerInput(event: DayBoardEvent.SaveStickerInput) {
        viewModelScope.launch {
            val currentSticker = _uiState.value.editingSticker
            if (event.text.isBlank()) return@launch

            if (currentSticker != null) {
                updateStickerUseCase(currentDayId, currentSticker.copy(text = event.text, colorArgb = event.colorArgb))
            } else {
                val newSticker = BoardSticker(id = UUID.randomUUID().toString(), text = event.text, colorArgb = event.colorArgb, offsetX = Random.nextFloat() * 200f, offsetY = Random.nextFloat() * 200f)
                addStickerUseCase(currentDayId, newSticker)
            }
            _uiState.update { it.copy(showStickerDialog = false, editingSticker = null) }
        }
    }

    private fun handleSaveStroke(event: DayBoardEvent.SaveStroke) {
        val newStroke = DrawingStroke(id = UUID.randomUUID().toString(), colorArgb = event.colorArgb, points = event.points, isEraser = event.isEraser)
        val updatedStrokes = _uiState.value.strokes + newStroke

        _uiState.update { it.copy(strokes = updatedStrokes, redoStack = emptyList()) }
        _strokesSaveTrigger.value = currentDayId to updatedStrokes
    }

    private fun handleUndoStroke() {
        val strokes = _uiState.value.strokes
        if (strokes.isNotEmpty()) {
            val removedStroke = strokes.last()
            val newStrokes = strokes.dropLast(1)

            _uiState.update { it.copy(strokes = newStrokes, redoStack = it.redoStack + removedStroke) }
            _strokesSaveTrigger.value = currentDayId to newStrokes
        }
    }

    private fun handleRedoStroke() {
        val redoStack = _uiState.value.redoStack
        if (redoStack.isNotEmpty()) {
            val restoredStroke = redoStack.last()
            val newStrokes = _uiState.value.strokes + restoredStroke

            _uiState.update { it.copy(strokes = newStrokes, redoStack = redoStack.dropLast(1)) }
            _strokesSaveTrigger.value = currentDayId to newStrokes
        }
    }
}