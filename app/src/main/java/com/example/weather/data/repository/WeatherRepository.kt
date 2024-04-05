package com.example.weather.data.repository

import com.example.weather.model.Weather
import com.example.weather.utils.Result
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    fun getWeatherForecast(city: String): Flow<Result<Weather>>
}