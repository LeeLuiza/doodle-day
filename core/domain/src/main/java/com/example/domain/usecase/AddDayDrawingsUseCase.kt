package com.example.domain.usecase

import com.example.domain.model.DrawingStroke
import com.example.domain.repository.DayRepository
import javax.inject.Inject

class AddDayDrawingsUseCase @Inject constructor(
    private val repository: DayRepository
) {
    suspend operator fun invoke(dayId: String, strokes: List<DrawingStroke>) =
        repository.replaceAllDrawings(dayId, strokes)
}