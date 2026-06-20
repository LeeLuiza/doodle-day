package com.example.domain.usecase

import com.example.domain.model.Task
import com.example.domain.repository.DayRepository
import javax.inject.Inject

class UpdateTaskUseCase @Inject constructor(
    private val repository: DayRepository
) {
    suspend operator fun invoke(dayId: String, task: Task) = repository.updateTask(dayId, task)
}