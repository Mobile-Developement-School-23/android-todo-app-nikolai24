package com.example.todoapp

import java.util.*

object DateConverter {
    fun getIntDate(): Triple<Int, Int, Int>{
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        return Triple(day, month, year)
    }

    fun getIntDate(str: String): Triple<Int, Int, Int>{
        val list = str.split(" ").map { x -> x.toInt() }
        val year = list[2]
        val month = list[1]
        val day = list[0]
        return Triple(day, month, year)
    }

    fun getStringDate(): String{
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        return "$day $month $year"
    }

    fun dateConvert(str: String): String{
        val list = str.split(" ").map { x -> x.toInt() }
        val day = list[0]
        val year = list[2]
        var date = ""
        var month = ""
        when (list[1]){
            0 -> month = "января"
            1 -> month = "февраля"
            2 -> month = "марта"
            3 -> month = "апреля"
            4 -> month = "мая"
            5 -> month = "июня"
            6 -> month = "июля"
            7 -> month = "августа"
            8 -> month = "сентября"
            9 -> month = "октября"
            10 -> month = "ноября"
            11 -> month = "декабря"
        }
        date = "$day $month $year"
        return date
    }
}