package com.snick55.weathertestapp.data

import com.snick55.weathertestapp.core.AppException
import com.snick55.weathertestapp.core.GenericException
import com.snick55.weathertestapp.core.NoInternetException
import com.snick55.weathertestapp.core.ServerUnavailableException
import com.snick55.weathertestapp.core.TimeOutException
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

interface ErrorHandler {

    fun handle(exception: Exception): AppException

    class ErrorHandlerImpl @Inject constructor(): ErrorHandler{

            override fun handle(exception: Exception): AppException {
                return when(exception){
                    is UnknownHostException -> NoInternetException()
                    is HttpException -> ServerUnavailableException()
                    is SocketTimeoutException -> TimeOutException()
                    else -> GenericException()
                }
            }

    }

}