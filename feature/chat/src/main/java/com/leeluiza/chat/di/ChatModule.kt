package com.leeluiza.chat.di

import com.leeluiza.chat.ChatFeatureImpl
import com.leeluiza.common.FeatureApi
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
abstract class ChatModule {

    @Binds
    @IntoSet
    abstract fun bindsChatFeature(chatFeatureImpl: ChatFeatureImpl): FeatureApi
}