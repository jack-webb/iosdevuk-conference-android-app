package me.jackwebb.android.conferenceappkotlin.ui.locations

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_location_list.*
import me.jackwebb.android.conferenceappkotlin.R
import me.jackwebb.android.conferenceappkotlin.model.location.Location

class LocationListFragment : Fragment() {
    private val TAG = this::class.java.name

    private lateinit var viewModel: LocationListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(LocationListViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_location_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val detailListener = object : LocationRecyclerViewAdapter.ClickListener {
            override fun onClick(location: Location) {
                findNavController().navigate(
                    LocationListFragmentDirections.showLocationDetails(location)
                )
            }
        }

        val adapter = LocationRecyclerViewAdapter(detailListener)
        locationListRecyclerView.layoutManager = LinearLayoutManager(context)
        locationListRecyclerView.adapter = adapter

        viewModel.locations.observe(this, Observer {locations ->
            Log.d(TAG, "${locations.size} location(s) observed in viewModel")
            adapter.setData(locations.sortedBy { it.name })
        })
    }

}
