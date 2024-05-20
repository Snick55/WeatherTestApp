package com.snick55.weathertestapp.data

import com.snick55.weathertestapp.data.entities.CityNameResult
import retrofit2.http.GET
import retrofit2.http.Query

interface CityNameService {

    @GET("reverse")
    suspend fun getNameByLatLon(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
    ): CityNameResult

}