package com.example.domain.usecase

import com.example.domain.repository.DayRepository
import javax.inject.Inject

class DeleteStickerUseCase @Inject constructor(
    private val repository: DayRepository
) {
    suspend operator fun invoke(stickerId: String) = repository.deleteSticker(stickerId)
}