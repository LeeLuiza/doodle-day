package com.example.domain.usecase

import com.example.domain.repository.MonthRepository
import javax.inject.Inject

class GetMonthDrawingsUseCase @Inject constructor(
    private val repository: MonthRepository
) {
    operator fun invoke(monthId: String) = repository.getDrawings(monthId)
}