package com.snick55.weathertestapp.di

import com.snick55.weathertestapp.data.CityNameService
import com.snick55.weathertestapp.data.ForecastService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class ForecastClient

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class CityNameClient

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class ForecastRetrofit

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class CityNameRetrofit

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun createInterceptor(): Interceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @CityNameClient
    @Provides
    fun provideCityNameClient(
        interceptor: Interceptor,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(Interceptor { chain ->
                val original = chain.request()
                val newUrl = original.url.newBuilder()
                    .addQueryParameter("appid", "37d34d4b86d03710f1f3318ce1223ee5")
                    .build()
                val newRequest = original.newBuilder()
                    .url(newUrl)
                    .build()
                chain.proceed(newRequest)
            })
            .addInterceptor(interceptor)
            .build()
    }

    @CityNameRetrofit
    @Provides
    @Singleton
    fun provideCityNameRetrofit(
        @CityNameClient client: OkHttpClient,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/geo/1.0/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @ForecastClient
    @Provides
    fun provideForecastClient(
        interceptor: Interceptor,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }

    @ForecastRetrofit
    @Provides
    @Singleton
    fun provideForecastRetrofit(
        @ForecastClient client: OkHttpClient,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.open-meteo.com/v1/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    @Provides
    fun provideForecastService(
        @ForecastRetrofit retrofit: Retrofit
    ): ForecastService {
        return retrofit.create(ForecastService::class.java)
    }

    @Provides
    fun provideCityNameService(
        @CityNameRetrofit retrofit: Retrofit
    ): CityNameService {
        return retrofit.create(CityNameService::class.java)
    }

}
