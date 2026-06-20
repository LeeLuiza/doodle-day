package com.example.domain.usecase

import com.example.domain.model.BoardSticker
import com.example.domain.repository.DayRepository
import javax.inject.Inject

class UpdateStickerUseCase @Inject constructor(
    private val repository: DayRepository
) {
    suspend operator fun invoke(dayId: String, sticker: BoardSticker) = repository.updateSticker(dayId, sticker)
}