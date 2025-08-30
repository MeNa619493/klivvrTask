package com.example.klivvrtask.di

import com.example.klivvrtask.data.ds.CityTrie
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
internal class TrieModule {

    @Provides
    fun provideTrie() : CityTrie = CityTrie()
}