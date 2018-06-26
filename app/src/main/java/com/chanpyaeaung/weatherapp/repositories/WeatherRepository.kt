package com.chanpyaeaung.weatherapp.repositories

import android.arch.lifecycle.LiveData
import com.chanpyaeaung.data.entites.WeatherEntity
import com.chanpyaeaung.weatherapp.api.WeatherApi
import com.chanpyaeaung.weatherapp.common.Utils
import com.chanpyaeaung.weatherapp.database.dao.WeatherDao
import com.chanpyaeaung.weatherapp.entities.WeatherModelEntity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import java.util.concurrent.Executor
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class WeatherRepository @Inject constructor(weatherApi: WeatherApi, weatherDao: WeatherDao, executor: Executor) {

    private val mWeatherApi = weatherApi
    private val mWeatherDao = weatherDao
    private val mExecutor = executor

    private val EXPIRE_IN_MINUTES = 1
    private val units = "metric"

    fun getWeatherByLatLong(lat: Double, long: Double): LiveData<WeatherModelEntity> {
        refreshWeather(lat, long)
        return mWeatherDao.getWeatherByLatLong(lat, long)!!
    }

    fun getAllWeather(): LiveData<List<WeatherModelEntity>> {
        return mWeatherDao.getAllWeathers()
    }

    fun removeWeather(id: Int) {
        mExecutor.execute {
            mWeatherDao.removeWeatherById(id)
        }
    }

    fun removeWeather(weatherModelEntity: WeatherModelEntity) {
        mExecutor.execute {
            mWeatherDao.removeWeather(weatherModelEntity)
        }
    }

    private fun refreshWeather(lat: Double, long: Double) {
        mExecutor.execute {
            val weatherExists = mWeatherDao.hasWeather(lat, long, getExpiryTime(Date())) != null
            if(!weatherExists) {
                mWeatherApi.getCurrentWeatherDataByLatLong(lat, long, units).enqueue(object : Callback<WeatherEntity> {
                    override fun onResponse(call: Call<WeatherEntity>, response: Response<WeatherEntity>) {
                        mExecutor.execute {
                            val weather = response.body()
                            if (weather != null) {
                                var weatherModel = WeatherModelEntity(
                                        weather.id,
                                        weather.name,
                                        weather.weather.get(0).main,
                                        weather.weather.get(0).description,
                                        Utils.getWeatherIcon(weather.weather.get(0).icon),
                                        weather.main.temp,
                                        weather.main.temp_min,
                                        weather.main.temp_max,
                                        weather.clouds.all,
                                        weather.wind.speed,
                                        weather.wind.deg,
                                        weather.sys.country,
                                        lat,
                                        long,
                                        Date()
                                )
                                mWeatherDao.saveWeather(weatherModel)
                            }
                        }
                    }

                    override fun onFailure(call: Call<WeatherEntity>, t: Throwable) {
                        t.printStackTrace()
                    }
                })
            }
        }
    }


    private fun getExpiryTime(now: Date): Date {
        val cal = Calendar.getInstance()
        cal.time = now
        cal.add(Calendar.MINUTE, -EXPIRE_IN_MINUTES)
        return cal.time
    }

}