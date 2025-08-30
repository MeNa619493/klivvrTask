package com.example.klivvrtask.di

import com.example.klivvrtask.data.repository.CityRepositoryImpl
import com.example.klivvrtask.domain.repository.CityRepository
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
    abstract fun bindCityRepository(
        cityRepositoryImpl: CityRepositoryImpl
    ): CityRepository
}