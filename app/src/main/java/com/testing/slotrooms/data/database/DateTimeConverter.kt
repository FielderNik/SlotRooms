package com.testing.slotrooms.data.database

import androidx.room.TypeConverter
import kotlinx.datetime.LocalDateTime

class DateTimeConverter {

    @TypeConverter
    fun fromStringToDateTime(stringDateTime: String) : LocalDateTime {
        return LocalDateTime.parse(stringDateTime)
    }

    @TypeConverter
    fun fromDateTimeToString(dateTime: LocalDateTime) : String {
        return dateTime.toString()
    }
}