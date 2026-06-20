package com.example.domain.usecase

import com.example.domain.model.DrawingStroke
import com.example.domain.model.ErrorType
import com.example.domain.model.Result
import com.example.domain.model.StrokePoint
import com.example.domain.repository.MonthRepository
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertTrue

class SaveMonthDrawingsUseCaseTest {
    private val repository = mock<MonthRepository>()
    private val useCase = AddMonthDrawingsUseCase(repository)

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
    fun `should return Success when repository saves drawings successfully`() = runTest {
        whenever(repository.addDrawings(monthId, strokes)).thenReturn(Result.Success(Unit))
        val result = useCase(monthId, strokes)

        verify(repository).addDrawings(monthId, strokes)
        assertTrue(result is Result.Success)
    }

    @Test
    fun `should return Error when repository fails to save drawings`() = runTest {
        whenever(repository.addDrawings(monthId, strokes)).thenReturn(Result.Error(ErrorType.DATA_SAVE_ERROR))
        val result = useCase(monthId, strokes)

        verify(repository).addDrawings(monthId, strokes)
        assertTrue(result is Result.Error)
        assertTrue(result.errorType == ErrorType.DATA_SAVE_ERROR)
    }
}