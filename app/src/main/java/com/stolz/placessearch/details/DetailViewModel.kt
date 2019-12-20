package com.stolz.placessearch.details

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stolz.placessearch.model.Place
import com.stolz.placessearch.model.places.Venue
import com.stolz.placessearch.network.FoursquareApi
import com.stolz.placessearch.network.GoogleMapsApi
import kotlinx.coroutines.*
import retrofit2.await
import java.io.BufferedInputStream

private val TAG = DetailViewModel::class.java.simpleName

class DetailViewModel : ViewModel() {

    // The detailed venue information
    private val _venueInformation = MutableLiveData<Venue>()
    val venueInformation: LiveData<Venue>
        get() = _venueInformation

    // The detailed venue information
    private val _staticMap = MutableLiveData<Bitmap>()
    val staticMap: LiveData<Bitmap>
        get() = _staticMap

    private var detailsJob = Job()
    private val detailsScope = CoroutineScope(detailsJob + Dispatchers.Main)

    fun getStaticMap(place: Place) { // TODO: DAGGER IN CONTEXT
        detailsScope.launch {
            val bitmap = fetchStaticMap(place)
            if (bitmap != null) {
                _staticMap.value = bitmap
            }
        }
    }

    private suspend fun fetchStaticMap(place: Place): Bitmap? {
        //        val mapWidth = Resources.getSystem().displayMetrics.widthPixels
        //        val mapHeight = 300 // TODO: ADD DIMEN
        // TODO: PX TO DP

        // FIXME: MARKER IS WRONG
        val markerString = "color:red%7c" + place.location.latitude + "," + place.location.longitude

        return withContext(Dispatchers.IO) {
            val staticMapDeferred =
                GoogleMapsApi.retrofitService.getStaticMap(placeMarker = markerString)
            try {
                val result = staticMapDeferred.await()
                val inputStream = result.byteStream()
                val bis = BufferedInputStream(inputStream)
                val bitmap = BitmapFactory.decodeStream(bis)
                Log.v(TAG, "Static map generated successfully")
                bitmap
            } catch (t: Throwable) {
                Log.e(TAG, "Static map generation failed - ${t.message}")
                null
            }
        }
    }

    fun getVenueInformation(venueId: String) {
        detailsScope.launch {
            val venueWithInfo = fetchVenueInformation(venueId)
            if (venueWithInfo != null) {
                _venueInformation.value = venueWithInfo
            }
        }
    }

    private suspend fun fetchVenueInformation(venueId: String): Venue? {
        return withContext(Dispatchers.IO) {
            val venueInformationDeferred =
                FoursquareApi.retrofitService.getVenueDetails(venueId = venueId)
            try {
                val venueDeferred = venueInformationDeferred.await()
                Log.v(TAG, "Got venue information successfully")
                venueDeferred.response.venue
            } catch (t: Throwable) {
                Log.e(TAG, "Failed retrieving venue information - ${t.message}")
                null
            }
        }
    }

    fun favoriteToggled(place: Place) {
        // TODO: IMPLEMENT
    }

    override fun onCleared() {
        super.onCleared()
        detailsJob.cancel()
    }
}