package com.leeluiza.domain.usecase

import com.leeluiza.domain.model.ErrorType
import com.leeluiza.domain.model.Task
import com.leeluiza.domain.model.Result
import com.leeluiza.domain.repository.DayRepository
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals


class AddTaskUseCaseTest {

    private val repository = mock<DayRepository>()
    private val useCase = AddTaskUseCase(repository)

    val dayId = "day-1"
    val task = Task(
        id = "task-1",
        time = System.currentTimeMillis(),
        title = "Купить молоко",
        note = "",
        isCompleted = false
    )

    @Test
    fun `AddTaskUseCase success`() = runTest {
        whenever(repository.addTask(dayId, task)).thenReturn(Result.Success(Unit))

        val result = useCase(dayId, task)

        verify(repository).addTask(dayId, task)
        assertTrue(result is Result.Success)
    }

    @Test
    fun `AddTaskUseCase error`() = runTest {
        whenever(repository.addTask(dayId, task)).thenReturn(Result.Error(ErrorType.DATA_SAVE_ERROR))

        val result = useCase(dayId, task)

        assertTrue(result is Result.Error)
        assertEquals(ErrorType.DATA_SAVE_ERROR, (result as Result.Error).errorType)
    }
}