package com.chanpyaeaung.data.entites

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*

data class WeatherEntity(

        @SerializedName("id")
        val id: Int = -1,

        @SerializedName("coord")
        val coord: Coord,

        @SerializedName("weather")
        val weather: List<Weather>,

        @SerializedName("base")
        val base: String,

        @SerializedName("main")
        val main: Main,

        @SerializedName("visibility")
        val visibility: Int,

        @SerializedName("wind")
        val wind: Wind,

        @SerializedName("clouds")
        val clouds: Clouds,

        @SerializedName("dt")
        val dt: Int,

        @SerializedName("sys")
        val sys: Sys,

        @SerializedName("name")
        val name: String,

        @SerializedName("cod")
        val cod: Int
)