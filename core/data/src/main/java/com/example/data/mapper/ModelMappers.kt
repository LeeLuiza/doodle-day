package com.example.data.mapper

import com.example.database.entity.DayStickerEntity
import com.example.database.entity.DrawingStrokeEntity
import com.example.database.entity.PointEntity
import com.example.database.entity.TaskEntity
import com.example.domain.model.BoardSticker
import com.example.domain.model.DrawingStroke
import com.example.domain.model.StrokePoint
import com.example.domain.model.Task

fun TaskEntity.toDomain(): Task = Task(
    id = this.id,
    time = this.time ?: 0L,
    title = this.title,
    note = this.description ?: "",
    isCompleted = this.isCompleted
)

fun Task.toEntity(dayId: String): TaskEntity = TaskEntity(
    id = this.id,
    dayId = dayId,
    title = this.title,
    time = this.time,
    description = this.note,
    isCompleted = this.isCompleted
)

fun DayStickerEntity.toDomain(): BoardSticker = BoardSticker(
    id = this.id,
    text = this.stickerName,
    colorArgb = this.colorArgb,
    offsetX = this.posX,
    offsetY = this.posY
)

fun BoardSticker.toEntity(dayId: String): DayStickerEntity = DayStickerEntity(
    id = this.id,
    dayId = dayId,
    stickerName = this.text,
    posX = this.offsetX,
    posY = this.offsetY,
    colorArgb = this.colorArgb
)

fun DrawingStrokeEntity.toDomain(): DrawingStroke {
    val points = this.points.map { StrokePoint(it.x, it.y) }
    return DrawingStroke(
        id = this.id,
        colorArgb = this.colorArgb,
        isEraser = this.isEraser,
        points = points
    )
}

fun DrawingStroke.toEntity(ownerId: String): DrawingStrokeEntity {
    val pointEntities = this.points.map { PointEntity(it.x, it.y) }
    return DrawingStrokeEntity(
        id = this.id,
        ownerId = ownerId,
        colorArgb = this.colorArgb,
        isEraser = this.isEraser,
        points = pointEntities
    )
}