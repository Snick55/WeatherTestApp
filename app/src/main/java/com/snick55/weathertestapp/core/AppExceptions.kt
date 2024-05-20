package com.snick55.weathertestapp.core

open class AppException(errorMessage: String): java.lang.Exception(errorMessage)

class NoInternetException: AppException("No Internet connection")
class ServerUnavailableException: AppException("Server unavailable")
class TimeOutException: AppException("Timeout... try again later")
class DisabledGPSException: AppException("Turn on the GPS")
class GenericException: AppException("Something went wrong")