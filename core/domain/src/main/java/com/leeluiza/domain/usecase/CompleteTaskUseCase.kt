package com.leeluiza.domain.usecase

import com.leeluiza.domain.repository.DayRepository
import javax.inject.Inject

class CompleteTaskUseCase @Inject constructor(
    private val repository: DayRepository
) {
    suspend operator fun invoke (taskId: String) = repository.completeTask(taskId)
}