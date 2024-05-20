package com.snick55.weathertestapp.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import javax.inject.Inject

interface DateFormatter {

    fun format(date: String): String


    class DateFormatterImpl @Inject constructor() : DateFormatter {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun format(date: String): String {
            val localeDate = LocalDate.parse(date)
            val today = LocalDate.now()
            return if (date == today.toString())
                "TODAY"
             else
                 localeDate.dayOfWeek.toString()
        }
    }

}