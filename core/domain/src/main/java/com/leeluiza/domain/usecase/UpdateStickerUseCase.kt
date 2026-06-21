package com.leeluiza.domain.usecase

import com.leeluiza.domain.model.BoardSticker
import com.leeluiza.domain.repository.DayRepository
import javax.inject.Inject

class UpdateStickerUseCase @Inject constructor(
    private val repository: DayRepository
) {
    suspend operator fun invoke(dayId: String, sticker: BoardSticker) = repository.updateSticker(dayId, sticker)
}