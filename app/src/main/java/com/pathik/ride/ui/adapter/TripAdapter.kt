package com.pathik.ride.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pathik.ride.databinding.TripItemBinding
import com.pathik.ride.model.Trip
import com.pathik.ride.utils.Util

class TripAdapter(private val list: List<Trip>) :
    RecyclerView.Adapter<TripAdapter.TripViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripViewHolder {
        return TripViewHolder(
            TripItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TripViewHolder, position: Int) {
        val trip = list[position]
        val dat = Util.prettyDateFormatter(trip.date)
        holder.binding.tvDate.text = dat
        holder.binding.tvVehicleInfo.text = trip.vehicleInfo
        holder.binding.tvAmount.text = Util.currencyFormat(trip.amount)
        holder.binding.tvSource.text = trip.source
        holder.binding.tvDesitnation.text = trip.destination
    }

    override fun getItemCount(): Int = list.size

    class TripViewHolder(val binding: TripItemBinding) : RecyclerView.ViewHolder(binding.root) {

    }
}