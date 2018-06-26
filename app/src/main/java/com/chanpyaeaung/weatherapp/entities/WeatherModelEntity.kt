package com.chanpyaeaung.weatherapp.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

@Entity(tableName = "weathers")
data class WeatherModelEntity(

        @PrimaryKey
        var id: Int = 0,
        var name: String,
        var condition: String,
        var description: String,
        var icon: Int,
        var temp: Double,
        var temp_min: Double,
        var temp_max: Double,
        var cloudiness: Int,
        var windSpeed: Double,
        var windDirection: Double,
        var country: String,
        var latitude: Double,
        var longitude: Double,
        var lastUpdate: Date
)