package com.leeluiza.day.di

import com.leeluiza.common.FeatureApi
import com.leeluiza.day.DayFeatureImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
abstract class DayModule {

    @Binds
    @IntoSet
    abstract fun bindsDayFeature(dayFeatureImpl: DayFeatureImpl): FeatureApi
}