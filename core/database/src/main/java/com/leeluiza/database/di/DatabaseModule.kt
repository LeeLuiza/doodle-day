package com.leeluiza.database.di

import android.content.Context
import androidx.room.Room
import com.leeluiza.database.dao.DrawingDao
import com.leeluiza.database.dao.DayStickerDao
import com.leeluiza.database.dao.TaskDao
import com.leeluiza.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context, AppDatabase::class.java, "planner-database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideTaskDao(database: AppDatabase): TaskDao {
        return database.taskDao()
    }

    @Provides
    fun provideDayStickerDao(database: AppDatabase): DayStickerDao {
        return database.dayStickerDao()
    }

    @Provides
    fun provideDayDrawingDao(database: AppDatabase): DrawingDao {
        return database.dayDrawingDao()
    }
}