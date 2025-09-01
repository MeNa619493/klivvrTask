package com.example.klivvrtask.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.klivvrtask.domain.model.City
import com.example.klivvrtask.domain.usecase.LoadCitiesUseCase
import com.example.klivvrtask.domain.usecase.SearchCitiesUseCase
import com.example.klivvrtask.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CitySearchViewModel @Inject constructor(
    private val loadCitiesUseCase: LoadCitiesUseCase,
    private val searchCitiesUseCase: SearchCitiesUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(CitySearchState())
    val state: StateFlow<CitySearchState> = _state

    private var searchJob: Job? = null

    private val _sideEffect = MutableSharedFlow<CitySearchSideEffect>()
    val sideEffect: SharedFlow<CitySearchSideEffect> = _sideEffect.asSharedFlow()

    init {
        handleIntent(CitySearchIntent.LoadCities)
        observeSearchQuery()
    }

    fun handleIntent(intent: CitySearchIntent) {
        when (intent) {
            is CitySearchIntent.LoadCities -> loadCities()
            is CitySearchIntent.OnCityClicked -> onCityClick(intent.city)
            is CitySearchIntent.OnSearchQueryChanged -> {
                _state.update { it.copy(searchQuery = intent.query) }
            }
        }
    }

    private fun loadCities() {
        viewModelScope.launch {
            loadCitiesUseCase().collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _state.update {
                            it.copy(
                                cityGroups = result.data ?: emptyMap(),
                                isLoading = false,
                                error = null
                            )
                        }
                    }

                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                error = result.message
                            )
                        }
                    }

                    is Resource.Loading -> {
                        _state.update {
                            it.copy(
                                isLoading = true
                            )
                        }
                    }
                }
            }
        }
    }

    @OptIn(FlowPreview::class)
    private fun observeSearchQuery() {
        state
            .map { it.searchQuery }
            .distinctUntilChanged()
            .debounce(500L)
            .onEach { query ->
                query?.let {
                    searchJob?.cancel()
                    searchJob = searchCities(it)
                }
            }.launchIn(viewModelScope)
    }

    private fun searchCities(query: String) =
        viewModelScope.launch {
            _sideEffect.emit(CitySearchSideEffect.ScrollToFirstElement)

            searchCitiesUseCase(query).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _state.update {
                            it.copy(
                                cityGroups = result.data ?: emptyMap(),
                                isLoading = false,
                                error = null,
                            )
                        }
                    }

                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                error = result.message
                            )
                        }
                    }

                    is Resource.Loading -> {}
                }
            }
        }

    private fun onCityClick(city: City) {
        viewModelScope.launch {
            _sideEffect.emit(CitySearchSideEffect.OpenGoogleMaps(city))
        }
    }
}