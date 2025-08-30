package com.example.klivvrtask.data.model

import kotlinx.serialization.Serializable

@Serializable
data class CityDto(
    val country: String,
    val name: String,
    val _id: Int,
    val coord: CoordDto
)

@Serializable
data class CoordDto(
    val lon: Double,
    val lat: Double
)