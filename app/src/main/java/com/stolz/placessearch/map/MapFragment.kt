package com.stolz.placessearch.map

import android.content.res.Configuration
import android.content.res.Resources.NotFoundException
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.stolz.placessearch.R
import com.stolz.placessearch.databinding.FragmentMapBinding
import com.stolz.placessearch.model.Place
import com.stolz.placessearch.util.SEATTLE_LATITUDE
import com.stolz.placessearch.util.SEATTLE_LONGITUDE

private val TAG = MapFragment::class.java.simpleName

class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentMapBinding
    private lateinit var map: GoogleMap
    private lateinit var places: List<Place>
    private var placesMap = mutableMapOf<Marker, Place>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_map, container, false
        )
        binding.lifecycleOwner = this

        (activity as AppCompatActivity).supportActionBar?.hide()

        val args = arguments ?: return null
        val safeArgs = MapFragmentArgs.fromBundle(args)

        safeArgs.places?.let {
            places = it.toList()
        }

        binding.fab.setOnClickListener {
            activity?.onBackPressed()
        }

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        return binding.root
    }

    /**
     * This method indicates that the asynchronous request for a Google Map is complete
     * -- this method adds all of the relevant pins for search results and zooms the camera to the
     * city center.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        Log.d(TAG, "Google Map is ready!")
        map = googleMap

        try {
            val style =
                when (context?.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
                    Configuration.UI_MODE_NIGHT_YES -> MapStyleOptions.loadRawResourceStyle(
                        activity,
                        R.raw.dark_map
                    )
                    else -> MapStyleOptions.loadRawResourceStyle(activity, R.raw.light_map)
                }

            val success = googleMap.setMapStyle(style)
            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (e: NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", e)
        }


        // Add markers for all of the places in the search results
        addPlaceMarkers()

        // Add click listener to open the details screen of the marker that is clicked
        map.setOnInfoWindowClickListener {
            val place = placesMap[it]

            if (place != null) {
                view?.findNavController()
                    ?.navigate(
                        MapFragmentDirections.actionMapFragmentToDetailFragment(
                            place
                        )
                    )
            }
        }

        // Move the camera to the center coordinate
        val seattle = LatLng(
            SEATTLE_LATITUDE,
            SEATTLE_LONGITUDE
        )
        val cameraLocation = CameraUpdateFactory.newLatLngZoom(seattle, 13F)
        map.animateCamera(cameraLocation)
    }

    /**
     * Adds pins/makers for all of the places in placesList
     */
    private fun addPlaceMarkers() {
        Log.d(TAG, "addPlaceMarkers -- Adding markers for search results to Google map")
        for (place in places) {
            val markerOptions = MarkerOptions()
                .title(place.name)
                .position(place.location)
            val marker = map.addMarker(markerOptions)
            placesMap[marker] = place
        }
    }
}
