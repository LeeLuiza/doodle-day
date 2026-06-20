package com.example.domain.usecase

import com.example.domain.model.ErrorType
import com.example.domain.model.Task
import com.example.domain.model.Result
import com.example.domain.repository.DayRepository
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class UpdateTaskUseCaseTest {
    private val repository = mock<DayRepository>()
    private val useCase = UpdateTaskUseCase(repository)

    val dayId = "day-1"
    val task = Task(
        id = "task-1",
        time = System.currentTimeMillis(),
        title = "Обновленная задача",
        note = "Заметка",
        isCompleted = true
    )

    @Test
    fun `should return Success when repository succeeds`() = runTest {
        whenever(repository.updateTask(dayId, task)).thenReturn(Result.Success(Unit))

        val result = useCase(dayId, task)

        verify (repository).updateTask(dayId, task)
        assertTrue(result is Result.Success)
    }

    @Test
    fun `should return Error when repository fails`() = runTest {
        whenever(repository.updateTask(dayId, task)).thenReturn(Result.Error(ErrorType.DATA_SAVE_ERROR))

        val result = useCase(dayId, task)

        verify (repository).updateTask(dayId, task)
        assertTrue(result is Result.Error)
        assertEquals(ErrorType.DATA_SAVE_ERROR, (result as Result.Error).errorType)
    }
}