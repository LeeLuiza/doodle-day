package com.leeluiza.domain.usecase

import com.leeluiza.domain.model.ErrorType
import com.leeluiza.domain.repository.DayRepository
import com.leeluiza.domain.model.Result
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertTrue
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.Test

class CompleteTaskUseCaseTest {
    private val repository = mock<DayRepository>()
    private val useCase = CompleteTaskUseCase(repository)

    val taskId = "task-123"

    @Test
    fun `should return Success when repository succeeds`() = runTest {
        whenever(repository.completeTask(taskId)).thenReturn(Result.Success(Unit))

        val result = useCase(taskId)

        verify (repository).completeTask(taskId)
        assertTrue(result is Result.Success)
    }

    @Test
    fun `should return Error when repository fails`() = runTest {
        whenever(repository.completeTask(taskId)).thenReturn(Result.Error(ErrorType.DATA_SAVE_ERROR))

        val result = useCase(taskId)

        verify (repository).completeTask(taskId)
        assertTrue(result is Result.Error)
        Assertions.assertEquals(ErrorType.DATA_SAVE_ERROR, (result as Result.Error).errorType)
    }
}