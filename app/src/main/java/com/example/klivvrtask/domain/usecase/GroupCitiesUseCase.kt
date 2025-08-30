package com.example.klivvrtask.domain.usecase

import com.example.klivvrtask.domain.model.City
import javax.inject.Inject

class GroupCitiesUseCase @Inject constructor() {
    operator fun invoke(cities: List<City>?): Map<Char, List<City>> {
        return cities?.groupBy { city ->
            city.name.firstOrNull()?.uppercaseChar() ?: '#'
        } ?: emptyMap()
    }
}