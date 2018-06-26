package com.chanpyaeaung.weatherapp.database.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.chanpyaeaung.data.entites.WeatherEntity
import com.chanpyaeaung.weatherapp.entities.WeatherModelEntity
import retrofit2.http.DELETE
import java.util.*


@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveWeather(weatherEntity: WeatherModelEntity)

    @Query("SELECT * FROM weathers")
    fun getAllWeathers(): LiveData<List<WeatherModelEntity>>

    @Query("SELECT * FROM weathers WHERE latitude=:lat AND longitude=:lgt ")
    fun getWeatherByLatLong(lat: Double, lgt: Double): LiveData<WeatherModelEntity>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveAllWeathers(weathers: List<WeatherModelEntity>)

    @Query("SELECT * FROM weathers WHERE latitude =:lat AND longitude =:lgt AND lastUpdate > :lastUpdate LIMIT 1")
    fun hasWeather(lat: Double, lgt: Double, lastUpdate: Date): WeatherModelEntity?

    @Delete
    fun removeWeather(weatherEntity: WeatherModelEntity)

    @Query("DELETE FROM weathers WHERE id =:id")
    fun removeWeatherById(id: Int)

}