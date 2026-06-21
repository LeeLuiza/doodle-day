package com.leeluiza.domain.usecase

import com.leeluiza.domain.model.BoardSticker
import com.leeluiza.domain.model.ErrorType
import com.leeluiza.domain.model.Result
import com.leeluiza.domain.repository.DayRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import org.junit.jupiter.api.Test
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class GetDayStickersUseCaseTest {

    private val repository = mock<DayRepository>()
    val useCase = GetDayStickersUseCase(repository = repository)

    val dayId = "day-1"
    val expectedStickers = listOf(
        BoardSticker(
            id = "s1",
            text = "Стикер 1",
            colorArgb = 1L,
            offsetX = 0f,
            offsetY = 0f
        )
    )

    @Test
    fun `should return Success flow with stickers`() = runTest {
        whenever(repository.getStickers(dayId)).thenReturn(flowOf(Result.Success(expectedStickers)))

        val result = useCase(dayId).first()

        verify (repository).getStickers(dayId)
        assertTrue(result is Result.Success)
        assertEquals(expectedStickers, (result as Result.Success).data)
    }

    @Test
    fun `should return Error flow when repository fails`() = runTest {
        whenever(repository.getStickers(dayId)).thenReturn(flowOf(Result.Error(ErrorType.DATA_SAVE_ERROR)))

        val result = useCase(dayId).first()

        verify (repository).getStickers(dayId)
        assertTrue(result is Result.Error)
        assertEquals(ErrorType.DATA_SAVE_ERROR, (result as Result.Error).errorType)
    }
}