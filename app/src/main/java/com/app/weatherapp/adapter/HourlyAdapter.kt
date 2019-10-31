package com.app.weatherapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.weatherapp.MainActivity
import com.app.weatherapp.databinding.HourlyItemViewBinding
import com.app.weatherapp.module.WeatherInfo
import java.text.SimpleDateFormat
import java.util.*


class HourlyAdapter: RecyclerView.Adapter<HourlyItemView>(){

    var hourlyInfo: List<WeatherInfo> = arrayListOf()
    set(value) {
        field = value
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyItemView {
       return HourlyItemView.inflate(parent)
    }

    override fun getItemCount(): Int {
        return hourlyInfo.size
    }

    override fun onBindViewHolder(holder: HourlyItemView, position: Int) {
        holder.weatherInfo = hourlyInfo[position]
    }

}

class HourlyItemView private constructor(val bindingView: HourlyItemViewBinding): RecyclerView.ViewHolder(bindingView.root){

    var weatherInfo: WeatherInfo? = null

    set(value) {
        field = value
        val weatherInfo = value ?: return
        bindingView.weather = weatherInfo
        bindingView.time = MainActivity.DateFormat.format(Date(weatherInfo.time * 1000))
    }

    companion object{
        fun inflate(parent: ViewGroup): HourlyItemView{
            return HourlyItemView(HourlyItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false ))
        }
    }

}