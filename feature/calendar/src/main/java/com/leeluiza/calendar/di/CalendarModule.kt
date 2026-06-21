package com.leeluiza.calendar.di

import com.leeluiza.calendar.CalendarFeatureImpl
import com.leeluiza.common.FeatureApi
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