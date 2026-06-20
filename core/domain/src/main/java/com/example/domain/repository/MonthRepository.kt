package com.example.domain.repository

import com.example.domain.model.DrawingStroke
import com.example.domain.model.Result
import kotlinx.coroutines.flow.Flow

interface MonthRepository {
    fun getDrawings(monthId: String): Flow<Result<List<DrawingStroke>>>
    suspend fun addDrawings(monthId: String, strokes: List<DrawingStroke>): Result<Unit>
    suspend fun replaceAllDrawings(monthId: String, strokes: List<DrawingStroke>): Result<Unit>
}