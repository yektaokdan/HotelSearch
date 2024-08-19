package com.yektaokdan.hotelsearch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ResultsAdapter : RecyclerView.Adapter<ResultsAdapter.HotelViewHolder>() {

    private var hotels: List<Hotel> = listOf()

    fun setHotels(hotels: List<Hotel>) {
        this.hotels = hotels
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotelViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_hotel, parent, false)
        return HotelViewHolder(view)
    }

    override fun onBindViewHolder(holder: HotelViewHolder, position: Int) {
        val hotel = hotels[position]
        holder.nameTextView.text = hotel.name
        holder.addressTextView.text = hotel.address
        holder.typeTextView.text = "Tür: ${hotel.type}"
        holder.coordsTextView.text = "Koordinatlar: ${hotel.coords}"
    }

    override fun getItemCount(): Int = hotels.size

    class HotelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.hotel_name)
        val addressTextView: TextView = itemView.findViewById(R.id.hotel_address)
        val typeTextView: TextView = itemView.findViewById(R.id.hotel_type)
        val coordsTextView: TextView = itemView.findViewById(R.id.hotel_coords)
    }
}
