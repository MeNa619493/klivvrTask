package com.example.klivvrtask.domain.usecase

import com.example.klivvrtask.domain.model.City
import com.example.klivvrtask.domain.repository.CityRepository
import com.example.klivvrtask.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SearchCitiesUseCase @Inject constructor(
    private val repository: CityRepository,
    private val groupCitiesUseCase: GroupCitiesUseCase
) {
    operator fun invoke(query: String): Flow<Resource<Map<Char, List<City>>>> {
        return repository.searchCities(query)
            .map { resource ->
                when (resource) {
                    is Resource.Success -> {
                        Resource.Success(groupCitiesUseCase(resource.data))
                    }

                    is Resource.Error -> Resource.Error(resource.message ?: "Unknown error")
                    is Resource.Loading -> Resource.Loading()
                }
            }
    }
}