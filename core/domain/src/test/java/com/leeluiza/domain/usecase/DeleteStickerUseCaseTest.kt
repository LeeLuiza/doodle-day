package com.leeluiza.domain.usecase

import com.leeluiza.domain.model.ErrorType
import com.leeluiza.domain.repository.DayRepository
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import com.leeluiza.domain.model.Result
import org.junit.jupiter.api.Assertions.*
import org.mockito.kotlin.whenever

class DeleteStickerUseCaseTest {

    private val repository = mock<DayRepository>()
    private val useCase = DeleteStickerUseCase(repository)

    val stickerId = "sticker-123"

    @Test
    fun `should return Success when repository succeeds`() = runTest {
        whenever(repository.deleteSticker(stickerId)).thenReturn(Result.Success(Unit))

        val result = useCase(stickerId)

        verify (repository).deleteSticker(stickerId)
        assertTrue(result is Result.Success)
    }

    @Test
    fun `should return Error when repository fails`() = runTest {
        whenever(repository.deleteSticker(stickerId)).thenReturn(Result.Error(ErrorType.DATA_SAVE_ERROR))

        val result = useCase(stickerId)

        verify (repository).deleteSticker(stickerId)
        assertTrue(result is Result.Error)
        assertEquals(ErrorType.DATA_SAVE_ERROR, (result as Result.Error).errorType)
    }
}