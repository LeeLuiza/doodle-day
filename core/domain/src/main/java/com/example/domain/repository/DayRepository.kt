package com.example.domain.repository

import com.example.domain.model.BoardSticker
import com.example.domain.model.Task
import com.example.domain.model.DrawingStroke
import com.example.domain.model.Result
import kotlinx.coroutines.flow.Flow

interface DayRepository {
    fun getTasks(dayId: String): Flow<Result<List<Task>>>
    suspend fun addTask(dayId: String, task: Task): Result<Unit>
    suspend fun deleteTask(taskId: String): Result<Unit>
    suspend fun updateTask(dayId: String, task: Task): Result<Unit>
    suspend fun completeTask(taskId: String): Result<Unit>

    fun getStickers(dayId: String): Flow<Result<List<BoardSticker>>>
    suspend fun addSticker(dayId: String, sticker: BoardSticker): Result<Unit>
    suspend fun updateSticker(dayId: String, sticker: BoardSticker): Result<Unit>
    suspend fun deleteSticker(stickerId: String): Result<Unit>

    fun getStrokes(dayId: String): Flow<Result<List<DrawingStroke>>>
    suspend fun addStrokes(dayId: String, strokes: List<DrawingStroke>): Result<Unit>
    suspend fun replaceAllDrawings(dayId: String, strokes: List<DrawingStroke>): Result<Unit>
}