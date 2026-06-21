package com.leeluiza.domain.usecase

import com.leeluiza.domain.model.DrawingStroke
import com.leeluiza.domain.repository.DayRepository
import javax.inject.Inject

class AddDayDrawingsUseCase @Inject constructor(
    private val repository: DayRepository
) {
    suspend operator fun invoke(dayId: String, strokes: List<DrawingStroke>) =
        repository.replaceAllDrawings(dayId, strokes)
}