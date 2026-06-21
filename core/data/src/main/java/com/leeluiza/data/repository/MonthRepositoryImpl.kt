package com.leeluiza.data.repository

import com.leeluiza.data.mapper.toDomain
import com.leeluiza.data.mapper.toEntity
import com.leeluiza.database.dao.DrawingDao
import com.leeluiza.domain.model.DrawingStroke
import com.leeluiza.domain.model.ErrorType
import com.leeluiza.domain.model.Result
import com.leeluiza.domain.repository.MonthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MonthRepositoryImpl @Inject constructor(
    private val drawingDao: DrawingDao
) : MonthRepository {

    override fun getDrawings(monthId: String): Flow<Result<List<DrawingStroke>>> {
        return drawingDao.getDrawingsFor(monthId)
            .map { entities ->
                Result.Success(entities.map { it.toDomain() }) as Result<List<DrawingStroke>>
            }
            .catch { emit(Result.Error(ErrorType.DATA_LOAD_ERROR)) }
    }

    override suspend fun addDrawings(monthId: String, strokes: List<DrawingStroke>): Result<Unit> {
        return try {
            strokes.forEach { stroke ->
                drawingDao.saveStroke(stroke.toEntity(monthId))
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