package com.poc.weatherapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.poc.weatherapp.BaseConfig
import com.poc.weatherapp.R
import com.poc.weatherapp.model.Data
import com.poc.weatherapp.utils.BaseUtils
import kotlinx.android.synthetic.main.hourly_weather_list_item_view.view.*

class WeatherDataAdapter(val mContext: Context) : RecyclerView.Adapter<WeatherDataAdapter.ViewHolder>() {
    var listItems: List<Data>

    init {
        listItems = ArrayList()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listItems.get(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.hourly_weather_list_item_view, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listItems.size
    }

    fun setItem(listItems: List<Data>) {
        this.listItems = listItems
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: Data) {
            itemView.txtTime.text = BaseUtils.getDate(data.time, BaseConfig.FORMAT_HOURS)
            itemView.txtTemp.text = mContext.getString(R.string.current_weather, data.temperature.toInt())
        }
    }
}