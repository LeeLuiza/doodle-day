package com.leeluiza.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.leeluiza.database.dao.DrawingDao
import com.leeluiza.database.dao.DayStickerDao
import com.leeluiza.database.dao.TaskDao
import com.leeluiza.database.entity.DayStickerEntity
import com.leeluiza.database.entity.DrawingStrokeEntity
import com.leeluiza.database.entity.TaskEntity
import com.leeluiza.database.converter.PointsTypeConverter

@Database(entities = [
    TaskEntity::class,
    DayStickerEntity::class,
    DrawingStrokeEntity::class
    ],
    version = 1,
)
@TypeConverters(
    value = [PointsTypeConverter::class]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun dayStickerDao(): DayStickerDao
    abstract fun dayDrawingDao(): DrawingDao
}