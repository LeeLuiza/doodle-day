package com.example.domain.usecase

import com.example.domain.repository.DayRepository
import javax.inject.Inject

class DeleteTaskUseCase @Inject constructor(
    private val repository: DayRepository
) {
    suspend operator fun invoke(taskId: String) = repository.deleteTask(taskId)
}