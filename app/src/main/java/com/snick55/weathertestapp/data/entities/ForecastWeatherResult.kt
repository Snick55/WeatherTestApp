package com.snick55.weathertestapp.data.entities

data class ForecastWeatherResult(
    val current: Current,
    val current_units: CurrentUnits,
    val daily: Daily,
    val daily_units: DailyUnits,
    val elevation: Int,
    val generationtime_ms: Double,
    val latitude: Double,
    val longitude: Double,
    val timezone: String,
    val timezone_abbreviation: String,
    val utc_offset_seconds: Int
) {
    data class Current(
        val interval: Int,
        val temperature_2m: Double,
        val time: String
    )

    data class CurrentUnits(
        val interval: String,
        val temperature_2m: String,
        val time: String
    )

    data class Daily(
        val temperature_2m_max: List<Double>,
        val time: List<String>
    )

    data class DailyUnits(
        val temperature_2m_max: String,
        val time: String
    )
    fun toWeatherData() = WeatherData(current.temperature_2m,daily.time,daily.temperature_2m_max)
}