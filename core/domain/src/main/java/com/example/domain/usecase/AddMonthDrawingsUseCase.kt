package com.example.domain.usecase

import com.example.domain.model.DrawingStroke
import com.example.domain.repository.MonthRepository
import javax.inject.Inject

class AddMonthDrawingsUseCase @Inject constructor(
    private val repository: MonthRepository
) {
    suspend operator fun invoke(monthId: String, strokes: List<DrawingStroke>) =
        repository.replaceAllDrawings(monthId, strokes)
}