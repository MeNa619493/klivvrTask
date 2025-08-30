package com.example.klivvrtask.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class City(
    val id: Int,
    val name: String,
    val country: String,
    val coordinates: Coordinates
) {
    val displayName: String get() = "$name, $country"
}

@Immutable
data class Coordinates(
    val longitude: Double,
    val latitude: Double
)