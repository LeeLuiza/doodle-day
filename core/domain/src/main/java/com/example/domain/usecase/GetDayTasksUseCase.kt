package com.example.domain.usecase

import com.example.domain.repository.DayRepository
import javax.inject.Inject

class GetDayTasksUseCase @Inject constructor(
    private val repository: DayRepository
) {
    operator fun invoke(dayId: String) = repository.getTasks(dayId)
}