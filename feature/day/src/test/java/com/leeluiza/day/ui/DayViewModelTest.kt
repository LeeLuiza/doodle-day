package com.leeluiza.day.ui

import androidx.lifecycle.SavedStateHandle
import com.leeluiza.day.presentation.DayViewModel
import com.leeluiza.day.presentation.model.DayBoardEvent
import com.leeluiza.domain.model.BoardSticker
import com.leeluiza.domain.model.DrawingStroke
import com.leeluiza.domain.model.ErrorType
import com.leeluiza.domain.model.Result
import com.leeluiza.domain.model.StrokePoint
import com.leeluiza.domain.model.Task
import com.leeluiza.domain.usecase.*
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
class DayViewModelTest {

    private val getTasksUseCase = mock<GetDayTasksUseCase>()
    private val addTaskUseCase = mock<AddTaskUseCase>()
    private val deleteTaskUseCase = mock<DeleteTaskUseCase>()
    private val updateTaskUseCase = mock<UpdateTaskUseCase>()
    private val completeTaskUseCase = mock<CompleteTaskUseCase>()
    private val getStickersUseCase = mock<GetDayStickersUseCase>()
    private val addStickerUseCase = mock<AddStickerUseCase>()
    private val updateStickerUseCase = mock<UpdateStickerUseCase>()
    private val deleteStickerUseCase = mock<DeleteStickerUseCase>()
    private val getDrawingsUseCase = mock<GetDayDrawingsUseCase>()
    private val addDrawingsUseCase = mock<AddDayDrawingsUseCase>()

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: DayViewModel

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel(date: String = "2024_1_15"): DayViewModel {
        val savedStateHandle = SavedStateHandle(mapOf("date" to date))
        return DayViewModel(
            getTasksUseCase = getTasksUseCase,
            addTaskUseCase = addTaskUseCase,
            deleteTaskUseCase = deleteTaskUseCase,
            updateTaskUseCase = updateTaskUseCase,
            completeTaskUseCase = completeTaskUseCase,
            getStickersUseCase = getStickersUseCase,
            addStickerUseCase = addStickerUseCase,
            updateStickerUseCase = updateStickerUseCase,
            deleteStickerUseCase = deleteStickerUseCase,
            getDrawingsUseCase = getDrawingsUseCase,
            addDrawingsUseCase = addDrawingsUseCase,
            savedStateHandle = savedStateHandle
        )
    }

    @Test
    fun `init should load tasks, stickers and drawings successfully`() = runTest {
        val tasks = listOf(Task(id = "1", time = 1200L, title = "Test", note = "", isCompleted = false))
        val stickers = listOf(BoardSticker(id = "s1", text = "Sticker", colorArgb = 1L, offsetX = 0f, offsetY = 0f))
        val strokes = listOf(DrawingStroke(id = "st1", points = emptyList(), colorArgb = 1L, isEraser = false))

        whenever(getTasksUseCase(any())).thenReturn(flowOf(Result.Success(tasks)))
        whenever(getStickersUseCase(any())).thenReturn(flowOf(Result.Success(stickers)))
        whenever(getDrawingsUseCase(any())).thenReturn(flowOf(Result.Success(strokes)))

        viewModel = createViewModel()
        advanceUntilIdle()

        assertEquals(tasks, viewModel.uiState.value.events)
        assertEquals(stickers, viewModel.uiState.value.stickers)
        assertEquals(strokes, viewModel.uiState.value.strokes)
        assertNull(viewModel.uiState.value.errorMessageResId)
    }

    @Test
    fun `init should handle error when loading tasks fails`() = runTest {
        whenever(getTasksUseCase(any())).thenReturn(flowOf(Result.Error(ErrorType.DATA_LOAD_ERROR)))
        whenever(getStickersUseCase(any())).thenReturn(flowOf(Result.Success(emptyList())))
        whenever(getDrawingsUseCase(any())).thenReturn(flowOf(Result.Success(emptyList())))

        viewModel = createViewModel()
        advanceUntilIdle()

        assertNotNull(viewModel.uiState.value.errorMessageResId)
        assertTrue(viewModel.uiState.value.events.isEmpty())
    }

    @Test
    fun `DeleteTask should call useCase and close dialog`() = runTest {
        whenever(getTasksUseCase(any())).thenReturn(flowOf(Result.Success(emptyList())))
        whenever(getStickersUseCase(any())).thenReturn(flowOf(Result.Success(emptyList())))
        whenever(getDrawingsUseCase(any())).thenReturn(flowOf(Result.Success(emptyList())))
        whenever(deleteTaskUseCase(any())).thenReturn(Result.Success(Unit))

        viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.onEvent(DayBoardEvent.DeleteTask("task-123"))
        advanceUntilIdle()

        verify(deleteTaskUseCase).invoke("task-123")
        assertNull(viewModel.uiState.value.editingTask)
        assertFalse(viewModel.uiState.value.showEventDialog)
    }

    @Test
    fun `ToggleTaskCompletion should call completeTaskUseCase`() = runTest {
        whenever(getTasksUseCase(any())).thenReturn(flowOf(Result.Success(emptyList())))
        whenever(getStickersUseCase(any())).thenReturn(flowOf(Result.Success(emptyList())))
        whenever(getDrawingsUseCase(any())).thenReturn(flowOf(Result.Success(emptyList())))
        whenever (completeTaskUseCase(any())).thenReturn(Result.Success(Unit))

        viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.onEvent(DayBoardEvent.ToggleTaskCompletion("task-123"))
        advanceUntilIdle()

        verify(completeTaskUseCase).invoke("task-123")
    }

    @Test
    fun `ToggleDrawingMode should toggle isDrawingMode and reset eraser`() = runTest {
        whenever(getTasksUseCase(any())).thenReturn(flowOf(Result.Success(emptyList())))
        whenever(getStickersUseCase(any())).thenReturn(flowOf(Result.Success(emptyList())))
        whenever(getDrawingsUseCase(any())).thenReturn(flowOf(Result.Success(emptyList())))

        viewModel = createViewModel()
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isDrawingMode)

        viewModel.onEvent(DayBoardEvent.ToggleDrawingMode)
        assertTrue(viewModel.uiState.value.isDrawingMode)
        assertFalse(viewModel.uiState.value.isEraserActive)

        viewModel.onEvent(DayBoardEvent.ToggleDrawingMode)
        assertFalse(viewModel.uiState.value.isDrawingMode)
    }

    @Test
    fun `SaveStroke should add stroke and trigger save with debounce`() = runTest {
        whenever(getTasksUseCase(any())).thenReturn(flowOf(Result.Success(emptyList())))
        whenever(getStickersUseCase(any())).thenReturn(flowOf(Result.Success(emptyList())))
        whenever(getDrawingsUseCase(any())).thenReturn(flowOf(Result.Success(emptyList())))
        whenever (addDrawingsUseCase(any(), any())).thenReturn(Result.Success(Unit))

        viewModel = createViewModel()
        advanceUntilIdle()

        val points = listOf(StrokePoint(10f, 20f))
        viewModel.onEvent(DayBoardEvent.SaveStroke(colorArgb = 123L, points = points, isEraser = false))

        assertEquals(1, viewModel.uiState.value.strokes.size)
        assertEquals(123L, viewModel.uiState.value.strokes[0].colorArgb)

        advanceTimeBy(1500L)
        advanceUntilIdle()

        verify(addDrawingsUseCase).invoke(any(), any())
    }

    @Test
    fun `UndoStroke should remove last stroke and add to redo stack`() = runTest {
        val strokes = listOf(
            DrawingStroke(id = "1", points = emptyList(), colorArgb = 1L, isEraser = false),
            DrawingStroke(id = "2", points = emptyList(), colorArgb = 2L, isEraser = false)
        )
        whenever(getTasksUseCase(any())).thenReturn(flowOf(Result.Success(emptyList())))
        whenever(getStickersUseCase(any())).thenReturn(flowOf(Result.Success(emptyList())))
        whenever(getDrawingsUseCase(any())).thenReturn(flowOf(Result.Success(strokes)))

        viewModel = createViewModel()
        advanceUntilIdle()

        assertEquals(2, viewModel.uiState.value.strokes.size)
        assertEquals(0, viewModel.uiState.value.redoStack.size)

        viewModel.onEvent(DayBoardEvent.UndoStroke)

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
        whenever(getTasksUseCase(any())).thenReturn(flowOf(Result.Success(emptyList())))
        whenever(getStickersUseCase(any())).thenReturn(flowOf(Result.Success(emptyList())))
        whenever(getDrawingsUseCase(any())).thenReturn(flowOf(Result.Success(strokes)))

        viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.onEvent(DayBoardEvent.UndoStroke)
        assertEquals(0, viewModel.uiState.value.strokes.size)

        viewModel.onEvent(DayBoardEvent.RedoStroke)
        assertEquals(1, viewModel.uiState.value.strokes.size)
        assertEquals("1", viewModel.uiState.value.strokes[0].id)
        assertEquals(0, viewModel.uiState.value.redoStack.size)
    }

    @Test
    fun `OpenEventDialog should show dialog`() = runTest {
        whenever(getTasksUseCase(any())).thenReturn(flowOf(Result.Success(emptyList())))
        whenever(getStickersUseCase(any())).thenReturn(flowOf(Result.Success(emptyList())))
        whenever(getDrawingsUseCase(any())).thenReturn(flowOf(Result.Success(emptyList())))

        viewModel = createViewModel()
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.showEventDialog)

        viewModel.onEvent(DayBoardEvent.OpenEventDialog)
        assertTrue(viewModel.uiState.value.showEventDialog)
    }

    @Test
    fun `CloseEventDialog should hide dialog and clear editing task`() = runTest {
        whenever(getTasksUseCase(any())).thenReturn(flowOf(Result.Success(emptyList())))
        whenever(getStickersUseCase(any())).thenReturn(flowOf(Result.Success(emptyList())))
        whenever(getDrawingsUseCase(any())).thenReturn(flowOf(Result.Success(emptyList())))

        viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.onEvent(DayBoardEvent.OpenEventDialog)
        viewModel.onEvent(DayBoardEvent.CloseEventDialog)

        assertFalse(viewModel.uiState.value.showEventDialog)
        assertNull(viewModel.uiState.value.editingTask)
    }
}