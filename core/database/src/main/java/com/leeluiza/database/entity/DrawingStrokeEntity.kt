package com.leeluiza.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "drawings",
    indices = [Index("ownerId")]
)
data class DrawingStrokeEntity(
    @PrimaryKey val id: String,
    val ownerId: String,
    val colorArgb: Long,
    val isEraser: Boolean = false,
    val points: List<PointEntity>
)