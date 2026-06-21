package com.leeluiza.domain.usecase

import com.leeluiza.domain.model.DrawingStroke
import com.leeluiza.domain.model.ErrorType
import com.leeluiza.domain.model.Result
import com.leeluiza.domain.model.StrokePoint
import com.leeluiza.domain.repository.MonthRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertTrue

class GetMonthDrawingsUseCaseTest {
    private val repository = mock<MonthRepository>()
    private val useCase = GetMonthDrawingsUseCase(repository)

    val monthId = "month-1"
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
    fun `should return Success flow with drawings when repository succeeds`() = runTest {
        whenever(repository.getDrawings(monthId)).thenReturn(flowOf(Result.Success(strokes)))
        val result = useCase(monthId).first()

        verify(repository).getDrawings(monthId)
        assertTrue(result is Result.Success)
        assertEquals(strokes, (result as Result.Success).data)
    }

    @Test
    fun `should return Error flow when repository fails to get drawings`() = runTest {
        whenever(repository.getDrawings(monthId)).thenReturn(flowOf(Result.Error(ErrorType.DATA_SAVE_ERROR)))
        val result = useCase(monthId).first()

        verify(repository).getDrawings(monthId)
        assertTrue(result is Result.Error)
        assertTrue(result.errorType == ErrorType.DATA_SAVE_ERROR)
    }
}