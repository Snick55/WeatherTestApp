package com.snick55.weathertestapp.data.entities

class CityNameResult : ArrayList<CityNameResult.CityNameResultItem>(){

    data class CityNameResultItem(
        val name: String,
    )
}