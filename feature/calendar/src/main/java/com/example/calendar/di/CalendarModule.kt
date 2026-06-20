package com.example.calendar.di

import com.example.calendar.CalendarFeatureImpl
import com.example.common.FeatureApi
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
abstract class CalendarModule {

    @Binds
    @IntoSet
    abstract fun bindsCalendarFeature(calendarFeatureImpl: CalendarFeatureImpl): FeatureApi
}