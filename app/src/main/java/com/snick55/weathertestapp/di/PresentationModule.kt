package com.snick55.weathertestapp.di

import com.snick55.weathertestapp.presentation.DateFormatter
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class PresentationModule {

    @Binds
    abstract fun bindDateFormatter(dateFormatter: DateFormatter.DateFormatterImpl): DateFormatter

}