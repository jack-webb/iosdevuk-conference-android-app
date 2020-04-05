package me.jackwebb.android.conferenceappkotlin.ui.locations

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import me.jackwebb.android.conferenceappkotlin.R
import me.jackwebb.android.conferenceappkotlin.databinding.FragmentLocationDetailBinding
import me.jackwebb.android.conferenceappkotlin.model.location.Location

class LocationDetailFragment : Fragment() {
    private val TAG = this::class.java.name

    private lateinit var location: Location
    private lateinit var mBinding: FragmentLocationDetailBinding
    private lateinit var mapView: MapView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_location_detail, container, false)

        arguments?.let{
            val safeArgs = LocationDetailFragmentArgs.fromBundle(it)
            location = safeArgs.location
        }

        mapView = mBinding.locationMapView
        mapView.onCreate(savedInstanceState)

        mapView.getMapAsync { map ->
            val coords = LatLng(location.latitude, location.longitude)
            map.addMarker(MarkerOptions().position(coords).title(location.name))
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(coords, 17.0f))
        }

        mBinding.btnDirections.setOnClickListener {
            val gmmIntentUri = Uri.parse("geo:0,0?q=${location.latitude},${location.longitude}(${location.name})?z=14")  // Slightly less zoom for better context
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }

        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mBinding.location = location
    }

    override fun onResume() {
        mapView.onResume()
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}