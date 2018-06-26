package com.chanpyaeaung.weatherapp.api

import com.chanpyaeaung.data.entites.WeatherEntity
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("weather")
    fun getCurrentWeatherDataByLatLong(@Query("lat") lat: Double,
                              @Query("lon") lgt: Double,
                              @Query("units") unit: String): Call<WeatherEntity>

}