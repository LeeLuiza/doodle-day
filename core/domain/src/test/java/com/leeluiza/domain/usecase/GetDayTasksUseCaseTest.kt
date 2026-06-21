package com.leeluiza.domain.usecase

import com.leeluiza.domain.model.ErrorType
import com.leeluiza.domain.model.Task
import com.leeluiza.domain.model.Result
import com.leeluiza.domain.repository.DayRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class GetDayTasksUseCaseTest {
    private val repository = mock<DayRepository>()
    private val useCase = GetDayTasksUseCase(repository)

    val dayId = "day-1"
    val expectedTasks = listOf(
        Task(
            id = "t1",
            time = System.currentTimeMillis(),
            title = "Задача 1",
            note = "",
            isCompleted = false)
    )

    @Test
    fun `should return Success flow with tasks`() = runTest {
        whenever(repository.getTasks(dayId)).thenReturn(flowOf(Result.Success(expectedTasks)))

        val result = useCase(dayId).first()

        verify (repository).getTasks(dayId)
        assertTrue(result is Result.Success)
        assertEquals(expectedTasks, (result as Result.Success).data)
    }

    @Test
    fun `should return Error flow when repository fails`() = runTest {
        whenever(repository.getTasks(dayId)).thenReturn(flowOf(Result.Error(ErrorType.DATA_SAVE_ERROR)))

        val result = useCase(dayId).first()

        verify (repository).getTasks(dayId)
        assertTrue(result is Result.Error)
        assertEquals(ErrorType.DATA_SAVE_ERROR, (result as Result.Error).errorType)
    }

}