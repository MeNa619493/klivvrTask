package com.example.klivvrtask.ui

import androidx.compose.runtime.Immutable
import com.example.klivvrtask.domain.model.City

@Immutable
data class CitySearchState(
    val isLoading: Boolean = true,
    val searchQuery: String? = null,
    val cityGroups: Map<Char, List<City>> = emptyMap(),
    val error: String? = null,
)

sealed class CitySearchIntent {
    object LoadCities : CitySearchIntent()
    data class OnSearchQueryChanged(val query: String) : CitySearchIntent()
    data class OnCityClicked(val city: City) : CitySearchIntent()
}

sealed class CitySearchSideEffect {
    object ScrollToFirstElement : CitySearchSideEffect()
    data class OpenGoogleMaps(val city: City) : CitySearchSideEffect()
}