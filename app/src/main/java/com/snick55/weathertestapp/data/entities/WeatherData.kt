package com.snick55.weathertestapp.data.entities

data class WeatherData(
    val currentTemp: Double,
    val times: List<String>,
    val temps: List<Double>
)
