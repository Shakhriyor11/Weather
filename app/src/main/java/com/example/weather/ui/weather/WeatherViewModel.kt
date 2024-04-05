package com.example.weather.ui.weather

import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.data.repository.WeatherRepository
import com.example.weather.utils.DEFAULT_WEATHER_DESTINATION
import com.example.weather.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository,
) : ViewModel() {

    private val _uiState: MutableStateFlow<WeatherUIState> =
        MutableStateFlow(WeatherUIState(isLoading = true))
    val uiState: StateFlow<WeatherUIState> = _uiState.asStateFlow()

    private val _searchWidgetState: MutableState<SearchWidgetState> =
        mutableStateOf(value = SearchWidgetState.CLOSED)
    val searchWidgetState: State<SearchWidgetState> = _searchWidgetState

    private val _searchTextState: MutableState<String> = mutableStateOf(value = "")
    val searchTextState: State<String> = _searchTextState

    fun updateSearchWidgetState(newValue: SearchWidgetState) {
        _searchWidgetState.value = newValue
    }

    fun updateSearchTextState(newValue: String) {
        _searchTextState.value = newValue
    }

    init {
        getWeather()
    }

    fun getWeather(city: String = DEFAULT_WEATHER_DESTINATION) {
        repository.getWeatherForecast(city).map { result ->
            when (result) {
                is Result.Success -> {
                    _uiState.value = WeatherUIState(weather = result.data)
                }

                is Result.Error -> {
                    _uiState.value = WeatherUIState(errorMessage = result.errorMessage)
                }

                Result.Loading -> {
                    _uiState.value = WeatherUIState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

}