package com.leeluiza.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "day_stickers")
data class DayStickerEntity(
    @PrimaryKey val id: String,
    val dayId: String,
    val stickerName: String,
    val posX: Float,
    val posY: Float,
    val colorArgb: Long = 0L
)