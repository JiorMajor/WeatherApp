package com.chanpyaeaung.weatherapp.common

import com.chanpyaeaung.weatherapp.R
import java.util.*

class Utils {

    companion object {

        fun getWeatherIcon(icon: String): Int {
            when(icon) {
                "02d", "02n" -> return R.drawable.cloud_2x

                "03d", "03n" -> return R.drawable.cloudy_2x

                "50d" -> return R.drawable.fog_2x

                "09d" -> return R.drawable.hail_2x

                "01n" -> return R.drawable.moon_2x

                "01d" -> return R.drawable.sun_2x

                "10d" -> return R.drawable.rain_2x

                "13d" -> return R.drawable.snow_2x

                "11d" -> return R.drawable.thunderstorm_2x

                "04d", "04n" -> return R.drawable.very_cloudy_2x

                else -> return R.drawable.ic_cloud_off
            }
        }

        fun celciusToFahrenheit(celcius: Double): Double {
            return (celcius * 9/5)+32
        }

        fun formatTempartureString(celcius: Double): String {
            val fahrenheit = celciusToFahrenheit(celcius)
            val fah = "%.2f".format(fahrenheit)
            val cel = "%.2f".format(celcius)
            return cel+" \u2103"+" / "+ fah+" \u2109"
        }

        fun getColorByTime(): Int {
            val now = Date()
            val hour = now.hours
            val min = now.minutes

            if(hour in 7..19) {
                //morning
                return R.color.colorPrimary
            } else{
                return R.color.colorAccent
            }
        }

    }

}