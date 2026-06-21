package com.leeluiza.domain.usecase

import com.leeluiza.domain.model.DrawingStroke
import com.leeluiza.domain.model.ErrorType
import com.leeluiza.domain.model.Result
import com.leeluiza.domain.model.StrokePoint
import com.leeluiza.domain.repository.MonthRepository
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertTrue

class AddMonthDrawingsUseCaseTest {
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
        whenever(repository.replaceAllDrawings(monthId, strokes)).thenReturn(Result.Success(Unit))
        val result = useCase(monthId, strokes)

        verify(repository).replaceAllDrawings(monthId, strokes)
        assertTrue(result is Result.Success)
    }

    @Test
    fun `should return Error when repository fails to save drawings`() = runTest {
        whenever(repository.replaceAllDrawings(monthId, strokes)).thenReturn(Result.Error(ErrorType.DATA_SAVE_ERROR))
        val result = useCase(monthId, strokes)

        verify(repository).replaceAllDrawings(monthId, strokes)
        assertTrue(result is Result.Error)
        assertTrue(result.errorType == ErrorType.DATA_SAVE_ERROR)
    }
}