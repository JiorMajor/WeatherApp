package com.chanpyaeaung.weatherapp.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.chanpyaeaung.weatherapp.database.converters.DateConverter
import com.chanpyaeaung.weatherapp.database.dao.WeatherDao
import com.chanpyaeaung.weatherapp.entities.WeatherModelEntity

@Database(entities = arrayOf(WeatherModelEntity::class), version = 1, exportSchema = true)
@TypeConverters(DateConverter::class)
abstract class WeatherAppDatabase: RoomDatabase() {
    abstract fun weattherDao(): WeatherDao
}