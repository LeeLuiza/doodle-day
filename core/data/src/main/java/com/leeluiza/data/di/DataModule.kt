package com.leeluiza.data.di

import com.leeluiza.data.repository.DayRepositoryImpl
import com.leeluiza.data.repository.MonthRepositoryImpl
import com.leeluiza.domain.repository.DayRepository
import com.leeluiza.domain.repository.MonthRepository
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