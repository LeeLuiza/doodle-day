package com.leeluiza.domain.usecase

import com.leeluiza.domain.model.Task
import com.leeluiza.domain.repository.DayRepository
import javax.inject.Inject

class UpdateTaskUseCase @Inject constructor(
    private val repository: DayRepository
) {
    suspend operator fun invoke(dayId: String, task: Task) = repository.updateTask(dayId, task)
}