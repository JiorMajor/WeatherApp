package com.chanpyaeaung.weatherapp.database.converters

import android.arch.persistence.room.TypeConverter
import java.util.*

class DateConverter {

    @TypeConverter
    fun toDate(timesStamp: Long?): Date? {
        if(timesStamp == null) {
            return null
        }
        return Date(timesStamp)
    }

    @TypeConverter
    fun toTimeStamp(date: Date?): Long? {
        if(date == null)
            return null
        return date.time
    }

}