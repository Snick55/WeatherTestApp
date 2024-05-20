package com.snick55.weathertestapp.presentation

data class WeatherUi(
    val name: String,
    val currentTemp: Double,
    val forecasts: List<Forecast>,
)

data class Forecast(
    val date: String,
    val temp: Double
)
