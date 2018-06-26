package com.chanpyaeaung.data.entites

import com.google.gson.annotations.SerializedName

data class Main(

        @SerializedName("temp")
        val temp: Double,

        @SerializedName("pressure")
        val pressure: Double,

        @SerializedName("humidity")
        val humidity: Double,

        @SerializedName("temp_min")
        val temp_min: Double,

        @SerializedName("temp_max")
        val temp_max: Double
)