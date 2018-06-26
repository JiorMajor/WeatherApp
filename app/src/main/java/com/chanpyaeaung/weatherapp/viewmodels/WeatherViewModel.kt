package com.chanpyaeaung.weatherapp.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.chanpyaeaung.data.entites.WeatherEntity
import com.chanpyaeaung.weatherapp.entities.WeatherModelEntity
import com.chanpyaeaung.weatherapp.repositories.WeatherRepository
import javax.inject.Inject

class WeatherViewModel @Inject constructor(weatherRepository: WeatherRepository) : ViewModel() {

    val mWeatherRepository = weatherRepository

    private lateinit var weatherEntity: LiveData<WeatherModelEntity>

    fun setLocation(lat: Double, lgt: Double) {
        this.weatherEntity = mWeatherRepository.getWeatherByLatLong(lat, lgt)
    }

    fun getWeather(): LiveData<WeatherModelEntity> {
        return this.weatherEntity
    }

    fun getWeatherList(): LiveData<List<WeatherModelEntity>> {
        return mWeatherRepository.getAllWeather()
    }

    fun deleteWeather(id: Int) {
        mWeatherRepository.removeWeather(id)
    }

}