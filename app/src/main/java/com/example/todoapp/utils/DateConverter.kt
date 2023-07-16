package com.example.todoapp.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.TimeZone

/**
 * Convert date to different types
 */

object DateConverter {

    fun getIntDate(longDate: Long): Triple<Int, Int, Int> {
        var date = Date(longDate * 1000)
        val calendar = Calendar.getInstance((TimeZone.getTimeZone("Europe/Moscow")))
        calendar.time = date
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        return Triple(day, month, year)
    }

    fun getIntDate(): Triple<Int, Int, Int> {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        return Triple(day, month, year)
    }

    fun getLongDate(): Long {
        return (Calendar.getInstance().timeInMillis / 1000)
    }

    fun getLongDate(year: Int, month: Int, day: Int): Long {
        val epoch = SimpleDateFormat("MM/dd/yyyy").parse("$month/$day/$year").time / 1000
        return epoch
    }

    fun dateConvert(longDate: Long?): String {
        if (longDate == null){
            return ""
        } else {
            var date = Date(longDate * 1000)
            val calendar = Calendar.getInstance((TimeZone.getTimeZone("Europe/Moscow")))
            calendar.time = date
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            var str = ""
            var monthStr = ""
            when (month) {
                0 -> monthStr = "января"
                1 -> monthStr = "февраля"
                2 -> monthStr = "марта"
                3 -> monthStr = "апреля"
                4 -> monthStr = "мая"
                5 -> monthStr = "июня"
                6 -> monthStr = "июля"
                7 -> monthStr = "августа"
                8 -> monthStr = "сентября"
                9 -> monthStr = "октября"
                10 -> monthStr = "ноября"
                11 -> monthStr = "декабря"
            }
            str = "$day $monthStr $year"
            return str
        }
    }
}