package com.leeluiza.domain.usecase

import com.leeluiza.domain.repository.MonthRepository
import javax.inject.Inject

class GetMonthDrawingsUseCase @Inject constructor(
    private val repository: MonthRepository
) {
    operator fun invoke(monthId: String) = repository.getDrawings(monthId)
}