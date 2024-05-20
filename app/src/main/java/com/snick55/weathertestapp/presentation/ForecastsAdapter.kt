package com.snick55.weathertestapp.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.snick55.weathertestapp.R
import com.snick55.weathertestapp.databinding.ForecastItemBinding

class ForecastsAdapter : ListAdapter<Forecast, ForecastsAdapter.MyViewHolder>(ForecastsDiffCallback()) {

    inner class MyViewHolder(private val binding: ForecastItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(itemUi: Forecast) {
            binding.dateTV.text = itemUi.date
            binding.tempTV.text = binding.root.context.getString(R.string.temp_title,itemUi.temp.toString())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ForecastItemBinding.inflate(inflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    class ForecastsDiffCallback : DiffUtil.ItemCallback<Forecast>() {
        override fun areItemsTheSame(oldItem: Forecast, newItem: Forecast): Boolean {
            return oldItem.date == newItem.date
        }

        override fun areContentsTheSame(oldItem: Forecast, newItem: Forecast): Boolean {
            return oldItem == newItem
        }
    }
}
