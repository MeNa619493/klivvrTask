package com.example.klivvrtask.data.mapper

import com.example.klivvrtask.data.model.CityDto
import com.example.klivvrtask.domain.model.City
import com.example.klivvrtask.domain.model.Coordinates

fun CityDto.toDomain(): City {
    return City(
        id = _id,
        name = name,
        country = country,
        coordinates = Coordinates(
            longitude = coord.lon,
            latitude = coord.lat
        )
    )
}