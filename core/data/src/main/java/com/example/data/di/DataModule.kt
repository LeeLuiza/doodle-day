package com.example.data.di

import com.example.data.repository.DayRepositoryImpl
import com.example.data.repository.MonthRepositoryImpl
import com.example.domain.repository.DayRepository
import com.example.domain.repository.MonthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindDayRepository(impl: DayRepositoryImpl): DayRepository

    @Binds
    @Singleton
    abstract fun bindMonthRepository(impl: MonthRepositoryImpl): MonthRepository
}