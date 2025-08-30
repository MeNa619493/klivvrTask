package com.example.klivvrtask.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DispatchersModule {
    @Provides
    @Singleton
    @IODispatcher
    fun provideIODispatcher() = Dispatchers.IO

    @Provides
    @Singleton
    @DefaultDispatcher
    fun provideDefaultDispatcher() = Dispatchers.Default
}