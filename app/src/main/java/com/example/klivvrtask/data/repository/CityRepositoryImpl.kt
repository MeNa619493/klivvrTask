package com.example.klivvrtask.data.repository

import android.content.Context
import com.example.klivvrtask.R
import com.example.klivvrtask.data.ds.CityTrie
import com.example.klivvrtask.data.mapper.toDomain
import com.example.klivvrtask.data.model.CityDto
import com.example.klivvrtask.di.DefaultDispatcher
import com.example.klivvrtask.di.IODispatcher
import com.example.klivvrtask.domain.model.City
import com.example.klivvrtask.domain.repository.CityRepository
import com.example.klivvrtask.utils.Resource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CityRepositoryImpl @Inject constructor(
    @ApplicationContext val context: Context,
    private val trie: CityTrie,
    @IODispatcher val ioDispatcher: CoroutineDispatcher,
    @DefaultDispatcher val defaultDispatcher: CoroutineDispatcher
) : CityRepository {

    override fun getCities(): Flow<Resource<List<City>>> = flow {
        emit(Resource.Loading())
        try {
            val cities = withContext(ioDispatcher) {
                val json = Json { ignoreUnknownKeys = true }
                val jsonString = context.resources.openRawResource(R.raw.cities)
                    .bufferedReader()
                    .use { it.readText() }
                val cityDtos = json.decodeFromString<List<CityDto>>(jsonString)
                val mappedCities = cityDtos.map { dto -> dto.toDomain() }
                val sortedList = mappedCities.sortedBy { it.name }
                trie.populateTrie(sortedList)
                sortedList
            }
            emit(Resource.Success(cities))
        } catch (e: Exception) {
            emit(Resource.Error("something went wrong"))
            e.printStackTrace()
        }
    }

    override fun searchCities(query: String): Flow<Resource<List<City>>> = flow {
        emit(Resource.Loading())
        try {
            val citiesMap = withContext(defaultDispatcher) {
                trie.searchByPrefix(query)
            }
            emit(Resource.Success(citiesMap))
        } catch (e: Exception) {
            emit(Resource.Error("something went wrong"))
            e.printStackTrace()
        }
    }
}