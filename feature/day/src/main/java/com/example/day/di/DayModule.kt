package com.example.day.di

import com.example.common.FeatureApi
import com.example.day.DayFeatureImpl
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