package me.jackwebb.android.conferenceappkotlin.ui.locations

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import me.jackwebb.android.conferenceappkotlin.R
import me.jackwebb.android.conferenceappkotlin.databinding.FragmentLocationListItemBinding
import me.jackwebb.android.conferenceappkotlin.model.location.Location

class LocationRecyclerViewAdapter(
    private val mOnClickDetails: ClickListener
) : RecyclerView.Adapter<LocationRecyclerViewAdapter.LocationViewHolder>() {

    private var locationsData: List<Location> = emptyList()

    interface ClickListener {
        fun onClick(location: Location)
    }

    inner class LocationViewHolder(val view: View, private val binding: FragmentLocationListItemBinding) : RecyclerView.ViewHolder(view) {
        fun bind(locationData: Location, mOnClickDetails: ClickListener) {
            binding.location = locationData
            binding.locationListItem.setOnClickListener {
                mOnClickDetails.onClick(locationData)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val inflater: LayoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding: FragmentLocationListItemBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_location_list_item, parent, false)

        return LocationViewHolder(binding.root, binding)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        holder.bind(locationsData[position], mOnClickDetails)
    }

    override fun getItemCount() = locationsData.size

    internal fun setData(locations: List<Location>) {
        this.locationsData = locations
        notifyDataSetChanged()
    }

}
