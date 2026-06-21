package com.example.data.mapper

import com.example.database.entity.DayStickerEntity
import com.example.database.entity.DrawingStrokeEntity
import com.example.database.entity.PointEntity
import com.example.database.entity.TaskEntity
import com.example.domain.model.BoardSticker
import com.example.domain.model.DrawingStroke
import com.example.domain.model.StrokePoint
import com.example.domain.model.Task
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class DayMappersTest {

    @Test
    fun `TaskEntity toDomain should map all fields and handle nulls correctly`() {
        val entity = TaskEntity(
            id = "task-1",
            dayId = "day-1",
            title = "Купить молоко",
            time = null,
            description = null,
            isCompleted = true
        )

        val domain = entity.toDomain()

        assertEquals("task-1", domain.id)
        assertEquals("Купить молоко", domain.title)
        assertEquals(0L, domain.time)
        assertEquals("", domain.note)
        assertEquals(true, domain.isCompleted)
    }

    @Test
    fun `Task toEntity should map all fields and add dayId correctly`() {
        val domain = Task(
            id = "task-1",
            title = "Купить молоко",
            time = 1200L,
            note = "Не забыть сыр",
            isCompleted = false
        )
        val dayId = "day-123"

        val entity = domain.toEntity(dayId)

        assertEquals("task-1", entity.id)
        assertEquals("day-123", entity.dayId)
        assertEquals("Купить молоко", entity.title)
        assertEquals(1200L, entity.time)
        assertEquals("Не забыть сыр", entity.description)
        assertEquals(false, entity.isCompleted)
    }

    @Test
    fun `DayStickerEntity toDomain should map all fields correctly`() {
        val entity = DayStickerEntity(
            id = "sticker-1",
            dayId = "day-1",
            stickerName = "Важно!",
            colorArgb = 123456L,
            posX = 10.5F,
            posY = 20.5F
        )

        val domain = entity.toDomain()

        assertEquals("sticker-1", domain.id)
        assertEquals("Важно!", domain.text)
        assertEquals(123456L, domain.colorArgb)
        assertEquals(10.5F, domain.offsetX)
        assertEquals(20.5F, domain.offsetY)
    }

    @Test
    fun `BoardSticker toEntity should map all fields and add dayId correctly`() {
        val domain = BoardSticker(
            id = "sticker-1",
            text = "Важно!",
            colorArgb = 123456L,
            offsetX = 10.5F,
            offsetY = 20.5F
        )
        val dayId = "day-123"

        val entity = domain.toEntity(dayId)

        assertEquals("sticker-1", entity.id)
        assertEquals("day-123", entity.dayId)
        assertEquals("Важно!", entity.stickerName)
        assertEquals(123456L, entity.colorArgb)
        assertEquals(10.5F, entity.posX)
        assertEquals(20.5F, entity.posY)
    }

    @Test
    fun `DrawingStrokeEntity toDomain should map all fields including points list`() {
        val entity = DrawingStrokeEntity(
            id = "stroke-1",
            ownerId = "day-1",
            colorArgb = 123456L,
            isEraser = false,
            points = listOf(
                PointEntity(x = 10.5F, y = 20.5F),
                PointEntity(x = 30.5F, y = 40.5F),
                PointEntity(x = 50.5F, y = 60.5F)
            )
        )

        val domain = entity.toDomain()

        assertEquals("stroke-1", domain.id)
        assertEquals(123456L, domain.colorArgb)
        assertEquals(false, domain.isEraser)
        assertEquals(3, domain.points.size)

        assertEquals(10.5F, domain.points[0].x)
        assertEquals(20.5F, domain.points[0].y)
        assertEquals(30.5F, domain.points[1].x)
        assertEquals(40.5F, domain.points[1].y)
        assertEquals(50.5F, domain.points[2].x)
        assertEquals(60.5F, domain.points[2].y)
    }

    @Test
    fun `DrawingStrokeEntity toDomain should handle empty points list`() {
        val entity = DrawingStrokeEntity(
            id = "stroke-1",
            ownerId = "day-1",
            colorArgb = 123456L,
            isEraser = false,
            points = emptyList()
        )

        val domain = entity.toDomain()

        assertEquals("stroke-1", domain.id)
        assertEquals(0, domain.points.size)
    }

    @Test
    fun `DrawingStroke toEntity should map all fields including points list and add ownerId`() {
        val domain = DrawingStroke(
            id = "stroke-1",
            colorArgb = 123456L,
            isEraser = true,
            points = listOf(
                StrokePoint(x = 10.5F, y = 20.5F),
                StrokePoint(x = 30.5F, y = 40.5F)
            )
        )
        val ownerId = "day-123"

        val entity = domain.toEntity(ownerId)

        assertEquals("stroke-1", entity.id)
        assertEquals("day-123", entity.ownerId)
        assertEquals(123456L, entity.colorArgb)
        assertEquals(true, entity.isEraser)
        assertEquals(2, entity.points.size)

        assertEquals(10.5F, entity.points[0].x)
        assertEquals(20.5F, entity.points[0].y)
        assertEquals(30.5F, entity.points[1].x)
        assertEquals(40.5F, entity.points[1].y)
    }

    @Test
    fun `DrawingStroke toEntity should handle empty points list`() {
        val domain = DrawingStroke(
            id = "stroke-1",
            colorArgb = 123456L,
            isEraser = false,
            points = emptyList()
        )
        val ownerId = "day-1"

        val entity = domain.toEntity(ownerId)

        assertEquals("stroke-1", entity.id)
        assertEquals("day-1", entity.ownerId)
        assertEquals(0, entity.points.size)
    }

    @Test
    fun `DrawingStroke round trip should preserve all data`() {
        val original = DrawingStroke(
            id = "stroke-1",
            colorArgb = 999888L,
            isEraser = false,
            points = listOf(
                StrokePoint(x = 1.0F, y = 2.0F),
                StrokePoint(x = 3.0F, y = 4.0F),
                StrokePoint(x = 5.0F, y = 6.0F)
            )
        )
        val ownerId = "day-1"

        val entity = original.toEntity(ownerId)
        val restored = entity.toDomain()

        assertEquals(original.id, restored.id)
        assertEquals(original.colorArgb, restored.colorArgb)
        assertEquals(original.isEraser, restored.isEraser)
        assertEquals(original.points.size, restored.points.size)

        original.points.forEachIndexed { index, point ->
            assertEquals(point.x, restored.points[index].x)
            assertEquals(point.y, restored.points[index].y)
        }
    }
}