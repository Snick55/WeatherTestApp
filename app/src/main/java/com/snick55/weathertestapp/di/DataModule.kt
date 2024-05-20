package com.snick55.weathertestapp.di

import com.snick55.weathertestapp.data.ErrorHandler
import com.snick55.weathertestapp.data.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    abstract fun bindErrorHandler(errorHandler: ErrorHandler.ErrorHandlerImpl): ErrorHandler

    @Binds
    abstract fun bindWeatherRepository(repository: WeatherRepository.WeatherRepositoryImpl): WeatherRepository

}