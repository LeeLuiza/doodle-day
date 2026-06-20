package com.example.domain.usecase

import com.example.domain.model.ErrorType
import com.example.domain.repository.DayRepository
import com.example.domain.model.Result
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever


class DeleteTaskUseCaseTest {
    private val repository = mock<DayRepository>()
    private val useCase = DeleteTaskUseCase(repository)

    val taskId = "task-123"

    @Test
    fun `should return Success when repository succeeds`() = runTest {
        whenever(repository.deleteTask(taskId)).thenReturn(Result.Success(Unit))

        val result = useCase(taskId)

        verify (repository).deleteTask(taskId)
        assertTrue(result is Result.Success)
    }

    @Test
    fun `should return Error when repository fails`() = runTest {
        whenever(repository.deleteTask(taskId)).thenReturn(Result.Error(ErrorType.DATA_SAVE_ERROR))

        val result = useCase(taskId)

        verify (repository).deleteTask(taskId)
        assertTrue(result is Result.Error)
        assertEquals(ErrorType.DATA_SAVE_ERROR, (result as Result.Error).errorType)
    }

}