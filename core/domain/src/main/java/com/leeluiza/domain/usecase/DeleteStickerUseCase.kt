package com.leeluiza.domain.usecase

import com.leeluiza.domain.repository.DayRepository
import javax.inject.Inject

class DeleteStickerUseCase @Inject constructor(
    private val repository: DayRepository
) {
    suspend operator fun invoke(stickerId: String) = repository.deleteSticker(stickerId)
}