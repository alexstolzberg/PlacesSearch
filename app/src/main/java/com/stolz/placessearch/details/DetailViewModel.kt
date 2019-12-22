package com.stolz.placessearch.details

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stolz.placessearch.database.PlaceDao
import com.stolz.placessearch.model.Place
import com.stolz.placessearch.model.places.Venue
import com.stolz.placessearch.network.FoursquareApiService
import com.stolz.placessearch.network.GoogleMapsApiService
import kotlinx.coroutines.*
import javax.inject.Inject

class DetailViewModel @Inject constructor(
    private val context: Context,
    private val favoritesDatabase: PlaceDao,
    foursquareApiService: FoursquareApiService,
    googleMapsApiService: GoogleMapsApiService
) : ViewModel() {

    // LiveData containing detailed venue information
    private val _venueInformation = MutableLiveData<Venue>()
    val venueInformation: LiveData<Venue>
        get() = _venueInformation

    // LiveData containing the static map image
    private val _staticMap = MutableLiveData<Bitmap>()
    val staticMap: LiveData<Bitmap>
        get() = _staticMap

    private var detailsJob = Job()
    private val detailsScope = CoroutineScope(detailsJob + Dispatchers.Main)

    private val detailRepository = DetailRepository.getInstance()

    init {
        detailRepository.foursquareApiService = foursquareApiService
        detailRepository.googleMapsApiService = googleMapsApiService
    }

    fun getStaticMap(place: Place) {
        detailsScope.launch {
            val bitmap = detailRepository.fetchStaticMap(context, place)
            if (bitmap != null) {
                _staticMap.value = bitmap
            }
        }
    }

    fun getVenueInformation(venueId: String) {
        detailsScope.launch {
            val venueWithInfo = detailRepository.fetchVenueInformation(venueId)
            if (venueWithInfo != null) {
                _venueInformation.value = venueWithInfo
            }
        }
    }

    fun updateFavoriteForPlace(place: Place) {
        detailsScope.launch {
            withContext(Dispatchers.IO) {
                val placeEntity = Place.toPlaceEntity(place)
                val placeInDb = favoritesDatabase.getPlace(place.id)
                if (placeInDb == null) {
                    favoritesDatabase.insert(placeEntity)
                } else {
                    favoritesDatabase.updatePlace(placeEntity)

                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        detailsJob.cancel()
    }
}