package com.example.klivvrtask.domain.repository

import com.example.klivvrtask.domain.model.City
import com.example.klivvrtask.utils.Resource
import kotlinx.coroutines.flow.Flow

interface CityRepository {
     fun getCities(): Flow<Resource<List<City>>>
     fun searchCities(query: String): Flow<Resource<List<City>>>
}