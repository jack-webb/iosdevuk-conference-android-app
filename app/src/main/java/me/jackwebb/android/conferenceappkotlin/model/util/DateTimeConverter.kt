package me.jackwebb.android.conferenceappkotlin.model.util

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class DateTimeConverter {
    @TypeConverter
    fun stringToDate(string: String): LocalDate {
        return LocalDate.parse(string, DateTimeFormatter.ISO_DATE)
    }

    @TypeConverter
    fun dateToString(localDate: LocalDate): String {
        return localDate.format(DateTimeFormatter.ISO_DATE)
    }

    @TypeConverter
    fun stringToTime(string: String): LocalTime {
        return LocalTime.parse(string, DateTimeFormatter.ofPattern("H:m"))
    }

    @TypeConverter
    fun timeToString(localTime: LocalTime): String {
        return localTime.format(DateTimeFormatter.ofPattern("H:m"))
    }
}