package com.example.domain.usecase

import com.example.domain.repository.DayRepository
import javax.inject.Inject

class CompleteTaskUseCase @Inject constructor(
    private val repository: DayRepository
) {
    suspend operator fun invoke (taskId: String) = repository.completeTask(taskId)
}