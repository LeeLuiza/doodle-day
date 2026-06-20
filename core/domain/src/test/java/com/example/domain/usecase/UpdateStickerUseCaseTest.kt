package com.example.domain.usecase

import com.example.domain.model.BoardSticker
import com.example.domain.model.ErrorType
import com.example.domain.model.Result
import com.example.domain.repository.DayRepository
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.Test

class UpdateStickerUseCaseTest {
    private val repository = mock<DayRepository>()
    private val useCase = UpdateStickerUseCase(repository)

    val dayId = "day-1"
    val sticker = BoardSticker(
        id = "s1",
        text = "Обновленный стикер",
        colorArgb = 2L,
        offsetX = 10f,
        offsetY = 20f
    )

    @Test
    fun `should return Success when repository succeeds`() = runTest {
        whenever(repository.updateSticker(dayId, sticker)).thenReturn(Result.Success(Unit))
        val result = useCase(dayId, sticker)
        verify (repository).updateSticker(dayId, sticker)
        assertTrue(result is Result.Success)
    }

    @Test
    fun `should return Error when repository fails`() = runTest {
        whenever(repository.updateSticker(dayId, sticker)).thenReturn(Result.Error(ErrorType.DATA_SAVE_ERROR))

        val result = useCase(dayId, sticker)

        verify (repository).updateSticker(dayId, sticker)
        assertTrue(result is Result.Error)
        assertEquals(ErrorType.DATA_SAVE_ERROR, (result as Result.Error).errorType)
    }
}