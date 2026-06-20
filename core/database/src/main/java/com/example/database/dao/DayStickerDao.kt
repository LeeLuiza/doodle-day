package com.example.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.database.entity.DayStickerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DayStickerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateSticker(sticker: DayStickerEntity)

    @Query("DELETE FROM day_stickers WHERE id = :stickerId")
    suspend fun deleteStickerById(stickerId: String)

    @Query("SELECT * FROM day_stickers WHERE dayId = :dayId")
    fun getStickersByDayId(dayId: String): Flow<List<DayStickerEntity>>
}