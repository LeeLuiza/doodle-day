package com.example.data.repository

import com.example.data.mapper.toDomain
import com.example.data.mapper.toEntity
import com.example.database.dao.DrawingDao
import com.example.database.dao.DayStickerDao
import com.example.database.dao.TaskDao
import com.example.domain.model.BoardSticker
import com.example.domain.model.DrawingStroke
import com.example.domain.model.ErrorType
import com.example.domain.model.Result
import com.example.domain.model.Task
import com.example.domain.repository.DayRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DayRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao,
    private val dayStickerDao: DayStickerDao,
    private val drawingDao: DrawingDao
) : DayRepository {

    override fun getTasks(dayId: String): Flow<Result<List<Task>>> {
        return taskDao.getTasksByDayId(dayId)
            .map { list -> Result.Success(list.map { it.toDomain() }) as Result<List<Task>> }
            .catch { emit(Result.Error(ErrorType.DATA_LOAD_ERROR)) }
    }

    override suspend fun addTask(dayId: String, task: Task): Result<Unit> {
        return try {
            taskDao.insertTask(task.toEntity(dayId))
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(ErrorType.DATA_SAVE_ERROR)
        }
    }

    override suspend fun deleteTask(taskId: String): Result<Unit> {
        return try {
            taskDao.deleteTaskById(taskId)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(ErrorType.DATA_DELETE_ERROR)
        }
    }

    override suspend fun updateTask(dayId: String, task: Task): Result<Unit> {
        return try {
            taskDao.updateTask(task.toEntity(dayId))
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(ErrorType.DATA_SAVE_ERROR)
        }
    }

    override suspend fun completeTask(taskId: String): Result<Unit> {
        return try {
            taskDao.completeTask(taskId)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(ErrorType.DATA_SAVE_ERROR)
        }
    }

    override fun getStickers(dayId: String): Flow<Result<List<BoardSticker>>> {
        return dayStickerDao.getStickersByDayId(dayId)
            .map { list -> Result.Success(list.map { it.toDomain() }) as Result<List<BoardSticker>> }
            .catch { emit(Result.Error(ErrorType.DATA_LOAD_ERROR)) }
    }

    override suspend fun addSticker(dayId: String, sticker: BoardSticker): Result<Unit> {
        return try {
            dayStickerDao.insertOrUpdateSticker(sticker.toEntity(dayId))
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(ErrorType.DATA_SAVE_ERROR)
        }
    }

    override suspend fun updateSticker(dayId: String, sticker: BoardSticker): Result<Unit> {
        return try {
            dayStickerDao.insertOrUpdateSticker(sticker.toEntity(dayId))
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(ErrorType.DATA_SAVE_ERROR)
        }
    }

    override suspend fun deleteSticker(stickerId: String): Result<Unit> {
        return try {
            dayStickerDao.deleteStickerById(stickerId)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(ErrorType.DATA_DELETE_ERROR)
        }
    }

    override fun getStrokes(dayId: String): Flow<Result<List<DrawingStroke>>> {
        return drawingDao.getDrawingsFor(dayId)
            .map { entities ->
                Result.Success(entities.map { it.toDomain() }) as Result<List<DrawingStroke>>
            }
            .catch { emit(Result.Error(ErrorType.DATA_LOAD_ERROR)) }
    }

    override suspend fun addStrokes(dayId: String, strokes: List<DrawingStroke>): Result<Unit> {
        return try {
            strokes.forEach { stroke ->
                drawingDao.saveStroke(stroke.toEntity(dayId))
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(ErrorType.DATA_SAVE_ERROR)
        }
    }

    override suspend fun replaceAllDrawings(dayId: String, strokes: List<DrawingStroke>): Result<Unit> {
        return try {
            val entities = strokes.map { it.toEntity(dayId) }
            drawingDao.replaceAllDrawings(dayId, entities)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(ErrorType.DATA_SAVE_ERROR)
        }
    }
}