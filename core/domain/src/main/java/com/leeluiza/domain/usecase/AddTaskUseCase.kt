package com.leeluiza.domain.usecase

import com.leeluiza.domain.model.Task
import com.leeluiza.domain.repository.DayRepository
import javax.inject.Inject

class AddTaskUseCase @Inject constructor(
    private val repository: DayRepository
) {
    suspend operator fun invoke(dayId: String, task: Task) = repository.addTask(dayId, task)
}