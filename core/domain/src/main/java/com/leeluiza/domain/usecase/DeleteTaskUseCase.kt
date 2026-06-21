package com.leeluiza.domain.usecase

import com.leeluiza.domain.repository.DayRepository
import javax.inject.Inject

class DeleteTaskUseCase @Inject constructor(
    private val repository: DayRepository
) {
    suspend operator fun invoke(taskId: String) = repository.deleteTask(taskId)
}