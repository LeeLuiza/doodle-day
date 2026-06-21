package com.example.domain.usecase

import com.example.domain.model.DrawingStroke
import com.example.domain.model.ErrorType
import com.example.domain.model.Result
import com.example.domain.model.StrokePoint
import com.example.domain.repository.DayRepository
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.Test

class AddDayDrawingsUseCaseTest {
    private val repository = mock<DayRepository>()
    private val useCase = AddDayDrawingsUseCase(repository)

    val dayId = "day-1"
    val strokes = listOf(
        DrawingStroke(
            id = "st1",
            points = listOf(
                StrokePoint(0F, 0F)
            ),
            colorArgb = 0L,
            isEraser = false
        )
    )

    @Test
    fun `AddDayDrawingsUseCase should return Success when repo succeeds`() = runTest {
        whenever(repository.replaceAllDrawings(dayId, strokes)).thenReturn(Result.Success(Unit))

        val result = useCase(dayId, strokes)

        verify(repository).replaceAllDrawings(dayId, strokes)
        assertTrue(result is Result.Success)
    }

    @Test
    fun `AddDayDrawingsUseCase should return Error when repo fails`() = runTest {
        whenever(repository.replaceAllDrawings(dayId, strokes)).thenReturn(Result.Error(ErrorType.DATA_SAVE_ERROR))

        val result = useCase(dayId, strokes)

        verify(repository).replaceAllDrawings(dayId, strokes)
        assertTrue(result is Result.Error)
        assertEquals(ErrorType.DATA_SAVE_ERROR, (result as Result.Error).errorType)
    }
}