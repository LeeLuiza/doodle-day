package com.leeluiza.calendar.presentation

import com.leeluiza.calendar.presentation.model.CalendarEvent
import com.leeluiza.domain.model.DrawingStroke
import com.leeluiza.domain.model.ErrorType
import com.leeluiza.domain.model.Result
import com.leeluiza.domain.usecase.AddMonthDrawingsUseCase
import com.leeluiza.domain.usecase.GetMonthDrawingsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*

@OptIn(ExperimentalCoroutinesApi::class)
class CalendarViewModelTest {

    private val getMonthDrawingsUseCase = mock<GetMonthDrawingsUseCase>()
    private val addMonthDrawingsUseCase = mock<AddMonthDrawingsUseCase>()

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: CalendarViewModel

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel(): CalendarViewModel {
        return CalendarViewModel(
            getMonthDrawingsUseCase = getMonthDrawingsUseCase,
            addMonthDrawingsUseCase = addMonthDrawingsUseCase
        )
    }

    @Test
    fun `init should load drawings for current month`() = runTest {
        val strokes = listOf(
            DrawingStroke(id = "1", points = emptyList(), colorArgb = 1L, isEraser = false)
        )
        whenever(getMonthDrawingsUseCase(any())).thenReturn(flowOf(Result.Success(strokes)))

        viewModel = createViewModel()
        advanceUntilIdle()

        assertEquals(strokes, viewModel.uiState.value.strokes)
        assertNull(viewModel.uiState.value.errorMessageResId)
    }

    @Test
    fun `init should handle error when loading drawings fails`() = runTest {
        whenever(getMonthDrawingsUseCase(any())).thenReturn(flowOf(Result.Error(ErrorType.DATA_LOAD_ERROR)))

        viewModel = createViewModel()
        advanceUntilIdle()

        assertNotNull(viewModel.uiState.value.errorMessageResId)
        assertTrue(viewModel.uiState.value.strokes.isEmpty())
    }

    @Test
    fun `NextMonth should increment month and year when needed`() = runTest {
        whenever(getMonthDrawingsUseCase(any())).thenReturn(flowOf(Result.Success(emptyList())))

        viewModel = createViewModel()
        advanceUntilIdle()

        val initialMonth = viewModel.uiState.value.currentMonth
        val initialYear = viewModel.uiState.value.currentYear

        viewModel.onEvent(CalendarEvent.NextMonth)
        advanceUntilIdle()

        if (initialMonth == 11) {
            assertEquals(0, viewModel.uiState.value.currentMonth)
            assertEquals(initialYear + 1, viewModel.uiState.value.currentYear)
        } else {
            assertEquals(initialMonth + 1, viewModel.uiState.value.currentMonth)
            assertEquals(initialYear, viewModel.uiState.value.currentYear)
        }
    }

    @Test
    fun `PreviousMonth should decrement month and year when needed`() = runTest {
        whenever(getMonthDrawingsUseCase(any())).thenReturn(flowOf(Result.Success(emptyList())))

        viewModel = createViewModel()
        advanceUntilIdle()

        val initialMonth = viewModel.uiState.value.currentMonth
        val initialYear = viewModel.uiState.value.currentYear

        viewModel.onEvent(CalendarEvent.PreviousMonth)
        advanceUntilIdle()

        if (initialMonth == 0) {
            assertEquals(11, viewModel.uiState.value.currentMonth)
            assertEquals(initialYear - 1, viewModel.uiState.value.currentYear)
        } else {
            assertEquals(initialMonth - 1, viewModel.uiState.value.currentMonth)
            assertEquals(initialYear, viewModel.uiState.value.currentYear)
        }
    }

    @Test
    fun `ToggleDrawingMode should toggle isDrawingMode and reset eraser`() = runTest {
        whenever(getMonthDrawingsUseCase(any())).thenReturn(flowOf(Result.Success(emptyList())))

        viewModel = createViewModel()
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isDrawingMode)

        viewModel.onEvent(CalendarEvent.ToggleDrawingMode)
        assertTrue(viewModel.uiState.value.isDrawingMode)
        assertFalse(viewModel.uiState.value.isEraserActive)

        viewModel.onEvent(CalendarEvent.ToggleDrawingMode)
        assertFalse(viewModel.uiState.value.isDrawingMode)
    }

    @Test
    fun `ToggleEraser should toggle isEraserActive`() = runTest {
        whenever(getMonthDrawingsUseCase(any())).thenReturn(flowOf(Result.Success(emptyList())))

        viewModel = createViewModel()
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isEraserActive)

        viewModel.onEvent(CalendarEvent.ToggleEraser)
        assertTrue(viewModel.uiState.value.isEraserActive)

        viewModel.onEvent(CalendarEvent.ToggleEraser)
        assertFalse(viewModel.uiState.value.isEraserActive)
    }

    @Test
    fun `ColorSelected should update selected color and reset eraser`() = runTest {
        whenever(getMonthDrawingsUseCase(any())).thenReturn(flowOf(Result.Success(emptyList())))

        viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.onEvent(CalendarEvent.ToggleEraser)
        assertTrue(viewModel.uiState.value.isEraserActive)

        viewModel.onEvent(CalendarEvent.ColorSelected(12345L))
        assertEquals(12345L, viewModel.uiState.value.selectedColorArgb)
        assertFalse(viewModel.uiState.value.isEraserActive)
    }

    @Test
    fun `SaveStroke should add stroke and trigger save with debounce`() = runTest {
        whenever(getMonthDrawingsUseCase(any())).thenReturn(flowOf(Result.Success(emptyList())))
        whenever(addMonthDrawingsUseCase(any(), any())).thenReturn(Result.Success(Unit))

        viewModel = createViewModel()
        advanceUntilIdle()

        val points = listOf(com.leeluiza.domain.model.StrokePoint(10f, 20f))
        viewModel.onEvent(CalendarEvent.SaveStroke(colorArgb = 123L, points = points, isEraser = false))

        assertEquals(1, viewModel.uiState.value.strokes.size)
        assertEquals(123L, viewModel.uiState.value.strokes[0].colorArgb)
        assertEquals(0, viewModel.uiState.value.redoStack.size)

        advanceTimeBy(1500L)
        advanceUntilIdle()

        verify(addMonthDrawingsUseCase).invoke(any(), any())
    }

    @Test
    fun `UndoStroke should remove last stroke and add to redo stack`() = runTest {
        val strokes = listOf(
            DrawingStroke(id = "1", points = emptyList(), colorArgb = 1L, isEraser = false),
            DrawingStroke(id = "2", points = emptyList(), colorArgb = 2L, isEraser = false)
        )
        whenever(getMonthDrawingsUseCase(any())).thenReturn(flowOf(Result.Success(strokes)))

        viewModel = createViewModel()
        advanceUntilIdle()

        assertEquals(2, viewModel.uiState.value.strokes.size)

        viewModel.onEvent(CalendarEvent.UndoStroke)

        assertEquals(1, viewModel.uiState.value.strokes.size)
        assertEquals("1", viewModel.uiState.value.strokes[0].id)
        assertEquals(1, viewModel.uiState.value.redoStack.size)
        assertEquals("2", viewModel.uiState.value.redoStack[0].id)
    }

    @Test
    fun `RedoStroke should restore last undone stroke`() = runTest {
        val strokes = listOf(
            DrawingStroke(id = "1", points = emptyList(), colorArgb = 1L, isEraser = false)
        )
        whenever(getMonthDrawingsUseCase(any())).thenReturn(flowOf(Result.Success(strokes)))

        viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.onEvent(CalendarEvent.UndoStroke)
        assertEquals(0, viewModel.uiState.value.strokes.size)

        viewModel.onEvent(CalendarEvent.RedoStroke)
        assertEquals(1, viewModel.uiState.value.strokes.size)
        assertEquals("1", viewModel.uiState.value.strokes[0].id)
        assertEquals(0, viewModel.uiState.value.redoStack.size)
    }

    @Test
    fun `clearError should clear error message`() = runTest {
        whenever(getMonthDrawingsUseCase(any())).thenReturn(flowOf(Result.Error(ErrorType.DATA_LOAD_ERROR)))

        viewModel = createViewModel()
        advanceUntilIdle()

        assertNotNull(viewModel.uiState.value.errorMessageResId)

        viewModel.clearError()
        assertNull(viewModel.uiState.value.errorMessageResId)
    }

    @Test
    fun `DayLongClicked should add decoration for day`() = runTest {
        whenever(getMonthDrawingsUseCase(any())).thenReturn(flowOf(Result.Success(emptyList())))

        viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.onEvent(CalendarEvent.DayLongClicked(dayNumber = 15, colorArgb = 12345L))

        val decoration = viewModel.uiState.value.dayDecorations[15]
        assertNotNull(decoration)
        assertEquals(15, decoration?.day)
        assertEquals(12345L, decoration?.backgroundColorArgb)
    }
}