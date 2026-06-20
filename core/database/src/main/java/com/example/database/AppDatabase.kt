package com.example.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.database.dao.DrawingDao
import com.example.database.dao.DayStickerDao
import com.example.database.dao.TaskDao
import com.example.database.entity.DayStickerEntity
import com.example.database.entity.DrawingStrokeEntity
import com.example.database.entity.TaskEntity
import com.example.database.converter.PointsTypeConverter

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