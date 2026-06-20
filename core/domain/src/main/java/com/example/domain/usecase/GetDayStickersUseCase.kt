package com.example.domain.usecase

import com.example.domain.repository.DayRepository
import javax.inject.Inject

class GetDayStickersUseCase @Inject constructor(
    private val repository: DayRepository
) {
    operator fun invoke(dayId: String) = repository.getStickers(dayId)
}