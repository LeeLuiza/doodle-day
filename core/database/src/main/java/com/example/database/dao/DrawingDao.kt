package com.example.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.database.entity.DrawingStrokeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DrawingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveStroke(stroke: DrawingStrokeEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveStrokes(strokes: List<DrawingStrokeEntity>)

    @Query("SELECT * FROM drawings WHERE ownerId = :ownerId")
    fun getDrawingsFor(ownerId: String): Flow<List<DrawingStrokeEntity>>

    @Query("DELETE FROM drawings WHERE ownerId = :ownerId")
    suspend fun clearDrawingsFor(ownerId: String)

    @Transaction
    suspend fun replaceAllDrawings(ownerId: String, newStrokes: List<DrawingStrokeEntity>) {
        clearDrawingsFor(ownerId)
        saveStrokes(newStrokes)
    }
}