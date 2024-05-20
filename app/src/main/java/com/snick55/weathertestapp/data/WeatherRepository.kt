package com.snick55.weathertestapp.data

import com.snick55.weathertestapp.core.Container
import com.snick55.weathertestapp.data.entities.WeatherData
import javax.inject.Inject

interface WeatherRepository {

    suspend fun getWeatherByLatLon(lat: Double, lon: Double): Container<WeatherData>

    suspend fun getCityNameByLatLon(lat: Double, lon: Double): String

    class WeatherRepositoryImpl @Inject constructor(
        private val errorHandler: ErrorHandler,
        private val forecastService: ForecastService,
        private val cityNameService: CityNameService
    ) : WeatherRepository {

        override suspend fun getWeatherByLatLon(lat: Double, lon: Double): Container<WeatherData> {
            return try {
                Container.Success(
                    forecastService.getForecastWeather(
                        latitude = lat,
                        longitude = lon
                    ).toWeatherData()
                )
            } catch (e: Exception) {
                Container.Error(errorHandler.handle(e))
            }

        }

        override suspend fun getCityNameByLatLon(lat: Double, lon: Double): String {
            return try {
                if (cityNameService.getNameByLatLon(lat, lon).isNotEmpty()) {
                    cityNameService.getNameByLatLon(lat, lon)[0].name
                } else "error with catching location name"
            } catch (e: Exception) {
                "error with catching location name"
            }
        }
    }

}