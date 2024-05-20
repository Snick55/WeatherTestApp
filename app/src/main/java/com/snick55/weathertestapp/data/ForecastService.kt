package com.snick55.weathertestapp.data

import com.snick55.weathertestapp.data.entities.ForecastWeatherResult
import retrofit2.http.GET
import retrofit2.http.Query

interface ForecastService {

    @GET("forecast")
    suspend fun getForecastWeather(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("current") currentTemp: String = "temperature_2m",
        @Query("daily") daily: String = "temperature_2m_max",
        @Query("forecast_days") forecastDays: Int = 14,
    ):ForecastWeatherResult

}