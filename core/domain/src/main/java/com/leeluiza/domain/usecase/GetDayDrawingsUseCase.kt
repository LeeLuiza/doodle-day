package com.leeluiza.domain.usecase

import com.leeluiza.domain.repository.DayRepository
import javax.inject.Inject

class GetDayDrawingsUseCase @Inject constructor(
    private val repository: DayRepository
) {
    operator fun invoke(dayId: String) = repository.getStrokes(dayId)
}