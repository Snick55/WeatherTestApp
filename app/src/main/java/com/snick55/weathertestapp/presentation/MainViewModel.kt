package com.snick55.weathertestapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.snick55.weathertestapp.core.Container
import com.snick55.weathertestapp.data.WeatherRepository
import com.snick55.weathertestapp.di.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: WeatherRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val dateFormatter: DateFormatter
) : ViewModel() {

    private val innerWeather: MutableSharedFlow<Container<WeatherUi>> =
        MutableSharedFlow(replay = 1)
    val weather: SharedFlow<Container<WeatherUi>> = innerWeather.asSharedFlow()
    private var job: Job? = null


    fun getWeatherByLatLon(lat: Double, lon: Double) {
        if (job?.isActive == true) {
            job?.cancel()
        }
        innerWeather.tryEmit(Container.Pending)
        job = viewModelScope.launch(ioDispatcher) {
            val forecast = async {
                repository.getWeatherByLatLon(lat, lon)
            }.await()
            val cityName = async {
                repository.getCityNameByLatLon(lat, lon)
            }.await()



            innerWeather.emit(forecast.map {
                val forecasts = it.times.take(10)
                    .zip(it.temps.take(10)).map { (date, temperature) ->
                        Forecast(dateFormatter.format(date), temperature)
                    }
                WeatherUi(cityName, it.currentTemp, forecasts)
            })
        }
    }

}