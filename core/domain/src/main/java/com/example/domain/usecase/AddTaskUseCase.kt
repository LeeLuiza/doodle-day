package com.example.domain.usecase

import com.example.domain.model.Task
import com.example.domain.repository.DayRepository
import javax.inject.Inject

class AddTaskUseCase @Inject constructor(
    private val repository: DayRepository
) {
    suspend operator fun invoke(dayId: String, task: Task) = repository.addTask(dayId, task)
}