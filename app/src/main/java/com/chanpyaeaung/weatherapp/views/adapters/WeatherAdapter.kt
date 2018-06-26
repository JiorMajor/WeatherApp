package com.chanpyaeaung.weatherapp.views.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chanpyaeaung.weatherapp.R
import com.chanpyaeaung.weatherapp.entities.WeatherModelEntity
import kotlinx.android.synthetic.main.weather_list_item.view.*

class WeatherAdapter(val context: Context, var items: ArrayList<WeatherModelEntity>, val customClickListener: CustomClickListener): RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {

    var mItems = items

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        return WeatherViewHolder(LayoutInflater.from(context).inflate(R.layout.weather_list_item, parent, false))
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    fun replaceItems(items: ArrayList<WeatherModelEntity>) {
        this.mItems = items
        this.notifyDataSetChanged()
    }

    fun getItem(position: Int): WeatherModelEntity {
        return mItems.get(position)
    }

    fun removeItem(item: WeatherModelEntity) {
        this.mItems.remove(item)
        this.notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder?.ivWeather.setImageDrawable(context.resources.getDrawable(getItem(position).icon))
        holder?.tvCity.text = getItem(position).name
        holder?.tvWeather.text = getItem(position).condition
        holder?.root.setOnClickListener { customClickListener.onItemClick(holder?.root.rootView, position) }
    }


    class WeatherViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val ivWeather = view.ivWeather
        val tvCity = view.tvCity
        val tvWeather = view.tvWeather
        val root = view.root
    }

    interface CustomClickListener {
        fun onItemClick(view: View, position: Int)
    }
}