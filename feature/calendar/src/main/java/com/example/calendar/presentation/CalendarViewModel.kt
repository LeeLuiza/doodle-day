package com.example.calendar.presentation

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calendar.R
import com.example.calendar.presentation.model.CalendarEvent
import com.example.calendar.presentation.model.CalendarUiState
import com.example.calendar.presentation.model.DayDecoration
import com.example.core.ui.util.CalendarUtils
import com.example.domain.model.DrawingStroke
import com.example.domain.model.ErrorType
import com.example.domain.model.Result
import com.example.domain.usecase.GetMonthDrawingsUseCase
import com.example.domain.usecase.AddMonthDrawingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val getMonthDrawingsUseCase: GetMonthDrawingsUseCase,
    private val addMonthDrawingsUseCase: AddMonthDrawingsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CalendarUiState())
    val uiState: StateFlow<CalendarUiState> = _uiState.asStateFlow()

    private val _strokesSaveTrigger = MutableStateFlow<Pair<String, List<DrawingStroke>>?>(null)

    private val currentMonthIdFlow = _uiState
        .map { "${it.currentYear}-${it.currentMonth}" }
        .distinctUntilChanged()
        .stateIn(viewModelScope, SharingStarted.Lazily, "${_uiState.value.currentYear}-${_uiState.value.currentMonth}")

    init {
        viewModelScope.launch {
            currentMonthIdFlow
                .onEach { _ ->
                _uiState.update { it.copy(strokes = emptyList(), redoStack = emptyList()) }
                }
                .flatMapLatest { monthId ->
                    getMonthDrawingsUseCase(monthId)
                }
                .collect { result ->
                    when (result) {
                        is Result.Success -> {
                            _uiState.update {
                                it.copy(strokes = result.data, errorMessageResId = null)
                            }
                        }
                        is Result.Error -> _uiState.update {
                            it.copy(
                                strokes = emptyList(),
                                isLoading = false,
                                errorMessageResId = mapErrorTypeToResId(result.errorType, R.string.error_drawings_load)
                            )
                        }
                    }
                }
        }

        viewModelScope.launch {
            _strokesSaveTrigger
                .debounce(1500L)
                .collect { pair ->
                    if (pair != null) {
                        addMonthDrawingsUseCase(pair.first, pair.second)
                    }
                }
        }
    }

    fun onEvent(event: CalendarEvent) {
        when (event) {
            is CalendarEvent.DayClicked -> handleDayClicked()
            is CalendarEvent.ToggleDrawingMode -> handleToggleDrawingMode()
            is CalendarEvent.ToggleEraser -> handleToggleEraser()
            is CalendarEvent.ColorSelected -> handleColorSelected(event.colorArgb)
            is CalendarEvent.UndoStroke -> handleUndo()
            is CalendarEvent.RedoStroke -> handleRedo()
            is CalendarEvent.PreviousMonth -> handlePreviousMonth()
            is CalendarEvent.NextMonth -> handleNextMonth()
            is CalendarEvent.SaveStroke -> handleSaveStroke(event)
            is CalendarEvent.DayLongClicked -> handleDayLongClicked(event.dayNumber, event.colorArgb)
            is CalendarEvent.GoToToday -> handleGoToToday()
        }
    }

    fun clearError() { _uiState.update { it.copy(errorMessageResId = null) } }

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

    private fun handleDayClicked() {
        if (_uiState.value.isDrawingMode) return
    }

    private fun handleSaveStroke(event: CalendarEvent.SaveStroke) {
        val newStroke = DrawingStroke(
            id = UUID.randomUUID().toString(),
            colorArgb = event.colorArgb,
            points = event.points,
            isEraser = event.isEraser
        )
        val updatedStrokes = _uiState.value.strokes + newStroke

        _uiState.update { it.copy(strokes = updatedStrokes, redoStack = emptyList()) }

        _strokesSaveTrigger.value = currentMonthIdFlow.value to updatedStrokes
    }

    private fun handleUndo() {
        val currentStrokes = _uiState.value.strokes
        if (currentStrokes.isNotEmpty()) {
            val removedStroke = currentStrokes.last()
            val newStrokes = currentStrokes.dropLast(1)
            val newRedoStack = _uiState.value.redoStack + removedStroke

            _uiState.update { it.copy(strokes = newStrokes, redoStack = newRedoStack) }

            _strokesSaveTrigger.value = currentMonthIdFlow.value to newStrokes
        }
    }

    private fun handleRedo() {
        val redoStack = _uiState.value.redoStack
        if (redoStack.isNotEmpty()) {
            val restoredStroke = redoStack.last()
            val newStrokes = _uiState.value.strokes + restoredStroke
            val newRedoStack = redoStack.dropLast(1)

            _uiState.update { it.copy(strokes = newStrokes, redoStack = newRedoStack) }
            _strokesSaveTrigger.value = currentMonthIdFlow.value to newStrokes
        }
    }

    private fun handleNextMonth() {
        var newMonth = _uiState.value.currentMonth + 1
        var newYear = _uiState.value.currentYear
        if (newMonth > 11) { newMonth = 0; newYear++ }
        updateMonth(newYear, newMonth)
    }

    private fun handlePreviousMonth() {
        var newMonth = _uiState.value.currentMonth - 1
        var newYear = _uiState.value.currentYear
        if (newMonth < 0) { newMonth = 11; newYear-- }
        updateMonth(newYear, newMonth)
    }

    private fun updateMonth(year: Int, month: Int) {
        _uiState.update {
            it.copy(
                currentYear = year,
                currentMonth = month,
                daysInMonth = CalendarUtils.getDaysInMonth(year, month),
                firstDayIndex = CalendarUtils.getFirstDayIndex(year, month)
            )
        }
    }

    private fun handleGoToToday() {
        val today = java.util.Calendar.getInstance()
        updateMonth(today.get(java.util.Calendar.YEAR), today.get(java.util.Calendar.MONTH))
    }

    private fun handleToggleDrawingMode() {
        _uiState.update { it.copy(isDrawingMode = !it.isDrawingMode, isEraserActive = false) }
    }

    private fun handleToggleEraser() {
        _uiState.update { it.copy(isEraserActive = !it.isEraserActive) }
    }

    private fun handleColorSelected(colorArgb: Long) {
        _uiState.update { it.copy(selectedColorArgb = colorArgb, isEraserActive = false) }
    }

    private fun handleDayLongClicked(dayNumber: Int, colorArgb: Long) {
        _uiState.update { current ->
            val newDecoration = DayDecoration(day = dayNumber, backgroundColorArgb = colorArgb)
            current.copy(dayDecorations = current.dayDecorations + (dayNumber to newDecoration))
        }
    }
}