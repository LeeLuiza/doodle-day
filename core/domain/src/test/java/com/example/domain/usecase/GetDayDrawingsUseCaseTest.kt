package com.example.domain.usecase

import com.example.domain.model.DrawingStroke
import com.example.domain.model.ErrorType
import com.example.domain.model.Result
import com.example.domain.model.StrokePoint
import com.example.domain.repository.DayRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.Test

class GetDayDrawingsUseCaseTest {
    private val repository = mock<DayRepository>()
    private val useCase = GetDayDrawingsUseCase(repository)

    val dayId = "day-1"
    val expectedStrokes = listOf(
        DrawingStroke(
            id = "st1",
            points = listOf(
                StrokePoint(0F, 0F)
            ),
            colorArgb = 0L,
            isEraser = false
        )
    )
    val errorMessage = "Ошибка загрузки рисунка"

    @Test
    fun `should return Success flow with strokes`() = runTest {
        whenever(repository.getStrokes(dayId)).thenReturn(flowOf(Result.Success(expectedStrokes)))

        val result = useCase(dayId).first()

        verify(repository).getStrokes(dayId)
        assertTrue(result is Result.Success)
        assertEquals(expectedStrokes, (result as Result.Success).data)
    }

    @Test
    fun `should return Error flow when repository fails`() = runTest {
        whenever(repository.getStrokes(dayId)).thenReturn(flowOf(Result.Error(ErrorType.DATA_SAVE_ERROR)))

        val result = useCase(dayId).first()

        verify(repository).getStrokes(dayId)
        assertTrue(result is Result.Error)
        assertEquals(ErrorType.DATA_SAVE_ERROR, (result as Result.Error).errorType)
    }
}